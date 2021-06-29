package com.ssh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssh.bean.Page;
import com.ssh.bean.ShuiZhi;
import com.ssh.dao.ShuiZhiDao;
import com.ssh.service.ShuiZhiService;
// 水质管理
@Service
public class ShuiZhiServiceImpl implements ShuiZhiService {
    @Autowired
    private ShuiZhiDao shuiZhiDao;

    @Override
    public List<ShuiZhi> getShuiZhiList(int sid, String type) {
        return shuiZhiDao.getShuiZhiList(sid, type);
    }

    @Override
    public Page getSomeList(ShuiZhi shuiZhi, Page page) {
        return shuiZhiDao.getSomeList(shuiZhi,page);
    }

    @Override
    public int saveShuiZhi(ShuiZhi shuiZhi) {
        return shuiZhiDao.saveShuiZhi(shuiZhi);
    }

    @Override
    public int deleteShuiZhi(String ids) {
        return shuiZhiDao.deleteShuiZhi(ids);
    }

    @Override
    public int updateShuiZhi(ShuiZhi shuiZhi) {
        return shuiZhiDao.updateShuiZhi(shuiZhi);
    }
}
