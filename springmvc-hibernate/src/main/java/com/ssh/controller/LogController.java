package com.ssh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssh.bean.Log;
import com.ssh.bean.Page;

@Controller
@RequestMapping("/log")
public class LogController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @RequestMapping("/getLogList")
    public Page getLogList(Page page, Log log) {
        String sql = " select * from log where 1=1  ";
        if (log.getUsername() != null && log.getUsername() != "") {
            sql += "and  username like '%" + log.getUsername() + "%'";
        }
        String countSql = "select count(*) from ( " + sql + " ) t";
        sql += "order by time desc,id desc ";
        sql += "LIMIT " + (page.getPage() - 1) * page.getLimit() + "," + page.getLimit();
        page.setData(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Log.class)));
        page.setCount(jdbcTemplate.queryForObject(countSql, Integer.class));
        return page;
    }

    @ResponseBody
    @RequestMapping("/deleteLog")
    public int deleteLog(String ids) {
        String[] id = ids.split(",");
        String para = "";
        for (String i : id) {
            para += "'" + i + "',";
        }
        String sql = " delete from log where id in ";
        sql += " ( " + para.substring(0, para.length() - 1) + ")";
        return jdbcTemplate.update(sql);
    }

    @ResponseBody
    @RequestMapping("/updateLog")
    public int updateLog(Log log) {
        String sql="";
        if(0!=log.getId()&&!"".equals(log.getId())){
            sql = "update log set username=?,action=?,time=? where id= ? ";
            return jdbcTemplate.update(sql, new Object[] { log.getUsername(), log.getAction(), log.getTime(),log.getId() });
        }
       else{
            sql = "insert  into log(username,action,time)  values(?,?,?)";
            return jdbcTemplate.update(sql,new Object[]{log.getUsername(),log.getAction(),log.getTime()});
        }
    }

    public int saveLog(Log log){
        String sql = "insert  into log(username,action,time)  values(?,?,?)";
        return jdbcTemplate.update(sql,new Object[]{log.getUsername(),log.getAction(),log.getTime()});
    }
}
