package com.ssh.controller;

import com.ssh.bean.Guanwang;
import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.service.GuanWangService;
import com.ssh.service.WaterPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/guanwang")
public class GuanWangController {

    @Autowired
    private GuanWangService waterPlaceService;

    @Autowired
    private WaterPlaceService wService;

    @RequestMapping("/getWaterPlaceById")
    @ResponseBody
    public Guanwang getWaterPlaceById(Guanwang water) {

        return waterPlaceService.getWaterById(water.getId());
    }

    @RequestMapping("/getSomeWaterPlace")
    @ResponseBody
    public Page getSomeWater(Guanwang WaterPlace, Page page) {
        return waterPlaceService.getSomeWater(WaterPlace, page);
    }

    @RequestMapping("/saveWaterPlace")
    @ResponseBody
    public int saveWater(Guanwang WaterPlace) {
        return waterPlaceService.saveWater(WaterPlace);
    }

    @RequestMapping("/updateWaterPlace")
    @ResponseBody
    public int updateWater(Guanwang WaterPlace) {
        return waterPlaceService.updateWater(WaterPlace);
    }

    @RequestMapping("/deleteWaterPlace")
    @ResponseBody
    public int updateWater(String ids) {
        return waterPlaceService.deleteWater(ids);
    }

    @RequestMapping("/getListBySid")
    @ResponseBody
    public List<ShuiChangTongJi> getListBySid(String sid, String type) {
        return wService.getListBySid(Integer.valueOf(sid), type);
    }

}
