package com.ssh.dao.impl;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiZhi;
import com.ssh.dao.ShuiZhiDao;
import com.ssh.tools.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ShuiZhiDaoImpl extends BaseDao<ShuiZhi> implements ShuiZhiDao {
    @Override
    public List<ShuiZhi> getShuiZhiList(int sid, String type) {
        String sql = "SELECT t.date,AVG(t.sedu) sedu,AVG(t.ph) ph,AVG(t.haoyang) haoyang,AVG(t.hunzhuo) hunzhuo,AVG(t.youli) youli  FROM ( SELECT DATE_FORMAT";
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
                "DATE,sedu,ph,haoyang,hunzhuo,youli\n"
                + "FROM shuizhi WHERE sid= ? )t GROUP BY t.date ASC limit 0,7";
        return this.list(sql,new Object[]{sid});
    }

    @Override
    public Page getSomeList(ShuiZhi shuiZhi,Page page) {
        String sql="select * from shuizhi where 1=1 ";
        String count="select count(*) from shuizhi where 1=1 ";
        List<Object> list=new ArrayList<>();
        if(shuiZhi.getId() == 2){
            sql+=" and yuanshuidiname is null ";
            count+=" and yuanshuidiname is null ";
        }else if(shuiZhi.getId() == 1){
            sql+=" and sname is null ";
            count+=" and sname is null ";
        }
        if(null!=shuiZhi.getSname()&&!"".equals(shuiZhi.getSname())){
            sql+=" and sname like ? ";
            count+=" and sname like ? ";
            list.add("%"+shuiZhi.getSname()+"%");
        }
        if(null!=shuiZhi.getYuanshuidiname()&&!"".equals(shuiZhi.getYuanshuidiname())){
            sql+=" and yuanshuidiname like ? ";
            count+=" and yuanshuidiname like ? ";
            list.add("%"+shuiZhi.getYuanshuidiname()+"%");
        }
        if(shuiZhi.getSid() == 89){
            // 报警的列表查询出超标的信息
            sql+=" and (hunzhuo > 10 or sedu > 10 or youli > 10 or junluo > 10) ";
            count+=" and (hunzhuo > 10 or sedu > 10 or youli > 10 or junluo > 10) ";
        }
        page.setCount(this.list(count,list.toArray()).size());
        if(0!=page.getPage()){
            sql+="order by date desc limit ?,? ";
            list.add((page.getPage()-1)* page.getLimit());
            list.add(page.getLimit());
        }
        page.setData(this.list(sql,list.toArray()));
        return page;
    }

    @Override
    public int saveShuiZhi(ShuiZhi shuiZhi) {
        return this.save(shuiZhi,"shuizhi");
    }

    @Override
    public int deleteShuiZhi(String ids) {
        return this.delete(ids,"shuizhi");
    }

    @Override
    public int updateShuiZhi(ShuiZhi shuiZhi) {
        return this.update(shuiZhi,"shuizhi");
    }
}
