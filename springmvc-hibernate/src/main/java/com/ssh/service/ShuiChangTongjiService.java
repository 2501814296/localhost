package com.ssh.service;

import java.util.List;

import com.ssh.bean.ShuiChangTongJi;

public interface ShuiChangTongjiService {
    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    public List<ShuiChangTongJi> getListBySid(int sid,String type);
}
