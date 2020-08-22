package service;

import java.util.List;

public interface UserInfoService {
    //查找符合条件的用车申请信息
    public List findAll_UserInfo(String state) throws Exception;
    //修改用车申请状态  部门领导待签字 0   办公室意见 1  申请通过2
    public int  update_UserInfo(Object obj,String name) throws Exception;
    //添加新的用车申请
    public int  insert_UserInfo(Object obj)throws Exception;
}
