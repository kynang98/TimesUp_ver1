package com.tgl.timesupver1;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class BleItemAdapter extends BaseAdapter {
    ArrayList<BluetoothDevice> mLeDevices;
    LayoutInflater mInflater;


    void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    void clear() {
        mLeDevices.clear();
    }

    public BleItemAdapter(Context c){
        super();
        mLeDevices = new ArrayList<>();
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        BleScanActivity.ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflater.inflate(R.layout.ble_list, null);
            viewHolder = new BleScanActivity.ViewHolder();
            viewHolder.deviceAddress = view.findViewById(R.id.device_address);
            viewHolder.deviceName = view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BleScanActivity.ViewHolder) view.getTag();
        }

        BluetoothDevice device = mLeDevices.get(i);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
    }
}
