package com.ssh.service.impl;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.WaterPlace;
import com.ssh.dao.ShuiChangTongjiDao;
import com.ssh.dao.WaterPlaceDao;
import com.ssh.service.WaterPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterPlaceServiceImpl implements WaterPlaceService {
    @Autowired
    private WaterPlaceDao waterDao;
    @Autowired
    private ShuiChangTongjiDao shuiChangTongjiDao;

    // 获取一个数据
    @Override
    public WaterPlace getWaterById(int id) {
        return waterDao.getWaterById(id);
    }

    // 表格获取
    @Override
    public Page getSomeWater(WaterPlace water, Page page) {
        return waterDao.getSomeWater(water, page);
    }

    @Override
    public int updateWater(WaterPlace water) {
        return waterDao.updateWater(water);
    }

    @Override
    public int saveWater(WaterPlace water) {
        return waterDao.saveWater(water);
    }

    @Override
    public int deleteWater(String ids) {
        return waterDao.deleteWater(ids);
    }

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    @Override
    public List<ShuiChangTongJi> getListBySid(int sid, String type) {
        return waterDao.getListBySid(sid,type);
    }
}
