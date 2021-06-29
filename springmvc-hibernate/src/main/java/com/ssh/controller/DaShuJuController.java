package com.ssh.controller;

import com.ssh.bean.DashujuVO;
import com.ssh.bean.Guanwang;
import com.ssh.bean.Water;
import com.ssh.bean.WaterPlace;
import com.ssh.service.GuanWangService;
import com.ssh.service.WaterPlaceService;
import com.ssh.service.WaterService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashuju")
public class DaShuJuController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GuanWangService guanWangService;

    @Autowired
    private WaterService waterService;

    @Autowired
    private WaterPlaceService waterPlaceService;

    @RequestMapping("/getditupoint")
    @ResponseBody
    public JSONObject getditupoint(String type) {
        JSONObject json = new JSONObject();
        json.put("flag", "0");
        String sql = "select t.* from shuichang t where t.jingdu is not null and t.weidu is not null and t.name is not null";
        String sql1 = "select t.* from yuanshuidi t where t.jingdu is not null and t.weidu is not null and t.name is not null";
        String sql2 = "select t.* from guanwang t where t.jingdu is not null and t.weidu is not null and t.guanwangname is not null and t.name is not null";
        List<DashujuVO> result = new ArrayList<>();
        if ("0".equals(type)) {
            List<Water> waterList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Water.class));
            List<WaterPlace> waterPlaceList = jdbcTemplate.query(sql1,new BeanPropertyRowMapper<>(WaterPlace.class));
            List<Guanwang> guanwangList = jdbcTemplate.query(sql2,new BeanPropertyRowMapper<>(Guanwang.class));
            if(waterList.size() > 0){
                for(Water entity : waterList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getName());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
            if(waterPlaceList.size() > 0){
                for(WaterPlace entity : waterPlaceList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getName());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
            if(guanwangList.size() > 0){
                for(Guanwang entity : guanwangList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getGuanwangname());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
        }else if("1".equals(type)){
            List<Guanwang> guanwangList = jdbcTemplate.query(sql2,new BeanPropertyRowMapper<>(Guanwang.class));
            if(guanwangList.size() > 0){
                for(Guanwang entity : guanwangList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getGuanwangname());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
        }else if("2".equals(type)){
            List<Water> waterList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Water.class));
            if(waterList.size() > 0){
                for(Water entity : waterList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getName());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
        }else if("3".equals(type)){
            List<WaterPlace> waterPlaceList = jdbcTemplate.query(sql1,new BeanPropertyRowMapper<>(WaterPlace.class));
            if(waterPlaceList.size() > 0){
                for(WaterPlace entity : waterPlaceList){
                    DashujuVO vo = new DashujuVO();
                    vo.setDw(entity.getName());
                    vo.setX(entity.getJingdu());
                    vo.setY(entity.getWeidu());
                    result.add(vo);
                }
            }
        }
        json.put("result",result);
        json.put("flag", "1");
        return json;
    }

    @RequestMapping("/gettongji")
    @ResponseBody
    public JSONObject gettongji(String type) {
        JSONObject json = new JSONObject();
        json.put("flag","0");
        String sql = "select count(*) from shuichang";
        String sql1 = "select count(*) from yuanshuidi";
        String sql2 = "select sum(changdu) from guanwang";
        int shuichang = jdbcTemplate.queryForObject(sql,Integer.class);
        int yuanshuidi = jdbcTemplate.queryForObject(sql1,Integer.class);
        int guanwang = jdbcTemplate.queryForObject(sql2,Integer.class);
        json.put("flag","1");
        if("0".equals(type)){
            json.put("a","平台概况");
            json.put("b","水厂总数,"+shuichang);
            json.put("c","管网总长度(km),"+guanwang);
            json.put("d","水源地总数,"+yuanshuidi);
            json.put("e","设备总数,256");
            json.put("f","累计供水量(m³),20300");
            json.put("g","累计水费(万元),6030");
        }else if("1".equals(type)){
            json.put("a","管网概况");
            json.put("b","管网总长度(km),"+guanwang);
            json.put("c","总管线数,23");
            json.put("d","设备总数,50");
            json.put("e","累计入口流量(m³),2560");
            json.put("f","累计出口流量(m³),2030");
            json.put("g","");
        }else if("2".equals(type)){
            json.put("a","水厂概况");
            json.put("b","水厂总数,"+shuichang);
            json.put("c","累计供水量(m³),20300");
            json.put("d","累计水费(万元),6030");
            json.put("e","累计耗电量(kwh),8000");
            json.put("f","综合水质合格率,1");
            json.put("g","");
        }else if("3".equals(type)){
            json.put("a","水源地概况");
            json.put("b","水源地总数,"+yuanshuidi);
            json.put("c","设备总数,100");
            json.put("d","累计供水量(m³),20900");
            json.put("e","累计耗电量(kwh),70000");
            json.put("f","综合水质合格率,0.9");
            json.put("g","");
        }
        return json;
    }

    @RequestMapping("/getyujingtu")
    @ResponseBody
    public JSONObject getyujingtu(String type) {
        JSONObject json = new JSONObject();
        json.put("flag","0");
        if("0".equals(type)){
            // 综合-预警分析
            String sql = "select count(*) from guanwang t where t.maxyali > 1000 and t.liuliang > 1000";
            String sql1 = "select count(*) from shuizhi t where (hunzhuo > 10 or sedu > 10 or youli > 10 or junluo > 10)";
            int guanwang = jdbcTemplate.queryForObject(sql,Integer.class);
            int yuanshuidi = jdbcTemplate.queryForObject(sql1,Integer.class);
            json.put("flag","1");
            json.put("a","管网预警,"+guanwang);
            json.put("b","水源地预警,"+yuanshuidi);
            json.put("c","水厂预警,0");
            json.put("d","A预警,0");
            json.put("e","B预警,0");
            return json;
        }
        return null;
    }

}
