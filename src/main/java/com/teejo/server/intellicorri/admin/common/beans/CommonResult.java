package com.teejo.server.intellicorri.admin.common.beans;

public class CommonResult {

	public boolean ok;//是否执行成功
	public int code;//结果代码
	public Object data;//结果数据
	public String msg;//错误信息
	
	public CommonResult(boolean ok,int code,Object data,String msg){
		this.ok = ok;
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	
	public CommonResult() {
		super();
	}

	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
