package com.china.stock.common.tool.entity;

import java.util.HashMap;
import java.util.Map;

import com.china.stock.common.tool.base.StrUtils;




public class ResponseData {

	private String message;
	
	private Map<String,Object> data;
	
	public ResponseData(){}
	
	public ResponseData(String message, Map<String,Object> data){
		if(StrUtils.isEmpty(message)){
			this.message="";
		}else{
			this.message = message;
		}
		
		if(data==null){
			this.data = new HashMap<String,Object>();
		}else{
			this.data = data;
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
