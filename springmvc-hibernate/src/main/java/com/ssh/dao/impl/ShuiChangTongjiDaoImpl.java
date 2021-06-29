package com.ssh.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssh.bean.ShuiChangTongJi;
import com.ssh.dao.ShuiChangTongjiDao;
import com.ssh.tools.BaseDao;
@Repository
public class ShuiChangTongjiDaoImpl extends BaseDao<ShuiChangTongJi> implements ShuiChangTongjiDao {
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
                + "FROM shuichangtongji WHERE sid= ? )t GROUP BY t.date ASC limit 0,7";
        return this.list(sql,new Object[]{sid});
    }
}
