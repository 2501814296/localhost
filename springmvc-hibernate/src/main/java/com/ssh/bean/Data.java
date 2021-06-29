package com.ssh.bean;

import java.util.List;
// 返回数据封装
public class Data {
	
private int code=0; //状态码
private String msg=""; //返回信息
private int count=0; //查询结果数量
private List<?> data; //查询出来的数据
public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}



}
