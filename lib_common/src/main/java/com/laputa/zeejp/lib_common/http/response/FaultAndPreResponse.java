package com.laputa.zeejp.lib_common.http.response;

public class FaultAndPreResponse {
    private String errorName;
    private String errorCode;
    private String errorId;
    private Pretreatment pretreatment ;

    public String getErrorId() {
        return errorId;
    }
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Pretreatment getPretreatment() {
        return pretreatment;
    }

    public void setPretreatment(Pretreatment pretreatment) {
        this.pretreatment = pretreatment;
    }

    public class Pretreatment{
        private String id;
        private String resolution;
        private String phoneNum;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }
    }
}
