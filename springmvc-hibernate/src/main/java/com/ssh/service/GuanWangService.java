package com.ssh.service;

import com.ssh.bean.Guanwang;
import com.ssh.bean.Page;

public interface GuanWangService {
    // 获取一条信息
    public Guanwang getWaterById(int id);

    // 分页获取数据
    public Page getSomeWater(Guanwang water, Page page);

    // 改数据
    public int updateWater(Guanwang water);

    //保存
    public int saveWater(Guanwang water);

    //删除
    public int deleteWater(String ids);

//    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
//    public List<ShuiChangTongJi> getListBySid(int sid, String type);
}
