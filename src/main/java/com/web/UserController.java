package com.web;

import com.bean.UserTb;
import com.service.UserTbService;
import com.util.YanZheng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Controller
@SessionAttributes({"u1", "logintime"})
public class UserController {

    @Autowired
    private UserTbService userTbService;


    //修改密码
    @RequestMapping("/user/updateuserpass")
    public void updateuserpass(UserTb userTb, HttpServletResponse response, SessionStatus status) {
        int k = userTbService.updateByPrimaryKeySelective(userTb);
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            if (k > 0) {
                //清除注册的session。
                status.setComplete();
                out.print("<script >alert('修改成功，即将跳转至登录页面');top.location.href='/login.jsp'</script>");
            } else {
                out.print("<script >alert('修改失败，请重试');location.href='password.jsp'</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //检查当前用户的合法性
    @RequestMapping("/user/chackpass")
    public void chachpass(String usedpass, ModelMap map, HttpServletResponse response) {
        UserTb userTb = (UserTb) map.get("u1");
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            if (userTb.getUserPs().equals(usedpass)) {
                out.print(true);
            } else {
                out.print(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //更新
    @RequestMapping("/user/updateuser")
    public void update(UserTb userTb, HttpServletResponse response, ModelMap map) {
        int k = userTbService.updateByPrimaryKeySelective(userTb);
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            if (k > 0) {
                UserTb userTb1 = userTbService.selectByPrimaryKey(userTb.getUserId());
                map.put("u1", userTb1);
                out.write("<script>alert('修改成功');top.location.href='/index.jsp'</script>");
            } else {
                out.write("<script>alert('修改失败');top.location.href='MyUser.jsp'</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //登录
    @RequestMapping("/login")
    public void select(ModelMap map, UserTb userTb, String DropExpiration, HttpServletResponse response, String yanzheng) {
        UserTb user = userTbService.login(userTb);  //存放的有角色信息
        response.setContentType("text/html;charset=utf-8");
        String yuanyanzheng = YanZheng.getCode();
         yanzheng = YanZheng.getCode();
        try {
            PrintWriter p = response.getWriter();
            if (yuanyanzheng.equals(yanzheng)) {
                if (user == null) {
                    p.print("<script>alert('用户名或密码不正确');location.href='login.jsp'</script>");
                } else {
                    map.put("u1", user); //Session传值
                    System.out.println(user.getUserName());
                    Cookie cookie = new Cookie("uname", "DropExpiration");
                    if ("DropExpiration".equals("Day")) {
                        cookie.setMaxAge(24 * 60 * 60);
                    } else if ("DropExpiration".equals("Month")) {
                        cookie.setMaxAge(24 * 60 * 60 * 30);
                    } else if ("DropExpiration".equals("Year")) {
                        cookie.setMaxAge(24 * 60 * 60 * 30 * 365);
                    }
                    response.addCookie(cookie);
                    //登录时间
                    Date date = new Date();
                    map.put("logintime", date);

                    //修改登录次数
                    p.print("<script>alert('登录成功');location.href='index.jsp'</script>");
                }

            } else {
                p.print("<script>alert('验证码错误');location.href='login.jsp'</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
