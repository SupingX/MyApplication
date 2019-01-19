package com.laputa.zeejp.lib_common.net;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JiaMiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        //http://127.0.0.1/test/upload/img?userName=xiaoming&userPassword=12345
        String scheme = url.scheme();//  http https
        String host = url.host();//   127.0.0.1
        String path = url.encodedPath();//  /test/upload/img
        String query = url.encodedQuery();//  userName=xiaoming&userPassword=12345

        StringBuffer sb = new StringBuffer();
        sb.append(scheme).append(url).append(path).append("?");
        Set<String> queryList = url.queryParameterNames();
        Iterator<String> iterator = queryList.iterator();

        for (int i = 0; i < queryList.size(); i++) {

            String queryName = iterator.next();
            sb.append(queryName).append("=");
            String queryKey = url.queryParameter(queryName);
            //对query的key进行加密
            sb.append(getMD5(queryKey));
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }


        String newUrl = sb.toString();


        RequestBody body = request.body();
//        String bodyToString = requestBodyToString(body);
//        TestBean testBean = GsonTools.changeGsonToBean(bodyToString, TestBean.class);
//        String userPassword = testBean.getUserPassword();
//        //加密body体中的用户密码
//        testBean.setUserPassword(CommonUtils.getMD5(userPassword));
//
//        String testGsonString = GsonTools.createGsonString(testBean);
        String testGsonString = "";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), testGsonString);
        Request.Builder builder = request.newBuilder()
                .post(requestBody)
                .url(newUrl);

        // header
        int paths = 0;
        switch (paths) {
            case 1:
                builder.addHeader("token","token");
                break;
            case 2:
                builder.addHeader("token","token");
                builder.addHeader("uid","uid");
                break;
        }


        return chain.proceed(builder.build());
    }

    private char[] getMD5(String queryKey) {
        return new char[0];
    }
}
