package com.ssh.dao.impl;

import com.ssh.bean.Guanwang;
import com.ssh.bean.Page;
import com.ssh.dao.GuanWangDao;
import com.ssh.tools.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GuanWangDaoImpl extends BaseDao<Guanwang> implements GuanWangDao {

    @Override
    public Guanwang getWaterById(int id) {
        String sql="select * from guanwang where id= ?";
        return this.load(sql,new Object[]{id});
    }

    @Override
    public Page getSomeWater(Guanwang water, Page page) {
        String sql="select * from guanwang where 1=1";
        String count="select count(*) from guanwang where 1=1";
        List<Object> list=new ArrayList<>();
        if(water.getId() == 1){
            // 经纬度列表
            sql+=" and name is null ";
            count+=" and name is null ";
        }else{
            // 管网列表
            sql+=" and name is not null ";
            count+=" and name is not null ";
        }
        if(null!=water.getName()&&!"".equals(water.getName())){
            sql+=" and guanwangname like ? ";
            count+=" and guanwangname like ? ";
            list.add("%"+water.getName()+"%");
        }
        if(water.getMaxyali() != 0 && water.getLiuliang() != 0){
            sql+=" and maxyali > ? or liuliang > ? ";
            count+=" and maxyali > ? or and liuliang > ?";
            list.add(water.getMaxyali());
            list.add(water.getLiuliang());
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
    public int updateWater(Guanwang water) {
        return this.update(water,"guanwang");
    }

    @Override
    public int saveWater(Guanwang water) {
        return this.save(water,"guanwang");
    }

    @Override
    public int deleteWater(String ids) {
        return this.delete(ids,"guanwang");
    }

    // 统计 对官网的产水率、耗电量和供水量的统计
//    @Override
//    public List<ShuiChangTongJi> getListBySid(int sid, String type) {
//        String sql = "select t.date,sUM(t.chanshui) chanshui,SUM(t.haodian) haodian,SUM(t.gongshui) gongshui  from ( SELECT DATE_FORMAT";
//        if (type.equals("day")) {
//            sql += "(DATE,'%Y-%m-%d') ";
//        } else if (type.equals("week")) {
//            sql += "(DATE,'%Y-%u')";
//        } else if (type.equals("month")) {
//            sql += "(DATE,'%Y-%m')";
//        } else if (type.equals("year")) {
//            sql += "(DATE,'%Y')";
//        }
//        sql +=
//                "DATE,chanshui,haodian,gongshui \n"
//                        + "FROM yuanshuiditongji WHERE yid= ? )t GROUP BY t.date ASC limit 0,7";
//        final String sqlF = sql;
//        final List<ShuiChangTongJi> tongji = new ArrayList<>();
//        class a extends BaseDao<ShuiChangTongJi>{
//            public List<ShuiChangTongJi> b(){
//                return this.list(sqlF,new Object[]{sid});
//            }
//        }
//        return new a().b();
//    }
}
