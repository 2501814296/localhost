package com.ssh.controller;

import com.ssh.bean.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @RequestMapping("/getMenu")
    public Menu getMenu(Menu menu, HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            int id = Integer.parseInt(session.getAttribute("userid").toString());

            String sql = "SELECT mm.* FROM USER t,yonghujuese tt,caidanxianshi cc,menuinfo mm WHERE t.id=" + id + " AND t.id=tt.uid AND cc.jid=tt.jid AND  cc.mid=mm.id order by id asc";
            String sql2 =
                    " SELECT * FROM child ch WHERE ch.mid = ? ";
            List<MenuInfo> menuInfo = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MenuInfo.class));
            for (MenuInfo i : menuInfo) {
                List<Child> children = jdbcTemplate.query(sql2, new Object[]{i.getId()}, new BeanPropertyRowMapper<>(Child.class));
                i.setChild(children);
            }
            menu.setHomeInfo(new HomeInfo());
            menu.setLogoInfo(new LogoInfo());
            menu.setMenuInfo(menuInfo);
        }catch (Exception e){
            System.out.println("---------------/menu/getMenu接口存在问题，详情如下--------------------");
            e.printStackTrace();
        }
        return menu;
    }

    @ResponseBody
    @RequestMapping("/getQuanxian")
    Page getQuanxian(Page page) {
        String sql = "select * from menuinfo where 1=1 ";
        String countSql = "select count(*) from menuinfo ";
        sql += "order by id asc ";
        sql += "LIMIT " + (page.getPage() - 1) * page.getLimit() + "," + page.getLimit();
        page.setData(jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MenuInfo.class)));
        page.setCount(jdbcTemplate.queryForObject(countSql, Integer.class));
        return page;
    }

    @ResponseBody
    @RequestMapping("/insertJuseMenu")
    public JSONObject insertJuseMenu(@RequestParam(value = "array[]") String[] array, String jueseId) {
        JSONObject json = new JSONObject();
        // 根据jueseId删除掉之前绑定的权限
        String deleteSql = "delete from caidanxianshi where jid = ?";
        jdbcTemplate.update(deleteSql,new Object[]{jueseId});
        // 根据array中的权限添加数据
        List<String> list = Arrays.asList(array);
        List<String> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder("insert into caidanxianshi(jid,mid) values");
        for(int i=0;i<list.size();i++){
            sb.append("(?,?),");
            params.add(jueseId);
            params.add(list.get(i));
        }
        String insertSql = sb.substring(0,sb.length()-1);
        int result = jdbcTemplate.update(insertSql,params.toArray());
        if(result != 0){
            json.put("flag","1");
        }else{
            json.put("flag","0");
        }
        return json;
    }

    @ResponseBody
    @RequestMapping("/selectJuseMenu")
    public JSONObject selectJuseMenu(String jueseId) {
        JSONObject json = new JSONObject();
        String sql = "select t.mid from caidanxianshi t where t.jid = ?";
        List<String> mids = jdbcTemplate.queryForList(sql,String.class,jueseId);
        json.put("flag","1");
        json.put("mids",mids);
        return json;
    }

 }
