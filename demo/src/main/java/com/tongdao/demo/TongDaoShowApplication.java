package com.tongdao.demo;

import com.baidu.frontia.FrontiaApplication;
import com.tongdao.newsdk.TongDao;

public class TongDaoShowApplication extends FrontiaApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TongDao.registerApplication(this);
    }

}
