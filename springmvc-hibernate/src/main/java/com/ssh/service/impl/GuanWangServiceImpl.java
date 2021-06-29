package com.ssh.service.impl;

import com.ssh.bean.Guanwang;
import com.ssh.bean.Page;
import com.ssh.dao.GuanWangDao;
import com.ssh.dao.ShuiChangTongjiDao;
import com.ssh.service.GuanWangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuanWangServiceImpl implements GuanWangService {
    @Autowired
    private GuanWangDao waterDao;
    @Autowired
    private ShuiChangTongjiDao shuiChangTongjiDao;

    // 获取一个数据
    @Override
    public Guanwang getWaterById(int id) {
        return waterDao.getWaterById(id);
    }

    // 表格获取
    @Override
    public Page getSomeWater(Guanwang water, Page page) {
        return waterDao.getSomeWater(water, page);
    }

    @Override
    public int updateWater(Guanwang water) {
        return waterDao.updateWater(water);
    }

    @Override
    public int saveWater(Guanwang water) {
        return waterDao.saveWater(water);
    }

    @Override
    public int deleteWater(String ids) {
        return waterDao.deleteWater(ids);
    }

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
//    @Override
//    public List<ShuiChangTongJi> getListBySid(int sid, String type) {
//        return waterDao.getListBySid(sid,type);
//    }
}
