package com.shanneng;

import com.shanneng.dao.UserInfoDao;
import com.shanneng.daoImpl.UserInfoDaoImpl;
import com.shanneng.model.UserInfo;
import service.UserInfoService;

import java.util.List;

public class UserInfoServiceImpl implements UserInfoService{

    @Override
    public List findAll_UserInfo(String state) throws Exception {
        UserInfoDao userInfo = new UserInfoDaoImpl();
        List list = userInfo.findAll_UserInfo(state);
        return list;
    }
   //修改申请状态
    @Override
    public int update_UserInfo(Object obj,String name) throws Exception {
        UserInfoDao userInfo = new UserInfoDaoImpl();
        int flag1 = 0;
        flag1 = userInfo.update_UserInfo(obj,name);
        return flag1;
    }

    @Override
    public int insert_UserInfo(Object obj) throws Exception {
        UserInfoDao userInfo = new UserInfoDaoImpl();
        int flag = 0;
        flag = userInfo.insert_UserInfo(obj);

        return flag;
    }

    //插入新的申请数据

}
