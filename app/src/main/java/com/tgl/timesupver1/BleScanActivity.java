package com.tgl.timesupver1;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

import static android.util.Log.d;

@SuppressWarnings("deprecation")
public class BleScanActivity extends AppCompatActivity {

    ListView BleListView;
    BleItemAdapter mBleItemAdapter;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
//    BluetoothLeScanner mBluetoothLeScanner;
    BluetoothLeService mBluetoothLeService;
    BluetoothDevice device;
    BluetoothGatt mBluetoothGatt;
    BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    Button BleScanBtn;
    TextView ScanStatusTextView;
    TextView BleConnectionTextView;
    Handler mHandler;
    User user;

    private final static int REQUEST_ENABLE_BT = 1;
    private static int SCAN_PERIOD = 10000;
    public final static UUID UUID_EXAM_CODE_SERV = UUID.fromString("00000001-710e-4a5b-8d75-3e5b444bc3cf");
    public final static UUID UUID_EXAM_CODE_CHAR = UUID.fromString("00000002-710e-4a5b-8d75-3e5b444bc3cf");
    public String EXAMCODE;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scan);
        Toolbar toolbar = findViewById(R.id.BleDeviceName2);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        user = (User) b.getSerializable("object");

        mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mHandler = new Handler();
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
//        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        ScanStatusTextView = findViewById(R.id.ScanStatusTextView);
        BleConnectionTextView = findViewById(R.id.BleConnectionTextView);

        BleListView = findViewById(R.id.BleListView);
        mBleItemAdapter = new BleItemAdapter(this);
        BleListView.setAdapter(mBleItemAdapter);

        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        ScanStatusTextView.setText("Scanning");
        mHandler.postDelayed(new Runnable(){
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                ScanStatusTextView.setText("Scan done");
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);
        mBluetoothAdapter.startLeScan(mLeScanCallback);

        BleScanBtn = findViewById(R.id.BleScanBtn);
        BleScanBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                mBleItemAdapter.clear();
                ScanStatusTextView.setText("Scanning");

                mHandler.postDelayed(new Runnable(){
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        ScanStatusTextView.setText("Scan done");
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);

                startScanning();
            }
        });

        BleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                device = mBleItemAdapter.getDevice(i);
                if (device == null) return;
                Intent intent = new Intent(BleScanActivity.this, Exam_Lobby.class);
                intent.putExtra("object",user);
//                intent.putExtra(BleDeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//                intent.putExtra(BleDeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                startActivity(intent);
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                ScanStatusTextView.setText("Scan done");
                EXAMCODE = "Form 3 Geografi";
//                Intent gattServiceIntent = new Intent(BleScanActivity.this, BluetoothLeService.class);
//                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                d("pong", "it pass");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBleItemAdapter.addDevice(device);
                            mBleItemAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    public void startScanning() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        });
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(device.getAddress());
            mBluetoothGattCharacteristic =
                    mBluetoothGatt.getService(UUID_EXAM_CODE_SERV)
                            .getCharacteristic(UUID_EXAM_CODE_CHAR);

            final byte[] data = mBluetoothGattCharacteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                d("pong2", new String(data));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                updateConnectionState(R.string.connected);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                updateConnectionState(R.string.disconnected);

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){
                d("pong", intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BleConnectionTextView.setText(resourceId);
            }
        });
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
