package com.ssh.dao;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.WaterPlace;

import java.util.List;

public interface WaterPlaceDao {
    // 获取一个源水地信息
    WaterPlace getWaterById(int id);

    // 分页获取数据
    Page getSomeWater(WaterPlace water, Page page);

    // 改数据
    int updateWater(WaterPlace water);

    //保存
    int saveWater(WaterPlace water);

    //删除
    int deleteWater(String ids);

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    List<ShuiChangTongJi> getListBySid(int sid, String type);
}
