package com.china.stock.common.util;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.serializer.PropertyFilter;

/**
 * FASTJSON属性过滤器
 * 
 * @author MrChu
 * @version 1.0
 * @date 2015年3月12日
 */
public class FastjsonPropertyFilter implements PropertyFilter {

	private final Set<String> includes = new HashSet<String>();
	private final Set<String> excludes = new HashSet<String>();

	public boolean apply(Object source, String name, Object value) {
		if (excludes.contains(name)) {
			return false;
		}
		if (excludes.contains(source.getClass().getSimpleName() + "." + name)) {
			return false;
		}
		if (includes.size() == 0 || includes.contains(name)) {
			return true;
		}
		if (includes.contains(source.getClass().getSimpleName() + "." + name)) {
			return true;
		}
		return false;
	}

	public Set<String> getIncludes() {
		return includes;
	}

	public Set<String> getExcludes() {
		return excludes;
	}

}