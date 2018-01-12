package com.china.stock.common.tool.base;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 标记模型中某一属性是否是唯一属性
 * @author dawei
 *
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Primarykey {
	String value() default "";
}
