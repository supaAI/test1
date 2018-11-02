package com.web;

import com.bean.Menu;
import com.github.pagehelper.PageInfo;
import com.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MenuController {
    @Autowired
    private MenuService menuService;

    //批量删除  all1  all2  1+2
    @RequestMapping("/power/menu/deletemenus")
    public void deletemenus(String[] menuid,HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            int k = menuService.deletesecondmenu(menuid);
            if (k>0){
                out.print("<script>alert('菜单删除成功');location.href='/power/menu/getmenu'</script>");
            }else {
                out.print("<script>alert('菜单删除失败');location.href='/power/menu/getmenu'</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除菜单 单一
    @RequestMapping("/power/menu/deletemenu")
    public void deletemenu(int menuid, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            int k = menuService.deleteByPrimaryKey(menuid);
            if (k == 0) {
                out.print("<script>alert('删除失败，请先删除该菜单下的操作');location.href='/power/menu/getmenu'</script>");
            } else {
                out.print("<script>alert('菜单删除成功');location.href='/power/menu/getmenu'</script>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //新增菜单 第一步
    @RequestMapping("/power/menu/addmenuf")
    public String addmenuf(ModelMap map) {
        ////查询一级目录
        List<Menu> menuList = menuService.findfristmenu(-1);
        map.put("fmenu", menuList);
        return "/power/menu/add";
    }

    //新增菜单 第二步
    @RequestMapping("/power/menu/addmenua")
    public String addmenua(Menu menu) {
        menuService.insert(menu);
        return "redirect:/power/menu/getmenu";
    }


    //查询某一菜单的信息
    @RequestMapping("/power/menu/findmenubymid")
    public String findmenubymid(int mid, ModelMap map) {
        Menu menu = menuService.selectByPrimaryKey(mid);
        map.put("menuo", menu);
        return "/power/menu/info";
    }


    //查询全部的操作
    @RequestMapping("/power/menu/getmenu")
    public String getmenu(@RequestParam(defaultValue = "1") int index,
                          @RequestParam(defaultValue = "5") int size,
                          ModelMap map) {
        PageInfo mpage = menuService.findall2(index, size);
        map.put("mpage", mpage);
        return "/power/menu/list";
    }


}
