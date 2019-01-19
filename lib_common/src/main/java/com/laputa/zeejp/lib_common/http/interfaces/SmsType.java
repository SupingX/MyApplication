package com.laputa.zeejp.lib_common.http.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.laputa.zeejp.lib_common.http.interfaces.SmsType.CHANGE;
import static com.laputa.zeejp.lib_common.http.interfaces.SmsType.REGISTER;
import static com.laputa.zeejp.lib_common.http.interfaces.SmsType.TRANSFER;


@Documented
@IntDef({
        REGISTER,
        CHANGE,
        TRANSFER
        })
@Target({
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.METHOD,
}) //表示注解作用范围，参数注解，成员注解，方法注解
@Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在 .class 文件中
public @interface SmsType { //接口，定义新的注解类型
    int REGISTER = 1; // 注册
    int CHANGE = 2; // 忘记密码
    int TRANSFER = 3; // 移交设备
}
