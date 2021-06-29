package com.ssh.bean;

import java.util.List;

// 分页数据表
public class Page {
private int page;//页码 第几页
private int limit; //每页显示多少数据
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

public List<?> getData() {
	return data;
}
public void setData(List<?> data) {
	this.data = data;
}
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public int getLimit() {
	return limit;
}
public void setLimit(int limit) {
	this.limit = limit;
}

}
