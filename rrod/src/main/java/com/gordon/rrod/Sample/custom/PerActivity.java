package com.gordon.rrod.Sample.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 自定义Dagger2注解，其作用域为activity范围内
 * Created by gordon on 2016/5/29.
 */
@Scope // 定义作用域
@Documented//规范要求是Documented，当然不写也问题不大，但是建议写，做提示作用
@Retention(RetentionPolicy.RUNTIME)// 编译器Annotation储存于class档中，可由VM读入。Runtime级别
public @interface PerActivity {
}
