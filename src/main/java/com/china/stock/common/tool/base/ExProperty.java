package com.china.stock.common.tool.base;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 在进行数据库操作的时候不需要的属性
 * @author dawei
 *
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExProperty {
	String value() default "";
}
