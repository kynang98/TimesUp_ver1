package com.tgl.timesupver1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BleScanActivity extends AppCompatActivity {

    ListView BleListView;
    BleItemAdapter mBleItemAdapter;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    boolean mScanning;
    Button BleScanBtn;
    TextView ScanStatusTextView;
    Handler mHandler;

    private final static int REQUEST_ENABLE_BT = 1;
    private static int SCAN_PERIOD = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan);
        Toolbar toolbar = findViewById(R.id.BleDeviceName2);
        setSupportActionBar(toolbar);

        mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mHandler = new Handler();
        ScanStatusTextView = findViewById(R.id.ScanStatusTextView);

        BleListView = findViewById(R.id.BleListView);
        mBleItemAdapter = new BleItemAdapter(this);
        BleListView.setAdapter(mBleItemAdapter);

        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        ScanStatusTextView.setText("Scanning");
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                ScanStatusTextView.setText("Scan done");
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        }, SCAN_PERIOD);
        mBluetoothLeScanner.startScan(mScanCallback);

        BleScanBtn = findViewById(R.id.BleScanBtn);
        BleScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanStatusTextView.setText("Scanning");

                mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        ScanStatusTextView.setText("Scan done");
                        mBluetoothLeScanner.stopScan(mScanCallback);
                    }
                }, SCAN_PERIOD);
                mBleItemAdapter.clear();
                startScanning();
            }
        });

        BleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = mBleItemAdapter.getDevice(position);
                if (device == null) return;
                final Intent intent = new Intent(BleScanActivity.this, BleDeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                if (mScanning) {
                    mBluetoothLeScanner.stopScan(mScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }
        });
    }

    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            mBleItemAdapter.addDevice(result.getDevice());
            mBleItemAdapter.notifyDataSetChanged();
        }
    };

    public void startScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mBluetoothLeScanner.startScan(mScanCallback);
            }
        });
    }

}
