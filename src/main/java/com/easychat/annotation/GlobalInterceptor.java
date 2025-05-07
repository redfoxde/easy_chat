package com.easychat.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName GlobalInterceptor
 * @Author chenhongxin
 * @Date 2025/5/7 下午6:00
 * @mood happy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {
    //校验登录
    boolean checkLogin() default true;
    //校验管理员
    boolean checkAdmin() default false;


}
