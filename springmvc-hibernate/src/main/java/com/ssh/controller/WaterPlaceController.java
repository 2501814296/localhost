package com.ssh.controller;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.WaterPlace;
import com.ssh.service.WaterPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/waterPlace")
public class WaterPlaceController {

    @Autowired
    private WaterPlaceService waterPlaceService;

    @RequestMapping("/getWaterPlaceById")
    @ResponseBody
    public WaterPlace getWaterPlaceById(WaterPlace water) {

        return waterPlaceService.getWaterById(water.getId());
    }

    @RequestMapping("/getSomeWaterPlace")
    @ResponseBody
    public Page getSomeWater(WaterPlace WaterPlace, Page page) {
        return waterPlaceService.getSomeWater(WaterPlace, page);
    }

    @RequestMapping("/saveWaterPlace")
    @ResponseBody
    public int saveWater(WaterPlace WaterPlace) {
        return waterPlaceService.saveWater(WaterPlace);
    }

    @RequestMapping("/updateWaterPlace")
    @ResponseBody
    public int updateWater(WaterPlace WaterPlace) {
        return waterPlaceService.updateWater(WaterPlace);
    }

    @RequestMapping("/deleteWaterPlace")
    @ResponseBody
    public int updateWater(String ids) {
        return waterPlaceService.deleteWater(ids);
    }

    // 源水地统计 对水厂的产水率、耗电量和供水量的统计
    @RequestMapping("/getListBySid")
    @ResponseBody
    public List<ShuiChangTongJi> getListBySid(String sid, String type) {
        return waterPlaceService.getListBySid(Integer.valueOf(sid), type);
    }

}
