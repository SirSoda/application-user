package com.shanneng.util;

//import com.sun.org.apache.bcel.internal.util.ClassPath;
//import javax.servlet.ServletContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Character.getName;
//import java.sql.ResultSet;

public class sqlSession {
    public static List selectList(String sql,Class classFile) throws Exception{
        PreparedStatement ps = DBUtil.createStatement(sql);
        ResultSet rs = null;
        Field fieldArray[] = null;
        List list = new ArrayList();
        try {
            rs = ps.executeQuery();
            fieldArray = classFile.getDeclaredFields();
            while(rs.next()){
                Object obj = classFile.newInstance();
                for (int i =0;i<fieldArray.length;i++){
                    Field fieldObj = fieldArray[i];
                    String fieldName = fieldObj.getName();
                    String value = rs.getString(fieldName);
                    init(obj,value,fieldObj);
                }
                list.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
        }
        return list;
    }
    public static List selectList(String sql,Class classFile,HttpServletRequest request) throws Exception{
        PreparedStatement ps = null;
        Connection con = null;
        ResultSet rs = null;
        Field fieldArray[] = null;
        List list =new ArrayList();
        ServletContext application = request.getServletContext();
        Map conPool = null;
        try {
            conPool = (Map) application.getAttribute("key");
            Iterator it = conPool.keySet().iterator();
            while(it.hasNext()){
                con = (Connection)it.next();
                boolean flag = (boolean)conPool.get(con);
                if(flag==true){
                    conPool.put(con,false);
                    break;
                }else{
                    con = null;
                }
            }
            if(con == null){
            //新建一个connection
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            fieldArray = classFile.getDeclaredFields();
            while(rs.next()){
                    Object obj = classFile.newInstance();
                    for(int i=0;i<fieldArray.length;i++){
                        Field fieldObj = fieldArray[i];
                        String fieldName = fieldObj.getName();
                        String value =rs.getString(fieldName);
                        init(obj,value,fieldObj);
                    }
                    list.add(obj);
            }
        } finally {
                     conPool.put(con,true);
        }
        return list;
    }
    private static  void init(Object obj ,String value,Field fieldObj)throws Exception{
        fieldObj.setAccessible(true);
        if(value == null ){
            fieldObj.set(obj,value);
        }else{
           String typeName = fieldObj.getType().getName();
           if("java.lang.Integer".equals(typeName)){
               fieldObj.set(obj,Integer.valueOf(value));
           }else if("java.lang.Double".equals(typeName)){
               fieldObj.set(obj,Double.valueOf(value));
           }else if("java.lang.String".equals(typeName)){
               fieldObj.set(obj,value);
           }
        }
    }
    /**
     * 根据实体类对象生成一条insert
     * 将insert推送到数据库中执行，返回操作行数
     */
    public static int insert(Object obj)throws Exception{
        String tableName = null;
        StringBuffer columnStr = new StringBuffer("(");
        StringBuffer valueStr = new StringBuffer("value(");
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        Field fieldArray[] = null;
        PreparedStatement ps =null;
        int flag = 0;
        //根据实体类获得操作的表名
        tableName = getName(obj.getClass());
        //获取需要赋值的字符串序列
        try{
            fieldArray = obj.getClass().getDeclaredFields();
            for(int i=0;i<fieldArray.length;i++){
                Field fieldObj = fieldArray[i];
                fieldObj.setAccessible(true);
                Object value = fieldObj.getType().getName();
                String typeName = fieldObj.getType().getName();
                if("java.lang.String".equals(typeName)){
                    if(value!=null&&"".equals(value)){
                        if(columnStr.length()==1){
                            columnStr.append(fieldObj.getName());
                            valueStr.append("'");
                            valueStr.append(value);
                            valueStr.append("'");
                        }else{
                            columnStr.append(",");
                            columnStr.append(fieldObj.getName());
                            valueStr.append(",'");
                            valueStr.append(value);
                        }
                    }
                }else if("java.lang.Integer".equals(typeName)||"java.lang.Double".equals(typeName)){
                    if(value!=null&&((Integer)value)!=0){
                        if(columnStr.length()==1){
                            columnStr.append(fieldObj.getName());
                            valueStr.append(value);
                        }else{
                            columnStr.append(",");
                            columnStr.append(fieldObj.getName());
                            valueStr.append(",");
                            valueStr.append(value);
                        }
                    }
                }
            }
            columnStr.append(")");
            valueStr.append(")");

            sql.append(tableName);
            sql.append(columnStr);
            sql.append(valueStr);
            System.out.print("sql"+sql.toString());
            ps = DBUtil.createStatement(sql.toString());
            flag = ps.executeUpdate();
        }finally {
            DBUtil.close(null);
        }
        return flag;

    }
    private static  String getName(Class classFile){
        String classPath = classFile.getName();
        int index = classPath.lastIndexOf(".");
        String typeName = classPath.substring(index+1);
        return typeName;
    }
    public static  int update(Object obj,String primaryKey)throws Exception{
       String tableName = null;
       StringBuffer setBuffer = new StringBuffer(" set ");
       StringBuffer whereBuffer = new StringBuffer(" where ");
       StringBuffer sql = new StringBuffer("update ");
       Field fieldArray[] = null;
       int flag =0;
       PreparedStatement ps = null;
       //1.获得操作表名
        tableName = getName(obj.getClass());
       //2.设置需要更新字段信息
        try{
            fieldArray = obj.getClass().getDeclaredFields();
            for(int i=0;i<fieldArray.length;i++){
                Field fieldObj = fieldArray[i];
                fieldObj.setAccessible(true);
                String fieldName = fieldObj.getName();
                String typeName = fieldObj.getType().getName();
                Object value = fieldObj.get(obj);
                if(value==null){
                    continue;
                }
                if(fieldName.equals(primaryKey)){
                    whereBuffer.append(fieldName);
                    whereBuffer.append(" = ");
                    if("java.lang.String".equals(typeName)||"java.util.Date".equals(typeName)){
                        whereBuffer.append("'");
                        whereBuffer.append(value);
                        whereBuffer.append("'");
                    }else{
                        whereBuffer.append(value);
                    }
                }else{
                    if(setBuffer.length()>5){
                        setBuffer.append(",");
                    }
                    setBuffer.append(fieldName);
                    setBuffer.append(" = ");
                    if("java.lang.String".equals(typeName)||"java.util.Date".equals(typeName)){
                        setBuffer.append("'");
                        setBuffer.append(value);
                        setBuffer.append("'");
                    }else{
                        setBuffer.append(value);
                    }
                }
            }
            sql.append(tableName);
            sql.append(setBuffer);
            sql.append(whereBuffer);
            System.out.println("sql:" + sql);
            ps = DBUtil.createStatement(sql.toString());
            flag = ps.executeUpdate();
        }finally {

        }
        return  flag;
    }


    public static int delete(String sql)throws Exception{
        PreparedStatement ps  = DBUtil.createStatement(sql);
        int flag =0;
        try {
            flag = ps.executeUpdate();
        } finally {
            DBUtil.close(null);
        }
        return flag;
    }
}
