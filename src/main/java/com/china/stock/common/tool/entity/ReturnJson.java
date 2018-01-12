package com.china.stock.common.tool.entity;

import com.china.stock.common.tool.base.JsonUtil;



public class ReturnJson {

	private String resultCode;
	
	private Object msg;
	
	private Object content;
	
	public ReturnJson(){}
	
	public ReturnJson(String resultCode, Object msg){
		this.resultCode = resultCode;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return JsonUtil.jsonSerialization(this);
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
