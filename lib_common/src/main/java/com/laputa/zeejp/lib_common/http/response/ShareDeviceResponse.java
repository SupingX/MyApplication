package com.laputa.zeejp.lib_common.http.response;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareDeviceResponse implements Parcelable {
    private String deviceNickName;
    private String deviceId;
    private String deviceName;
    private String productName;

    protected ShareDeviceResponse(Parcel in) {
        deviceNickName = in.readString();
        deviceId = in.readString();
        deviceName = in.readString();
        productName = in.readString();
    }

    public static final Creator<ShareDeviceResponse> CREATOR = new Creator<ShareDeviceResponse>() {
        @Override
        public ShareDeviceResponse createFromParcel(Parcel in) {
            return new ShareDeviceResponse(in);
        }

        @Override
        public ShareDeviceResponse[] newArray(int size) {
            return new ShareDeviceResponse[size];
        }
    };

    public String getDeviceNickName() {
        return deviceNickName;
    }

    public void setDeviceNickName(String deviceNickName) {
        this.deviceNickName = deviceNickName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceNickName);
        dest.writeString(deviceId);
        dest.writeString(deviceName);
        dest.writeString(productName);
    }
}


