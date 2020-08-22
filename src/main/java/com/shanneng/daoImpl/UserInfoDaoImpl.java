package com.shanneng.daoImpl;

import com.shanneng.dao.UserInfoDao;
import com.shanneng.model.UserInfo;
import com.shanneng.util.sqlSession;

import java.util.List;

public class UserInfoDaoImpl implements UserInfoDao{
    @Override
    public List findAll_UserInfo(String state) throws Exception{
        String sql = "select * from usecarapply where userInfo_state = "+state;
        return sqlSession.selectList(sql,UserInfo.class);
    }

    @Override
    public int insert_UserInfo(Object obj)throws Exception {
     //   String sql = "insert into usecarapply values(?,?,?,?,?,?,?,?,?)";
       // int i = sqlSession.insert();
        int i =0 ;
        i= sqlSession.insert(obj);
        return i;
    }

    @Override
    public int update_UserInfo(Object obj1,String name) throws Exception {
        int m =0;
        m = sqlSession.update(obj1,name);
        return m;
    }
}
