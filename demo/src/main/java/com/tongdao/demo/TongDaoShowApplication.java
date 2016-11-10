package com.tongdao.demo;

import com.baidu.frontia.FrontiaApplication;
import com.tongdao.sdk.TongDao;

public class TongDaoShowApplication extends FrontiaApplication {

    private TongDao tongDao;

    @Override
    public void onCreate() {
        super.onCreate();
        if (tongDao == null){
            tongDao = TongDao.getInstance(this,DataTool.APP_KEY);
        }
    }

    synchronized public TongDao getTongDao() {
        if (tongDao == null){
            tongDao = TongDao.getInstance(this,DataTool.APP_KEY);
        }
        return tongDao;
    }
}
