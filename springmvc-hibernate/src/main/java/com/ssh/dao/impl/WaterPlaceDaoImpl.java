package com.ssh.dao.impl;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiChangTongJi;
import com.ssh.bean.WaterPlace;
import com.ssh.dao.WaterPlaceDao;
import com.ssh.tools.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WaterPlaceDaoImpl extends BaseDao<WaterPlace> implements WaterPlaceDao {

    @Override
    public WaterPlace getWaterById(int id) {
        String sql="select * from yuanshuidi where id= ?";
        return this.load(sql,new Object[]{id});
    }

    @Override
    public Page getSomeWater(WaterPlace water, Page page) {
        String sql="select * from yuanshuidi ";
        String count="select count(*) from yuanshuidi ";
        List<Object> list=new ArrayList<>();
        if(null!=water.getName()&&!"".equals(water.getName())){
            sql+=" where name like ? ";
            count+=" where name like ? ";
            list.add("%"+water.getName()+"%");
        }
        page.setCount(this.list(count,list.toArray()).size());
        if(0!=page.getPage()){
        sql+="order by id desc limit ?,? ";
        list.add((page.getPage()-1)* page.getLimit());
        list.add(page.getLimit());
        }
        page.setData(this.list(sql,list.toArray()));
        return page;
    }

    @Override
    public int updateWater(WaterPlace water) {
        return this.update(water,"yuanshuidi");
    }

    @Override
    public int saveWater(WaterPlace water) {
        return this.save(water,"yuanshuidi");
    }

    @Override
    public int deleteWater(String ids) {
        return this.delete(ids,"yuanshuidi");
    }

    // 水厂统计 对水厂的产水率、耗电量和供水量的统计
    @Override
    public List<ShuiChangTongJi> getListBySid(int sid, String type) {
        String sql = "select t.date,sUM(t.chanshui) chanshui,SUM(t.haodian) haodian,SUM(t.gongshui) gongshui  from ( SELECT DATE_FORMAT";
        if (type.equals("day")) {
            sql += "(DATE,'%Y-%m-%d') ";
        } else if (type.equals("week")) {
            sql += "(DATE,'%Y-%u')";
        } else if (type.equals("month")) {
            sql += "(DATE,'%Y-%m')";
        } else if (type.equals("year")) {
            sql += "(DATE,'%Y')";
        }
        sql +=
                "DATE,chanshui,haodian,gongshui \n"
                        + "FROM yuanshuiditongji WHERE yid= ? )t GROUP BY t.date ASC limit 0,7";
        final String sqlF = sql;
        final List<ShuiChangTongJi> tongji = new ArrayList<>();
        class a extends BaseDao<ShuiChangTongJi>{
            public List<ShuiChangTongJi> b(){
                return this.list(sqlF,new Object[]{sid});
            }
        }
        return new a().b();
    }
}
