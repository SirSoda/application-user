package com.shanneng.dao;

import java.util.List;

public interface UserInfoDao {
    //条件查询  部门领导待签字 state =0     办公室意见待签字 state = 1  申请完成 state =2
    public List findAll_UserInfo(String state) throws  Exception;
    //添加新的用车申请
    public int insert_UserInfo(Object obj) throws  Exception;
    //更新申请表中 用车状态 改变在部门领导  办公室意见显示页面的数据内容
    public int update_UserInfo(Object obj,String name) throws  Exception;
}
