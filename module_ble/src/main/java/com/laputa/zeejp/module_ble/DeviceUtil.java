package com.laputa.zeejp.module_ble;

import android.text.TextUtils;

import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DeviceUtil {

    public static RxBleDevice parseData(ScanResult scanResult) {
        byte[] bytes = scanResult.getScanRecord().getBytes();
//        if (bytes == null) {
//            return null;
//        }
        RxBleDevice bleDevice = scanResult.getBleDevice();
        if (TextUtils.isEmpty(bleDevice.getName())|| TextUtils.isEmpty(bleDevice.getMacAddress())){
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0)
                break;
            byte type = buffer.get();
            length -= 1;
            switch (type) {
                case 0x01: // Flags
                    length--;
                    break;
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                case 0x14: // List of 16-bit Service Solicitation UUIDs
//                    while (length >= 2) {
//                        parsedAd.UUIDs.add(UUID.fromString(String.format(
//                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
//                        length -= 2;
//                    }
                    break;
                case 0x04: // Partial list of 32 bit service UUIDs
                case 0x05: // Complete list of 32 bit service UUIDs
//                    while (length >= 4) {
//                        parsedAd.UUIDs.add(UUID.fromString(String.format(
//                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getInt())));
//                        length -= 4;
//                    }
                    break;
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
//                        parsedAd.UUIDs.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;
                case 0x06: // Partial list of 128-bit UUIDs
                case 0x15: // List of 128-bit Service Solicitation UUIDs
//                    while (length >= 16) {
//                        long lsb = buffer.getLong();
//                        long msb = buffer.getLong();
//                        parsedAd.UUIDs.add(new UUID(msb, lsb));
//                        length -= 16;
//                    }
                    break;
                case 0x08: // Short local device name
                case 0x09: // Complete local device name
                    byte sb[] = new byte[length];
                    buffer.get(sb, 0, length);
                    length = 0;
                    break;
                case (byte) 0xFF: // Manufacturer Specific Data
                    length -= 2;
                    break;
                default: // skip
                    break;
            }
            if (length > 0) {
                buffer.position(buffer.position() + length);
            }
        }
        return scanResult.getBleDevice();
    }
}
