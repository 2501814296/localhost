package com.ssh.controller;

import com.ssh.bean.Page;
import com.ssh.bean.User;
import com.ssh.bean.YonghujuseVO;
import com.ssh.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 登录
    @RequestMapping("/login")
    @ResponseBody
    public JSONObject login(HttpServletRequest httpServletRequest, String username, String password) {
        JSONObject json = new JSONObject();
        User user = userService.login(username, password);
        if (user == null) {
            // 没值说明登录名或者密码是错误的
            json.put("flag", "0");
        } else {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("LOGIN_USER", user.getUsername());
            session.setAttribute("userid",user.getId());
            session.setAttribute("username",username);
            // 登录成功
            json.put("flag", "1");
            json.put("username",username);
        }
        return json;
    }

    // 获取登录session
    @RequestMapping("/getSission")
    @ResponseBody
    public JSONObject getSission(HttpServletRequest httpServletRequest) {
        JSONObject json = new JSONObject();
        HttpSession sission = httpServletRequest.getSession();
        json.put("username",sission.getAttribute("username"));
        return json;
    }
        // 退出登录
    @RequestMapping("/exitLogin")
    @ResponseBody
    public void exitLogin(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        session.invalidate();
    }

    // 修改密码
    @RequestMapping("/updatePassword")
    @ResponseBody
    public JSONObject updatePassword(HttpServletRequest httpServletRequest, String oldPassword,
                                     String newPassword) {
        HttpSession session = httpServletRequest.getSession();
        String username = session.getAttribute("LOGIN_USER").toString();
        JSONObject json = new JSONObject();
        int result = userService.updatePassword(username, oldPassword, newPassword);
        if (result == 1) {
            json.put("flag", "1");
            session.invalidate();
        } else if (result == -1) {
            json.put("flag", "-1");
        } else {
            json.put("flag", "0");
        }
        return json;
    }

    // 加载用户活跃信息
    @RequestMapping("/getActiveUser")
    @ResponseBody
    public JSONObject getActiveUser() throws Exception {
        return userService.getActiveUser();
    }

    @RequestMapping("/getYZM")
    @ResponseBody
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            /*
             * 一、绘图  
             */
            //step1, 创建一个内存映像(画布)
            BufferedImage image = new BufferedImage(80, 30, BufferedImage.TYPE_INT_RGB);
            //step2,获得画笔
            Graphics g = image.getGraphics();
            //step3,给笔设置颜色
            g.setColor(new Color(255, 255, 255));
            //step4,设置画布的背景颜色
            g.fillRect(0, 0, 80, 30);
            //step5,给笔设置颜色和字体
            Random r = new Random();
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.setFont(new Font(null, Font.BOLD + Font.ITALIC, 25));
            //step6,生成一个随机数
//            String pagecode = getNumber(4);
            String pagecode = request.getParameter("yzm");
            HttpSession session = request.getSession();
            session.setAttribute("checkCode", pagecode);
            //step7,绘制图片
            g.drawString(pagecode, 5, 26);
            //step8,加一些干扰线
            for (int i = 0; i < 8; i++) {
                g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                g.drawLine(r.nextInt(80), r.nextInt(30),
                           r.nextInt(80), r.nextInt(30));
            }

            /*
             * 二、压缩图片并输出 
             */
            //step1,告诉浏览器，返回的是图片
            response.setContentType("image/jpeg");
            //step2,要使用字节流输出
            OutputStream os = response.getOutputStream();
            //step3,压缩图片并输出
            javax.imageio.ImageIO.write(image, "jpeg", os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 生成一个验证码
     */
    private String getNumber(int size) {
        String number = "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            number += chars.charAt(
                    r.nextInt(chars.length()));
        }
        return number;
    }

    // 获取头像路径
    @RequestMapping("/getTouxiang")
    @ResponseBody
    public JSONObject getTouxiang() throws Exception {
        JSONObject json = new JSONObject();
        String sql = "select touxiang from user where id = 1";
        String touxiang = jdbcTemplate.queryForObject(sql, String.class);
        json.put("url", touxiang);
        return json;
    }

    // 获取所有用户
    @RequestMapping("/getAllUser")
    @ResponseBody
    public Page getAllUser(Page page, User user) {
        String sql = " select * from user where 1=1  ";
        if (user.getUsername() != null && user.getUsername() != "") {
            sql += "and  username like '%" + user.getUsername() + "%'";
        }
        String countSql = "select count(*) from ( " + sql + " ) t";
        sql += "order by lasttime desc,id desc ";
        sql += "LIMIT " + (page.getPage() - 1) * page.getLimit() + "," + page.getLimit();
        page.setData(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class)));
        page.setCount(jdbcTemplate.queryForObject(countSql, Integer.class));
        return page;
    }


    // 新增用户
    @RequestMapping("/save")
    @ResponseBody
    public void save(User user) {
        String sql = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        if(user.getId() != 0){
            sql = "delete from user where id = "+user.getId();
            jdbcTemplate.update(sql);
        }
        sql = "insert into user(username,password,lasttime,nicheng) " +
                "values('"+user.getUsername()+"','"+user.getPassword()+"','"+nowDate+"','"+user.getNicheng()+"')";
        jdbcTemplate.update(sql);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public void updateWater(String id) {
        String sql = "delete from user where id = "+id;
        jdbcTemplate.update(sql);
    }

    // 绑定角色
    @RequestMapping("/allot")
    @ResponseBody
    public void allot(YonghujuseVO vo) {
        // 查询是否绑定过角色
        String sql = "";
        sql = "select id from yonghujuese where uid="+vo.getUid();
        int id = 0;
        try{
            id = jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (Exception e){
        }
        if(id == 0){
            // 没有绑定过
            sql = "insert into yonghujuese(uid,jid) " +
                    "values("+vo.getUid()+","+vo.getJid()+")";
        }else{
            // 绑定过
            sql = "update yonghujuese set jid = "+vo.getJid()+" where id = "+id;
        }
        jdbcTemplate.update(sql);
    }


}
