package com.laputa.zeejp.lib_common.http;

import android.support.annotation.NonNull;

import com.laputa.zeejp.lib_common.http.interfaces.QYBond;
import com.laputa.zeejp.lib_common.http.interfaces.SmsType;
import com.laputa.zeejp.lib_common.http.response.AcceptDeviceResponse;
import com.laputa.zeejp.lib_common.http.response.AdviseResponse;
import com.laputa.zeejp.lib_common.http.response.AreaResponse;
import com.laputa.zeejp.lib_common.http.response.DeviceResponse;
import com.laputa.zeejp.lib_common.http.response.FaultAndPreResponse;
import com.laputa.zeejp.lib_common.http.response.FaultDeviceResponse;
import com.laputa.zeejp.lib_common.http.response.FaultResponse;
import com.laputa.zeejp.lib_common.http.response.FeedbackResponse;
import com.laputa.zeejp.lib_common.http.response.FilterDataResponse;
import com.laputa.zeejp.lib_common.http.response.HttpResult;
import com.laputa.zeejp.lib_common.http.response.LeaseResponse;
import com.laputa.zeejp.lib_common.http.response.MessageResponse;
import com.laputa.zeejp.lib_common.http.response.MessageTypeResponse;
import com.laputa.zeejp.lib_common.http.response.MsgCountResponse;
import com.laputa.zeejp.lib_common.http.response.OwnerDeviceListResponse;
import com.laputa.zeejp.lib_common.http.response.ProductFilesResponse;
import com.laputa.zeejp.lib_common.http.response.ProductResponse;
import com.laputa.zeejp.lib_common.http.response.RecordAvgResponse;
import com.laputa.zeejp.lib_common.http.response.RecordMonthListResponse;
import com.laputa.zeejp.lib_common.http.response.ShareDeviceResponse;
import com.laputa.zeejp.lib_common.http.response.ShareUserResponse;
import com.laputa.zeejp.lib_common.http.response.SupportProductResponse;
import com.laputa.zeejp.lib_common.http.response.TdsQaResponse;
import com.laputa.zeejp.lib_common.http.response.UserInfoResponse;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * 1.APP用户在未登录之前的操作（如注册、获取验证码、验证短信验证码、登录等接口）使用accessKey验证接入。accessKey由Server端开发人员分配，通过保密途径给到APP开发者。
 * 2.登录成功后，Server端返回token，作为后续其他接口参数进行调用，需要在Http header中以Authorization为key，携带token，格式如下：
 * Authorization: token
 * token的有效期为3天
 * <p>
 * Created by Sandy Luo on 2018/7/11.
 */

public interface ApiService {

    String ACCESSKEY = "1SNc3DS4W65fYLBI03SokWOSwUHcfjV4";
    String HOST = "https://iot2.qinyuan.cn";
    String IMG_USER = HOST + "/web/api/common/getFile/";
    String URL_FILES = HOST + "/web/api/common/getFiles/";
    /**
     * apk更新地址
     */
    String URL_APK = HOST + "/files/qinyuan.apk";
    /**
     * 商城
     */
    String URL_STORE = "http://m.qinyuanmall.com";
    /**
     * 官方网站
     */
    String URL_Official_Website = "http://www.qinyuan.cn";
    /**
     * 沁园隐私保护准则
     */
    String URL_Privacy_protection_guidelines = "https://qinyuan-test.yunext.com/protocol/service_agreement.html";
    /**
     * 软件许可及服务协议
     */
    String URL_Software_license_Service_agreement = "https://qinyuan-test.yunext.com/protocol/register_agreement.html";
    /**
     * 二维码扫描地址
     */
    String URL_PRODUCT_QRCODE = "https://smart.qinyuan.cn/iot2/app/download/index.html";
    /**
     * IoT 2G 设备
     */
    String URL_IOT_2G = "http://192.168.199.197:8080/#/";

    // 2.2 登录注册

    /**
     * 2.2.1 注册
     *
     * @param phone     手机
     * @param password  密码(MD5加密后)
     * @param accessKey 接入码
     * @param code      验证码
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/register")
    Flowable<HttpResult<Object>> register(
            @Field("phone") @NonNull String phone
            , @Field("password") @NonNull String password
            , @Field("code") @NonNull String code
            , @Field("accessKey") @NonNull String accessKey
    );

    /**
     * 2.2.2 发送验证码
     *
     * @param phone     手机
     * @param accessKey 接入码
     * @param type      1 : 注册短信验证码 2 : 忘记密码验证码 3.移机
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/register/send/code")
    Flowable<HttpResult<Object>> sendCode(
            @Field("phone") @NonNull String phone
            , @Field("accessKey") @NonNull String accessKey
            , @Field("type") @SmsType int type
    );

    /**
     * 2.2.3 验证验证码
     *
     * @param code      code
     * @param phone     phone
     * @param accessKey accessKey
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/register/check/code")
    Flowable<HttpResult<Object>> checkCode(
            @Field("code") @NonNull String code
            , @Field("phone") @NonNull String phone
            , @Field("accessKey") @NonNull String accessKey
    );

    /**
     * 2.2.4 完善信息
     *
     * @param phone    phone
     * @param nickName nickName
     * @param sex      性别（1：男、2：女）
     * @param province province
     * @param city     city
     * @param region   region
     * @param address  address
     * @param token    token
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/fill/info")
    Flowable<HttpResult<Object>> fillInfo(
            @Field("phone") @NonNull String phone
            , @Field("nickName") String nickName
            , @Field("sex") String sex
            , @Field("province") String province
            , @Field("city") String city
            , @Field("region") String region
            , @Field("address") String address
            , @Header("Authorization") @NonNull String token
    );

    String SEX_MAN = "1";
    String SEX_FEMALE = "2";

    /**
     * 2.2.5 登录
     *
     * @param phone      phone
     * @param password   password
     * @param appType    APP类型 （1:Android, 2:IOS）
     * @param appVersion APP版本号
     * @param accessKey  accessKey
     * @return LoginSuccessResponse
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/login")
    Flowable<HttpResult<Object>> login(
            @Field("phone") @NonNull String phone
            , @Field("password") @NonNull String password
            , @Field("appType") @NonNull Integer appType
            , @Field("appVersion") @NonNull String appVersion
            , @Field("longitude") @NonNull String longitude
            , @Field("latitude") @NonNull String latitude
            , @Field("accessKey") @NonNull String accessKey
    );

    @FormUrlEncoded
    @POST("/web/api/app/user/login")
    Observable<HttpResult<Object>> loginObs(
            @Field("phone") @NonNull String phone
            , @Field("password") @NonNull String password
            , @Field("appType") @NonNull Integer appType
            , @Field("appVersion") @NonNull String appVersion
            , @Field("longitude") @NonNull String longitude
            , @Field("latitude") @NonNull String latitude
            , @Field("accessKey") @NonNull String accessKey
    );

    int TYPE_ANDROID = 1;
    int TYPE_IOS = 2;

    /**
     * 验证验证码
     */
    @FormUrlEncoded
    @POST("/web/api/verifyCode/verify/loginVerifyCode")
    Flowable<HttpResult<Object>> loginVerifyCode(
            @Field("phone") @NonNull String phone
            , @Field("verifyCode") @NonNull String verifyCode
            , @Field("accessKey") @NonNull String accessKey
    );

    /**
     * 获取验证码
     */
    @GET("/web/api/verifyCode/getLoginVerifyCode")
    Flowable<HttpResult<String>> getLoginVerifyCode(
            @Query("phone") @NonNull String phone
            , @Query("accessKey") @NonNull String accessKey
    );

    /**
     * 2.2.6 忘记密码
     *
     * @param phone     phone
     * @param password  密码(MD5加密后)
     * @param accessKey accessKey
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/register/change/password")
    Flowable<HttpResult<Object>> changePassword(
            @Field("phone") @NonNull String phone
            , @Field("password") @NonNull String password
            , @Field("accessKey") @NonNull String accessKey
    );

    /**
     * 2.2.7 修改密码
     *
     * @param token       token
     * @param oldPassword oldPassword
     * @param password    password
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/modify/password")
    Flowable<HttpResult<Object>> modifyPassword(
            @Header("Authorization") @NonNull String token
            , @Field("oldPassword") @NonNull String oldPassword
            , @Field("password") @NonNull String password
    );

    /**
     * 2.2.8 修改个人资料
     *
     * @param sex 性别 1男 2女
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/modifyUserInfo")
    Flowable<HttpResult<Object>> modifyUser(
            @Header("Authorization") @NonNull String authorization
            , @Field("phone") @NonNull String phone
            , @Field("userNickName") String userNickName
            , @Field("sex") String sex
            , @Field("province") String province
            , @Field("city") String city
            , @Field("region") String region
            , @Field("address") String address
    );

    /**
     * 2.2.9 获取用户资料
     */
    @POST("/web/api/app/user/getUserInfo")
    Flowable<HttpResult<UserInfoResponse>> getUserInfo(
            @Header("Authorization") @NonNull String authorization
            //, @Field("phone") @NonNull String phone
    );

    /**
     * 2.2.10 上传用户头像
     */
    @Multipart
    @POST("/web/api/app/user/uploadImg")
    Flowable<HttpResult<Object>> uploadImg(
            @Header("Authorization") @NonNull String authorization
//            , @Part() MultipartBody.Part phone
            , @Part() @NonNull MultipartBody.Part map
    );

    /**
     * 2.2.12 用户登出
     */
    @POST("/web/api/app/user/logout")
    Flowable<HttpResult<Object>> logout(
            @Header("Authorization") @NonNull String token
    );

    // 2.3 地区信息

    /**
     * 2.3.1 获取全国区域
     *
     * @param level 级别（1：省级 2：市级 3：区级）
     * @return AreaResponse
     */
    @GET("/web/api/app/address/getAreaList")
    Flowable<HttpResult<AreaResponse>> getAreaList(
            @Query("level") @NonNull Integer level
            , @Header("Authorization") @NonNull String token
    );

    /**
     * 2.3.2 获取全国区域版本号
     */
    @GET("/web/api/app/address/getAreaVersion")
    Flowable<HttpResult<AreaResponse>> getAreaVersion(
            @Header("Authorization") @NonNull String token
    );

    /**
     * just for test
     */
    @GET("/web/api/app/address/getAreaList2")
    Observable<HttpResult<AreaResponse>> getAreaList2(
            @Query("level") @NonNull Integer level
            , @Header("Authorization") @NonNull String token
    );

    int LEVEL_PROVINCE = 1;
    int LEVEL_CITY = 2;
    int LEVEL_REGION = 3;
    int PID_PROVINCE = 10;

    // 2.4 设备信息

    /**
     * 2.4.1 用户绑定设备
     *
     * @param authorization token
     * @param deviceName    deviceName
     * @param iotId         iotId
     * @param type          1:绑定 2解绑
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/bind")
    Flowable<HttpResult<Object>> bindDeviceToUser(
            @Header("Authorization") @NonNull String authorization
            , @Field("iotId") @NonNull String iotId
            , @Field("deviceName") @NonNull String deviceName
            , @Field("productName") @NonNull String productName
            , @Field("type") int type
            , @Field("province") String province
            , @Field("city") String city
            , @Field("region") String region
            , @Field("address") String address
            , @Field("longitude") String longitude
            , @Field("latitude") String latitude
    );

    /**
     * 2.4.2 设备滤芯详细信息
     */
    @GET("/web/api/app/device/filter/detail")
    Flowable<HttpResult<FilterDataResponse>> filterList(
            @Header("Authorization") @NonNull String token
            , @Query("iotId") @NonNull String iotId
            , @Query("productKey") @NonNull String productKey
    );

    /**
     * 2.4.4 用户设备列表
     */
    @GET("/web/api/app/device/list")
    Flowable<HttpResult<List<DeviceResponse>>> getDeviceListFromServer(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 2.4.5 用户设置设备昵称
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/set/deviceNickName")
    Flowable<HttpResult> setDeviceNickName(
            @Header("Authorization") @NonNull String authorization
            , @Field("deviceId") @NonNull String deviceId
            , @Field("deviceNickName") @NonNull String deviceNickName
    );

    /**
     * 2.4.6 用户分享设备
     *
     * @param receiver 被分享手机号
     * @param link     分享链接
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/share/device")
    Flowable<HttpResult<Object>> shareDevice(
            @Header("Authorization") @NonNull String authorization
            , @Field("iotId") @NonNull String iotId
            , @Field("deviceId") @NonNull String deviceId
            , @Field("deviceName") @NonNull String deviceName
            , @Field("deviceNickName") @NonNull String deviceNickName
            , @Field("receiver") @NonNull String receiver
            , @Field("link") @NonNull String link
            , @Field("productName") @NonNull String productName
    );

    /**
     * 2.4.7 被分享用户接受设备列表
     */
    @GET("/web/api/app/device/getShareDevice/list")
    Flowable<HttpResult<List<AcceptDeviceResponse>>> getShareDeviceListForShare(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 2.4.8 拒绝、接受分享设备
     *
     * @param type 1:接受 2:拒绝
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/bind/share/device")
    Flowable<HttpResult<Object>> confirmShare(
            @Header("Authorization") @NonNull String authorization
            , @Field("iotId") @NonNull String iotId
            , @Field("shareUserPhone") @NonNull String shareUserPhone
            , @Field("deviceId") @NonNull String deviceId
            , @Field("deviceName") @NonNull String deviceName
            , @Field("type") int type
    );

    /**
     * 2.4.9 撤销共享设备
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/cancel/shareDevice")
    Flowable<HttpResult<Object>> cancelShareDevice(
            @Header("Authorization") @NonNull String authorization
            , @Field("deviceId") @NonNull String deviceId
    );

    /**
     * 2.4.10主用户共享设备列表
     */
    @GET("/web/api/app/device/getSharer/shareDevice/list")
    Flowable<HttpResult<List<ShareDeviceResponse>>> getShareDeviceListForOwner(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 2.4.11共享设备对应用户列表信息
     */
    @GET("/web/api/app/device/getShareUser")
    Flowable<HttpResult<List<ShareUserResponse>>> getShareUser(
            @Header("Authorization") @NonNull String token
            , @Query("deviceId") @NonNull String deviceId
    );

    /**
     * 2.4.12取消共享用户
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/cancel/shareUser")
    Flowable<HttpResult<Object>> cancelShareUser(
            @Header("Authorization") @NonNull String authorization
            , @Field("deviceId") @NonNull String deviceId
            , @Field("userId") @NonNull String userId
    );

    /**
     * 2.4.13取水日志数据统计平均值
     *
     * @param timeType 时间类型（1 : 30天 、2 : 24小时）
     */
    @GET("/web/api/app/device/data/record/avg")
    Flowable<HttpResult<RecordAvgResponse>> getRecordAvg(
            @Header("Authorization") @NonNull String token
            , @Query("productKey") @NonNull String productKey
            , @Query("deviceName") @NonNull String deviceName
            , @Query("deviceId") @NonNull String deviceId
            , @Query("timeType") int timeType

    );

    /**
     * 2.4.14取水日志数据统计
     *
     * @param timeType 时间类型（1 : 30天 、2 : 24小时）
     */
    @GET("/web/api/app/device/data/record")
    Flowable<HttpResult<List<RecordMonthListResponse>>> getRecordList(
            @Header("Authorization") @NonNull String token
            , @Query("productKey") @NonNull String productKey
            , @Query("deviceName") @NonNull String deviceName
            , @Query("deviceId") @NonNull String deviceId
            , @Query("timeType") int timeType
    );

    /**
     * 2.4.15获取设备租赁到期设置
     */
    @GET("/web/api/app/device/getLeaseClock")
    Flowable<HttpResult<LeaseResponse>> getLeaseClock(
            @Header("Authorization") @NonNull String token
            , @Query("deviceId") @NonNull String deviceId
    );

    /**
     * 2.4.16设置设备租赁到期时间
     *
     * @param time 到期时间 2018-09-11
     * @param type 1 : 设置  2 : 取消
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/setLeaseClock")
    Flowable<HttpResult<Object>> setLeaseClock(
            @Header("Authorization") @NonNull String authorization
            , @Field("deviceId") @NonNull String deviceId
            , @Field("time") @NonNull String time
            , @Field("type") int type
    );

    /**
     * 2.4.17 设备是否能绑定
     */
    @GET("/web/api/app/device/validate/bindStatus")
    Flowable<HttpResult<Object>> isDeviceCanBond(
            @Header("Authorization") @NonNull String token
            , @Query("productKey") @NonNull String productKey
            , @Query("deviceName") @NonNull String deviceName
    );

    // 2.5 产品信息

    /**
     * 2.5.1 产品类型名称列表
     */
    @GET("/web/api/app/product/list")
    Flowable<HttpResult<List<ProductResponse>>> getProductTypeList(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 2.5.2 根据产品类型获取产品名称列表
     *
     * @param token       token
     * @param productType productType
     * @return ProductResponse
     */
    @GET("/web/api/app/product/productType/detail")
    Flowable<HttpResult<List<ProductResponse>>> getProductList(
            @Header("Authorization") @NonNull String token
            , @Query("productType") @NonNull String productType
    );

    @GET("/web/api/app/product/getProductName/version")
    Flowable<HttpResult<List<SupportProductResponse>>> getSupportProductList(
            @Header("Authorization") @NonNull String token
            , @Query("type") int type
            , @Query("version") @NonNull String version
    );

    /**
     * 2.5.4 获取产品出水口TDS显示文字级别
     */
    @GET("/web/api/app/product/tds/getConfig")
    Flowable<HttpResult<List<TdsQaResponse>>> getTdsQaList(
            @Header("Authorization") @NonNull String token
            , @Query("productKey") @NonNull String productKey
    );

    // 2.6 消息管理

    /**
     * 2.6.1查询消息类别
     *
     * @param token    token
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return MessageTypeResponse
     */
    @GET("/web/api/app/messageType/getMessageTypeList")
    Flowable<HttpResult<List<MessageTypeResponse>>> getMessageTypeList(
            @Header("Authorization") @NonNull String token
            , @Query("pageNum") @NonNull String pageNum
            , @Query("pageSize") @NonNull String pageSize
    );

    /**
     * 2.6.2 查询我的消息列表
     *
     * @param token    token
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param msgType  msgType 为 id
     * @return MessageResponse
     */
    @GET("/web/api/app/messageRecord/getMessageRecordList")
    Flowable<HttpResult<List<MessageResponse>>> getMessageRecordList(
            @Header("Authorization") @NonNull String token
            , @Query("pageNum") @NonNull String pageNum
            , @Query("pageSize") @NonNull String pageSize
            , @Query("msgType") @NonNull String msgType
    );

    /**
     * 2.6.4 查询消息详情
     */
    @GET("/web/api/app/messageRecord/getMessageRecordInfo")
    Flowable<HttpResult<List<MessageResponse>>> getMessageRecordInfo(
            @Header("Authorization") @NonNull String token
            , @Query("id") @NonNull String id
    );

    /**
     * 2.6.5 用户提交反馈
     */
    @FormUrlEncoded
    @POST("/web/api/app/feedBack/addFeedback")
    Flowable<HttpResult<Object>> addFeedback(
            @Header("Authorization") @NonNull String token
            , @Field("content") @NonNull String content
    );

    /**
     * 2.6.6 app 查询用户反馈
     */
    @GET("/web/api/app/feedBack/listFeedBacks")
    Flowable<HttpResult<List<FeedbackResponse>>> listFeedBacks(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 2.6.7 app 删除消息
     */
    @FormUrlEncoded
    @POST("/web/api/app/messageRecord/deleteMessageRecord")
    Flowable<HttpResult<Object>> deleteMessageRecord(
            @Header("Authorization") @NonNull String token
            , @Field("id") @NonNull String id
    );

    // 未知

    /**
     * 用户是否有消息
     *
     * @param token token
     * @return MsgCountResponse
     */
    @GET("/web/api/app/messageRecord/getNoReadMessageRecordInfoCount")
    Flowable<HttpResult<MsgCountResponse>> getNoReadMessageRecordInfoCount(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 获取故障信息和预处理方案
     *
     * @param deviceId {@link ApiService#getDeviceListFromServer(String)}
     */
    @GET("/web/api/app/device/getDeviceErrorAndPretreatment")
    Flowable<HttpResult<FaultAndPreResponse>> getDeviceErrorAndPretreatment(
            @Header("Authorization") @NonNull String token
            , @Query("deviceId") @NonNull String deviceId
            , @Query("productKey") @NonNull String productKey
    );

    @GET("/web/api/app/device/getDeviceErrorAndPretreatmentList")
    Flowable<HttpResult<List<FaultAndPreResponse>>> getDeviceErrorAndPretreatmentList(
            @Header("Authorization") @NonNull String token
            , @Query("deviceId") @NonNull String deviceId
            , @Query("productKey") @NonNull String productKey
    );


    /**
     * 报修设备列表
     */
    @GET("/web/api/app/device/userErrorDeviceList ")
    Flowable<HttpResult<List<FaultDeviceResponse>>> getUserErrorDeviceList(
            @Header("Authorization") @NonNull String token
    );

    /**
     * 报修
     *
     * @param dealType 1.安装/2.维修/3.保养
     */
    @FormUrlEncoded
    @POST("/web/api/app/customer/service/customerService")
    Flowable<HttpResult<Object>> customerService(
            @Header("Authorization") @NonNull String authorization
            , @Field("dealType") int dealType
//            , @Field("errorId")  String errorId
            , @Field("deviceId") @NonNull String deviceId
            , @Field("deviceName") @NonNull String deviceName
            , @Field("productType") @NonNull String productType
            , @Field("phone") @NonNull String phone
            , @Field("province") @NonNull String province
            , @Field("city") @NonNull String city
            , @Field("region") @NonNull String region
            , @Field("address") @NonNull String address
            , @Field("breakdownDescription") @NonNull String breakdownDescription

    );

    /**
     * 首页广告
     */
    @GET("/web/api/common/getAdvertisement")
    Flowable<HttpResult<List<AdviseResponse>>> getAdList();

    /**
     * 获取配网说明 使用说明
     *
     * @param token      token
     * @param productKey productKey
     * @return 配网说明 使用说明
     */
    @GET("/web/api/app/product/getProductFiles")
    Flowable<HttpResult<ProductFilesResponse>> getProductFiles(
            @Header("Authorization") @NonNull String token
            , @Query("productKey") @NonNull String productKey
    );

    /**
     * 获取版本号
     */
    @Streaming
    @GET("/files/qinyuan-version.json")
    Observable<ResponseBody> getVersionInfo();

    // delete or test

    /**
     * 验证iLop authCode delete
     */
    @FormUrlEncoded
    @POST("/web/api/app/user/check/authCode")
    Flowable<HttpResult<Object>> checkAuthCode(
            @Header("Authorization") @NonNull String authorization
            , @Field("authCode") @NonNull String authCode
    );

    /**
     * delete
     **/
    @Streaming
    @FormUrlEncoded
    @POST("/web/api/app/user/getImg")
    Flowable<ResponseBody> getUserImg(
            @Header("Authorization") @NonNull String authorization
            , @Field("id") @NonNull String id
    );

    /**
     * 个人头像 delete
     */
    @Streaming
    @GET("/web/api/common/getFile/{fileName}")
    Flowable<ResponseBody> getUserImg2(
            @Header("Authorization") @NonNull String token
            , @Path("fileName") @NonNull String fileName
    );

    /**
     * 修改用户信息 delete
     */
    @Multipart
    @POST("/web/api/app/user/modifyUserInfo")
    Flowable<HttpResult<Object>> modifyUser(
            @Header("Authorization") @NonNull String authorization
            , @Part() List<MultipartBody.Part> fileList
    );

    /**
     * 故障信息 delete 弃用
     */
    @GET("/web/api/app/device/userDeviceAndErrorDeviceList")
    Flowable<HttpResult<List<FaultResponse>>> getDeviceErrorList(
            @Header("Authorization") @NonNull String token
    );

    /**
     * just for test
     */
    @GET("/web/ api/common/getAdvertisement2")
    Observable<HttpResult<List<AdviseResponse>>> getAdList2();


    /**
     * 获取拥有设备列表
     */
    @GET("/web/api/app/device/userDeviceList")
    Flowable<HttpResult<List<OwnerDeviceListResponse>>> getOwenDeviceList(
            @Header("Authorization") @NonNull String authorization
    );
    
    /**
     * 移交设备给某人
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/confirmTransferDevice")
    Flowable<HttpResult<Object>> transfer(
            @Header("Authorization") @NonNull String authorization
            , @Field("receiverPhone") @NonNull String receiverPhone
            , @Field("transferDevice") @NonNull String transferDevice
    );


    //                      【2G】 start                      //

    /**
     * 绑定IoT设备
     */
    @FormUrlEncoded
    @POST("/web/api/app/device/iotBind")
    Flowable<HttpResult<Object>> iotBind(
            @Header("Authorization") @NonNull String authorization
            , @Field("identityId") @NonNull String identityId
            , @Field("snCode") @NonNull String snCode
            , @Field("productKey") @NonNull String productKey
            , @Field("type") @QYBond int type
            , @Field("province") String province
            , @Field("city") String city
            , @Field("region") String region
            , @Field("address") String address
            , @Field("latitude") String latitude
            , @Field("longitude") String longitude
    );



    /**
     *
     */

    //                      【2G】 end                        //

}
