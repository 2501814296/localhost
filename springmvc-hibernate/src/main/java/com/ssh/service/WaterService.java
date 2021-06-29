package com.ssh.service;

import com.ssh.bean.Page;
import com.ssh.bean.Water;

public interface WaterService {
    // 获取一个水厂信息
    public Water getWaterById(int id);

    // 分页获取数据
    public Page getSomeWater(Water water, Page page);

    // 改数据
    public int updateWater(Water water);

    //保存
    public int saveWater(Water water);

    //删除
    public int deleteWater(String ids);
}
