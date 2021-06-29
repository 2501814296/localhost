package com.ssh.controller;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiZhi;
import com.ssh.service.ShuiZhiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/shuizhi")
public class ShuiZhiController {

    @Autowired
    private ShuiZhiService shuiZhiService;
    //水质统计图
    @RequestMapping("/getShuiZhiList")
    @ResponseBody
    public List<ShuiZhi> getShuiZhiList(int sid, String type) {
        return shuiZhiService.getShuiZhiList(sid, type);
    }
    //分页水质表格
    @RequestMapping("/getSomeList")
    @ResponseBody
    Page getSomeList(ShuiZhi shuiZhi, Page page) {
        return shuiZhiService.getSomeList(shuiZhi, page);
    }

    //分页水质表格
    @RequestMapping("/getSomeList1")
    @ResponseBody
    Page getSomeList1(ShuiZhi shuiZhi, Page page) {
        return shuiZhiService.getSomeList(shuiZhi, page);
    }

    @RequestMapping("/saveShuiZhi")
    @ResponseBody
    int saveShuiZhi(ShuiZhi shuiZhi) {
        return shuiZhiService.saveShuiZhi(shuiZhi);
    }

    @RequestMapping("/deleteShuiZhi")
    @ResponseBody
    public int deleteShuiZhi(String ids) {
        return shuiZhiService.deleteShuiZhi(ids);
    }

    @RequestMapping("/updateShuiZhi")
    @ResponseBody
    public int updateShuiZhi(ShuiZhi shuiZhi) {
        return shuiZhiService.updateShuiZhi(shuiZhi);
    }
}
