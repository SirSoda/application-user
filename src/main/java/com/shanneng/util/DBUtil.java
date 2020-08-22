package com.shanneng.util;

import java.sql.*;


public class DBUtil {
    private static String className = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://10.0.17.201:3306/dingding";
    private static String username = "root";
    private static String password ="my-secret-pw";
    private static Connection conn = null;
    private static PreparedStatement ps= null;
    private static ThreadLocal threadLocal = new ThreadLocal();
    static {
        try{
            Class.forName(className);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    public  static Connection getConn(){
      try{
          conn = (Connection) threadLocal.get();
          if(conn==null){
              conn = DriverManager.getConnection(url,username,password);
              threadLocal.set(conn);
          }
      }catch(SQLException e){
          e.printStackTrace();
      }
      return  conn;
    }


    public static PreparedStatement createStatement(String sql){
        getConn();
        try{
            ps = conn.prepareStatement(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ps;
    }

    public static void close(ResultSet rs){
        try{
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(conn!=null){
                conn.close();
                threadLocal.remove();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void close(PreparedStatement ps,Connection conn){
        close(null,ps,conn);
    }
    public static void close(ResultSet rs,PreparedStatement ps,Connection con){
        if(rs!=null){
            try{
                rs.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            threadLocal.remove();
        }
    }
}
