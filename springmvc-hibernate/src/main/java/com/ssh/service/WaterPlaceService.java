package com.ssh.service;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.WaterPlace;

import java.util.List;

public interface WaterPlaceService {
    // 获取一个源水地信息
    public WaterPlace getWaterById(int id);

    // 分页获取数据
    public Page getSomeWater(WaterPlace water, Page page);

    // 改数据
    public int updateWater(WaterPlace water);

    //保存
    public int saveWater(WaterPlace water);

    //删除
    public int deleteWater(String ids);

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    public List<ShuiChangTongJi> getListBySid(int sid, String type);
}
