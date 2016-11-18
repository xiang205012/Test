package com.xiang.viewanim.view.comm.TreeView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义属性注解，将此注解添加到对象的属性上，
 * 就可以通过反射获取该属性的值而不关心该属性的命名
 * Created by gordon on 2016/6/18.
 */
@Target(ElementType.FIELD) // FIELD 是属性注解
@Retention(RetentionPolicy.RUNTIME) // RUNTIME 保留在运行时，还可以是CLASS(编译时)或者SOURCE(会保留在源码中)
public @interface TreeNodePid {

}
