package com.laputa.zeejp.lib_common.http.response;

public class FaultResponse {
    private String errorName;
    private String ilopIotId;
    private String isError;
    private String errorTime;
    private Pretreatment pretreatment;


    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(String errorTime) {
        this.errorTime = errorTime;
    }

    public Pretreatment getPretreatment() {
        return pretreatment;
    }

    public void setPretreatment(Pretreatment pretreatment) {
        this.pretreatment = pretreatment;
    }

    public String getIlopIotId() {
        return ilopIotId;
    }

    public void setIlopIotId(String ilopIotId) {
        this.ilopIotId = ilopIotId;
    }

    public String getIsError() {
        return isError;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }



    public class  Pretreatment {
        private String phoneNum;
        private String resolution;

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }
    }
}
