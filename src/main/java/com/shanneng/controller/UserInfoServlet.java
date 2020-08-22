package com.shanneng.controller;

import com.shanneng.UserInfoServiceImpl;
import com.shanneng.model.UserInfo;
import service.UserInfoService;
//import sun.management.Agent;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.util.List;

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         //1.调用Service层完成待审批查询
        //2。将查询结果保存在request中
        //3.向Tomcat调用jsp
//        UserInfoService service = new UserInfoServiceImpl();
//      //  InvocationHandler agetManager = new Agent(service);
////        UserInfoService serviceProxy = (UserInfoService)((Agent)agetManager).getAgent();
//        List UserInfoList = null;
//        try {
//           // UserInfoList = service.findAll_UserInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        request.setAttribute("key",UserInfoList);
//        request.getRequestDispatcher("/index.jsp").forward(request,response);
        //1.获取前台页面传递的参数
        //2.将前台页面的参数插入到数据库中
        //3.调用钉钉接口给部门领导发送消息通知
        String username = request.getParameter("username");


        /** 处理ajax请求 */
        System.out.println("我被请求了");
//        request.setAttribute("key", "value");
//        response.setContentType("text/html;charset=utf-8");
//        response.setHeader("cache-control", "no-cache");
//        String str = "{'msg':'成功','success':'true'}";
//
//        // 先将对账转成json对象
//        // 将json对象 转成jsonstring
//
//        PrintWriter out = response.getWriter();
//        out.print(str);
//        out.flush();
//        out.close();
//        UserInfoService userinfo = new UserInfoServiceImpl();

        /** 返回页面 */
        request.setAttribute("username", username);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
