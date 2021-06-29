package com.ssh.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssh.bean.Page;
import com.ssh.bean.Water;
import com.ssh.dao.WaterDao;
import com.ssh.tools.BaseDao;

@Repository
public class WaterDaoImpl extends BaseDao<Water> implements WaterDao {

    @Override
    public Water getWaterById(int id) {
        String sql="select * from shuichang where id= ?";
        return this.load(sql,new Object[]{id});
    }

    @Override
    public Page getSomeWater(Water water, Page page) {
        String sql="select * from shuichang ";
        String count="select count(*) from shuichang ";
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
    public int updateWater(Water water) {
        return this.update(water,"shuichang");
    }

    @Override
    public int saveWater(Water water) {
        return this.save(water,"shuichang");
    }

    @Override
    public int deleteWater(String ids) {
        return this.delete(ids,"shuichang");
    }
}
