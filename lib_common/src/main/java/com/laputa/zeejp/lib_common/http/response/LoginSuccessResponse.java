package com.laputa.zeejp.lib_common.http.response;

public class LoginSuccessResponse {

    /** 登录成功后的API调用，需要在Header中携带该token登录成功后的API调用，需要在Header中携带该token */
    public String authCode;

    /** 授权登录调用IOT SDK使用 */
    public String token;
}
