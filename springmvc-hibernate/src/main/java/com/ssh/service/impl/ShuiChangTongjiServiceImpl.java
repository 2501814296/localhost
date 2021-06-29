package com.ssh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssh.bean.ShuiChangTongJi;
import com.ssh.dao.ShuiChangTongjiDao;
import com.ssh.service.ShuiChangTongjiService;

@Service
public class ShuiChangTongjiServiceImpl implements ShuiChangTongjiService {
    @Autowired
    ShuiChangTongjiDao shuiChangTongjiDao;
    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    @Override
    public List<ShuiChangTongJi> getListBySid(int sid, String type) {
        return shuiChangTongjiDao.getListBySid(sid,type);
    }
}
