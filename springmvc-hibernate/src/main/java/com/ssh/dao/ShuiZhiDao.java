package com.ssh.dao;

import java.util.List;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiZhi;

public interface ShuiZhiDao {

    public List<ShuiZhi> getShuiZhiList(int sid, String type);

    public Page getSomeList(ShuiZhi shuiZhi,Page page);

    public int saveShuiZhi(ShuiZhi shuiZhi);

    public int deleteShuiZhi(String ids);

    public int updateShuiZhi(ShuiZhi shuiZhi);
}
