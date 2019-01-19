package com.laputa.zeejp.module_ble;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeviceAdapter extends BaseQuickAdapter<RxBleDevice, BaseViewHolder> {
    public DeviceAdapter(@Nullable List<RxBleDevice> data) {
        super(R.layout.item_device, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RxBleDevice item) {
        helper.setText(R.id.tv_text, item.getName() + " - " + item.getMacAddress());
        helper.addOnClickListener(R.id.tv_text);
    }

    public void addRxBleDevice(RxBleDevice rxBleDevice) {
        // Not the best way to ensure distinct devices, just for sake on the demo.
        List<RxBleDevice> data = getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getMacAddress().equals(rxBleDevice.getMacAddress())) {
                data.set(i, rxBleDevice);
                notifyItemChanged(i);
                return;
            }
        }

        data.add(rxBleDevice);
        Collections.sort(data, SORTING_COMPARATOR);
        notifyDataSetChanged();
    }

    public void clearRxBleDevice() {
        getData().clear();
        notifyDataSetChanged();
    }

    public RxBleDevice getRxBleDevice(int position){
        return getData().get(position);
    }

    private static final Comparator<RxBleDevice> SORTING_COMPARATOR = (lhs, rhs) ->
            lhs.getMacAddress().compareTo(rhs.getMacAddress());
}
