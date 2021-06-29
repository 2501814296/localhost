package com.ssh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssh.bean.Page;
import com.ssh.bean.Water;
import com.ssh.dao.WaterDao;
import com.ssh.service.WaterService;

@Service
public class WaterServiceImpl implements WaterService {
    @Autowired
    private WaterDao waterDao;

    // 获取一个数据
    @Override
    public Water getWaterById(int id) {
        return waterDao.getWaterById(id);
    }

    // 表格获取
    @Override
    public Page getSomeWater(Water water, Page page) {
        return waterDao.getSomeWater(water, page);
    }

    @Override
    public int updateWater(Water water) {
        return waterDao.updateWater(water);
    }

    @Override
    public int saveWater(Water water) {
        return waterDao.saveWater(water);
    }

    @Override
    public int deleteWater(String ids) {
        return waterDao.deleteWater(ids);
    }
}
