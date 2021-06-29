package com.ssh.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.ssh.bean.User;
import com.ssh.service.UserService;
import com.ssh.tools.BaseDao;

@Service
public class UserServiceImpl extends BaseDao<User> implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User login(String username, String password) {
        String sql = "select * from user where username = '" + username
                + "' and password = '" + password + "'";
        List<User> userList = this.list(sql, new Object[]{});
        if (userList.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(new Date());
            sql = "update user set lasttime = '" + time + "' where username = '"
                    + username + "'";
            // 登录成功，更新最终登录时间
            jdbcTemplate.update(sql);
            return userList.get(0);
        }
        return null;
    }

    @Override
    public int updatePassword(String username, String oldPassword, String newPassword) {
        int result = 0;
        try {
            // 验证原密码是否正确
            String sql = "select * from user where username = '" + username + "'";
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class));
            if (oldPassword.equals(user.getPassword())) {
                sql = "update user set password = '" + newPassword
                        + "' where username = '" + username + "'";
                result = jdbcTemplate.update(sql);
            } else {
                result = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public JSONObject getActiveUser() throws Exception {
        JSONObject json = new JSONObject();
        String sql = "select * from user";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        int hyzh = 0, oneHour = 0, oneDay = 0, oneMooth = 0, bhyzh = 0;
        for (User user : users) {
            if (user.getLasttime() != null && user.getLasttime() != "") {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(user.getLasttime());
                long ms = date.getTime();
                double days = ((ms - System.currentTimeMillis()) / (1000 * 3600 * 24 * 1.0));
                if (days < 0.05) {
                    // 小于1小时
                    oneHour++;
                    oneDay++;
                    oneMooth++;
                    hyzh++;
                } else if (days < 1) {
                    // 小于一天为活跃账号
                    oneDay++;
                    oneMooth++;
                    hyzh++;
                }else if(days < 30){
                    // 小于一个月
                    oneMooth++;
                }else{
                    // 大于一个月的为不活跃账号
                    bhyzh++;
                }
            }
        }
        json.put("hyzh",hyzh);
        json.put("oneHour",oneHour);
        json.put("oneDay",oneDay);
        json.put("oneMooth",oneMooth);
        json.put("bhyzh",bhyzh);
        return json;
    }

}


