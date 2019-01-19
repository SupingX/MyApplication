package com.laputa.zeejp.lib_common.http.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.laputa.zeejp.lib_common.http.interfaces.QYBond.BOND;
import static com.laputa.zeejp.lib_common.http.interfaces.QYBond.UN_BOND;


@Documented
@IntDef({
        BOND,
        UN_BOND
        })
@Target({
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.METHOD,
}) //表示注解作用范围，参数注解，成员注解，方法注解
@Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在 .class 文件中
public @interface QYBond { //接口，定义新的注解类型
    int BOND = 1; // 绑定
    int UN_BOND = 0; // 解绑
}
