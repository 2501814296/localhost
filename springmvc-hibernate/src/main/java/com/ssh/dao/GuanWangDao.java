package com.ssh.dao;

import com.ssh.bean.Guanwang;
import com.ssh.bean.Page;

public interface GuanWangDao {
    // 获取一条信息
    Guanwang getWaterById(int id);

    // 分页获取数据
    Page getSomeWater(Guanwang water, Page page);

    // 改数据
    int updateWater(Guanwang water);

    //保存
    int saveWater(Guanwang water);

    //删除
    int deleteWater(String ids);

    // 统计 对官网的产水率、耗电量和供水量的统计
//    List<ShuiChangTongJi> getListBySid(int sid, String type);
}
