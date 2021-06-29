package com.ssh.controller;

import com.ssh.bean.JueSe;
import com.ssh.bean.Page;
import com.ssh.bean.YonghujuseVO;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/juse")
public class JueSeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @ResponseBody
    @RequestMapping("/getJuSeList")
    public Page getJuSeList(Page page, JueSe jueSe) {
        String sql = " select * from juese where 1=1  ";
        if (jueSe.getName() != null && jueSe.getName() != "") {
            sql += "and  name like '%" + jueSe.getName() + "%'";
        }
        String countSql = "select count(*) from ( " + sql + " ) t";
        sql += "order by id desc ";
        sql += "LIMIT " + (page.getPage() - 1) * page.getLimit() + "," + page.getLimit();
        page.setData(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(JueSe.class)));
        page.setCount(jdbcTemplate.queryForObject(countSql, Integer.class));
        return page;
    };

    @ResponseBody
    @RequestMapping("/deleteJuSe")
    public int deleteJuSe(String ids) {
        String[] id = ids.split(",");
        String para = "";
        for (String i : id) {
            para += "'" + i + "',";
        }
        String sql2=" select * from yonghujuese where jid in ";
        String sql = " delete from juese where id in ";
        sql += " ( " + para.substring(0, para.length() - 1) + ")";
        sql2+=" ( " + para.substring(0, para.length() - 1) + ")";
       List<YonghujuseVO> yonghujuseVOList =jdbcTemplate.query(sql2, new BeanPropertyRowMapper<>(YonghujuseVO.class));
       if(yonghujuseVOList.size()>0){
           return 0;
       }
       else {
           return jdbcTemplate.update(sql);
       }
    }

    @ResponseBody
    @RequestMapping("/updateJuSe")
    public int updateJuSe(JueSe jueSe) {
        String sql="";
        if(0!=jueSe.getId()&&!"".equals(jueSe.getId())){
            sql = "update juese set name=?,time=? where id= ? ";
            return jdbcTemplate.update(sql, new Object[] { jueSe.getName(),jueSe.getTime(),jueSe.getId() });
        }
        else{
            sql = "insert  into juese(name,time)  values(?,?)";
            return jdbcTemplate.update(sql,new Object[]{jueSe.getName(),new Date()});
        }
    }

    @ResponseBody
    @RequestMapping("/getAllList")
    public JSONObject getAllList(String id) {
        JSONObject json = new JSONObject();
        String sql = " select * from juese ";
        List<JueSe> jueSeList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(JueSe.class));
        json.put("data",jueSeList);
        return json;
    }

    // 根据账号id获取角色，判断是否绑定
    @ResponseBody
    @RequestMapping("/getJueseByid")
    public JSONObject getJueseByid(int uid) {
        JSONObject json = new JSONObject();
        String sql = " select jid from yonghujuese where uid="+uid;
        int jid = jdbcTemplate.queryForObject(sql,Integer.class);
        json.put("jid",jid);
        return json;
    }
}
