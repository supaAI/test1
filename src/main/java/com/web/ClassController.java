package com.web;

import com.bean.Classes;
import com.github.pagehelper.PageInfo;
import com.service.ClassesService;
import com.service.DepartmentService;
import com.service.MajorService;
import com.service.UserTbService;
import com.util.Excel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes
public class ClassController {

    @Autowired
    private ClassesService service;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private UserTbService userTbService;

    //excal
    @RequestMapping("/Educational/class/import")
    public void toimport(int[] pbox, HttpServletResponse response) {
        PageInfo pg = service.getclasslist(null, null, 0, 0, pbox, null);
        List<Classes> list = pg.getList();
        Excel.headers = new String[]{"院系", "班级编号", "班级名称", "班主任老师", "人数", "班级状态"};
        Excel.createhead(6);
        Excel.createother(list);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = simpleDateFormat.format(new Date());
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("f:\\class" + date + ".xls");
            Excel.export(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter out2 = response.getWriter();
            out2.print("<script>alert('导出成功');location.href='/Educational/class/getclasslist'</script>");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //审核
    @RequestMapping("/Educational/class/updateclassstate")
    public String updatestate(Classes classes) {
        classes.setClassstate("审核中");
        service.updateByPrimaryKeySelective(classes);
        return "redirect:/Educational/class/getclasslist";
    }

    // 审核通过是否
    @RequestMapping("/Educational/udpatestate")
    public String update2(Classes classes) {
        service.updateByPrimaryKeySelective(classes);
        return "redirect:/Educational/getclasses";
    }


    //审核班级
    @RequestMapping("/Educational/getclasses")
    public String getclass(String classname, String classnum,
                           @RequestParam(value = "index", defaultValue = "1") int pageindex,
                           ModelMap map,
                           @RequestParam(value = "size", defaultValue = "5") int size) {

        PageInfo p1 = service.getclasslist(classname, classnum, pageindex, size, null, "审核中");
        map.put("p1", p1);
        map.put("cname", classname);
        map.put("cnum", classnum);
        map.put("size", size);
        return "/Educational/Auditing";
    }


    //班级信息
    @RequestMapping("/Educational/class/infobyid")
    public String infobyid(int classid, ModelMap map) {
        Classes classes = service.selectByPrimaryKey(classid);
        map.put("cklist", classes);
        return "/Educational/class/info";
    }


    //获得全部班级列表
    @RequestMapping("/Educational/class/getclasslist")
    public String getclasslist(String classname, String classnum,
                               @RequestParam(value = "index", defaultValue = "1") int pageindex,
                               @RequestParam(value = "size", defaultValue = "5") int pagesize,
                               ModelMap map) {
        PageInfo p = service.getclasslist(classname, classnum, pageindex, pagesize, null, null);
        map.put("pageinfo", p);
        map.put("cname", classname);
        map.put("cnum", classnum);
        map.put("size", pagesize);

        return "/Educational/class/list";
    }

    //获得全部的学院名称
    @RequestMapping("/Educational/class/getdepartment")
    public String getdepartment(ModelMap map) {
        List list = departmentService.getalldepartment();
        map.put("dlist", list);
        return "/Educational/class/add";
    }

    //获得学院的课程信息
    @RequestMapping("/Educational/class/getmajorbydid")
    @ResponseBody
    public List getmajorbydid(int deptid) {
        List list = majorService.getmajorbydid(deptid);
        return list;
    }

    //学院和专业信息获得讲师信息
    @RequestMapping("/Educational/class/getteacher")
    @ResponseBody
    public List getteacher(int did, int mid) {
        List list = userTbService.gettecher(did, mid, "班主任");
        return list;
    }


    //新增班级
    @RequestMapping("/Educational/class/addclass")
    public String addclass(Classes classes) {
        classes.setClassstate("未审核");
        service.insert(classes);
        return "redirect:/Educational/class/getclasslist";
    }
}
