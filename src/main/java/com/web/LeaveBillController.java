package com.web;

import com.bean.LeaveBill;
import com.bean.UserTb;
import com.service.LeaveBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class LeaveBillController {

    @Autowired
    private LeaveBillService billService;


    //添加请假请求
    @RequestMapping("/qingjia/insert")
    public String insert(LeaveBill leaveBill){
        billService.insert(leaveBill);
        return "redirect:/qingjia/getall";
    }

    //删除请假请求
    @RequestMapping("/qingjia/delete")
    public void delete(HttpServletResponse response, int leaveid) {
        response.setContentType("text/html;charset=utf-8");
        try {
            billService.delete(leaveid);
            response.getWriter().write("<script>alert('删除成功');location.href='/qingjia/getall'</script>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查看全部请假信息
    @RequestMapping("/qingjia/getall")
    public String getall(HttpSession session, ModelMap map) {
        UserTb u = (UserTb) session.getAttribute("u1");
        List<LeaveBill> leaveBills = billService.getall(u.getUserId());
        map.put("leavelist", leaveBills);
        return "/qingjia/list";
    }
}
