package com.ssh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.Water;
import com.ssh.service.ShuiChangTongjiService;
import com.ssh.service.WaterService;

@Controller
@RequestMapping("/water")
public class WaterController {
    @Autowired
    private WaterService waterService;
    @Autowired
    private ShuiChangTongjiService shuiChangTongjiService;

    @RequestMapping("/getWaterById")
    @ResponseBody
    public Water getWaterById(Water water) {

        return waterService.getWaterById(water.getId());
    }

    @RequestMapping("/getSomeWater")
    @ResponseBody
    public Page getSomeWater(Water water, Page page) {
        return waterService.getSomeWater(water, page);
    }

    @RequestMapping("/saveWater")
    @ResponseBody
    public int saveWater(Water water) {
        return waterService.saveWater(water);
    }

    @RequestMapping("/updateWater")
    @ResponseBody
    public int updateWater(Water water) {
        return waterService.updateWater(water);
    }

    @RequestMapping("/deleteWater")
    @ResponseBody
    public int updateWater(String ids) {
        return waterService.deleteWater(ids);
    }

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    @RequestMapping("/getListBySid")
    @ResponseBody
    public List<ShuiChangTongJi> getListBySid(String sid, String type) {
        return shuiChangTongjiService.getListBySid(Integer.valueOf(sid), type);
    }
}
