package com.laputa.zeejp.lib_common.http.response;

import java.util.List;

public class OwnerDeviceListResponse {
    private List<OwnerDeviceResponse> deviceList;
    private String productKey;
    private String productName;

    public List<OwnerDeviceResponse> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<OwnerDeviceResponse> deviceList) {
        this.deviceList = deviceList;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public static class OwnerDeviceResponse{
        private int owner;
        private String deviceId;
        private String deviceName;
        private String productKey;
        private String productName;
        private String deviceNickName;
        private String netType;
        private String iotId;
        private String identityId;

        public int getOwner() {
            return owner;
        }

        public void setOwner(int owner) {
            this.owner = owner;
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

        public String getNetType() {
            return netType;
        }

        public void setNetType(String netType) {
            this.netType = netType;
        }

        public String getIotId() {
            return iotId;
        }

        public void setIotId(String iotId) {
            this.iotId = iotId;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getDeviceNickName() {
            return deviceNickName;
        }

        public void setDeviceNickName(String deviceNickName) {
            this.deviceNickName = deviceNickName;
        }

        public String getIdentityId() {
            return identityId;
        }

        public void setIdentityId(String identityId) {
            this.identityId = identityId;
        }
    }
}
