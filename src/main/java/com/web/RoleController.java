package com.web;

import com.bean.Role;
import com.github.pagehelper.PageInfo;
import com.service.MenuService;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;



    //删除角色
    @RequestMapping("/power/role/deleterole")
    public void deleterole(int roleid, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter  out = response.getWriter();
            int k = roleService.deleterole(roleid);
            if(k==0){
                out.print("<script>alert('删除失败,请先删除该角色下用户');location.href='/power/role/getroles';</script>");
            }else{
                out.print("<script>alert('删除角色成功');location.href='/power/role/getroles';</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //更新某一角色的信息
    @RequestMapping("/power/role/updateroles")
    public  String updateroles(Role role,int[] menu){
        //1先删除后添加
        roleService.updaterole(role,menu);
        return  "redirect:/power/role/getroles";
    }

    //查询某一角色的信息
    @RequestMapping("/power/role/findroles")
    public String findroles(int rid,ModelMap map){
         //此时的角色中包含自己所拥有的操作
         Role role = roleService.selectByPrimaryKey(rid);
         //查询数据库中所有的全部操作
         List list = menuService.findall();
         map.put("role",role);
         map.put("mlist",list);
        return "/power/role/edit";
    }


    //角色详情
    @RequestMapping("/power/role/showrole")
    public String showrole(int rid,ModelMap map){
        Role role = roleService.selectByPrimaryKey(rid);
        //查询数据库中所有的全部操作
        List list = menuService.findall();
        map.put("role",role);
        map.put("mlist",list);
        return "/power/role/info";
    }

    //得到全部的角色
    @RequestMapping("/power/role/getroles")
    public String getroles(
            @RequestParam( defaultValue =  "1" )int index,
            @RequestParam( defaultValue =  "5" )int size,
            ModelMap map){
       PageInfo pageInfo = roleService.getroles(index, size);

        map.put("rolepage",pageInfo);
        return "/power/role/list";
    }

    //得到全部菜单
    @RequestMapping("/power/role/getmenus")
    public String getmenus(ModelMap map){
        List menu = menuService.findall();
        map.put("menus",menu);
        return "/power/role/add";
    }

    //添加角色
    @RequestMapping("/power/role/addrole")
    public String addrole(Role role,int[] menu){
        roleService.insertrole(role,menu);
        return "redirect:/power/role/getroles";
    }
}
