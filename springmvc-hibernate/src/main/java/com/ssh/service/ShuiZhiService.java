package com.ssh.service;

import java.util.List;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiZhi;
// 水质管理
public interface ShuiZhiService {

    public List<ShuiZhi> getShuiZhiList(int sid, String type);
    // 表格获取
    public Page getSomeList(ShuiZhi shuiZhi,Page page);
    // 保存
    public int saveShuiZhi(ShuiZhi shuiZhi);
    // 删除
    public int deleteShuiZhi(String ids);
     //修改
    public int updateShuiZhi(ShuiZhi shuiZhi);
}
