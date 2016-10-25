package com.tongdao.demo;

import com.baidu.frontia.FrontiaApplication;
import com.tongdao.sdk.TongDaoOO;

public class TongDaoShowApplication extends FrontiaApplication {

    private TongDaoOO tongDao;

    @Override
    public void onCreate() {
        super.onCreate();
        if (tongDao == null){
            tongDao = TongDaoOO.getInstance(this,DataTool.APP_KEY);
        }
    }

    synchronized public TongDaoOO getTongDao() {
        if (tongDao == null){
            tongDao = TongDaoOO.getInstance(this,DataTool.APP_KEY);
        }
        return tongDao;
    }
}
