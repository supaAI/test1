package com.web;

import com.bean.LeaveBill;
import com.bean.UserTb;
import com.service.ActivitiService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;


    //办理任务
    @RequestMapping("/renwu/dotask")
    public String dotask(String tid) {
        //使用from_key执行流程的请求
        String url = activitiService.getformkey(tid);
        return "redirect:/" + url + "?tid=" + tid; //在流程中体现
    }

    //处理form_key请求
    @RequestMapping("/approval")
    public String approval(String tid,ModelMap map) {
        //通过任务id查询leavebill
        LeaveBill leaveBill = activitiService.finleavebillbytaskid(tid);
        map.put("leavebil",leaveBill);
        return "/renwu/banli";
    }


    //个人任务查询
    @RequestMapping("/renwu/getall")
    public String getalltask(HttpSession session, ModelMap map) {
        UserTb userTb = (UserTb) session.getAttribute("u1");
        List<Task> tasks = activitiService.getalltask(userTb.getUserName());
        map.put("tlist", tasks);
        return "/renwu/list";
    }


    //用户提交请假申请 与流程相关联写在此处
    @RequestMapping("/qingjia/uploadleave")
    public String uploadleave(int leaveid, HttpSession session) {
        UserTb userTb = (UserTb) session.getAttribute("u1");
        activitiService.uploadleave(leaveid, userTb.getUserName());
        return "redirect:/qingjia/getall";
    }


    //删除流程部署
    @RequestMapping("/bushu/delete")
    public void deletedep(String deptid, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            activitiService.deletedep(deptid);
            response.getWriter().print("<script>alert('删除成功');location.href='/bushu/getall'</script>");
        } catch (Exception x) {
            try {
                response.getWriter().write("<script>alert('删除失败');location.href='/bushu/getall</script>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //下载流程图
    @RequestMapping("/bushu/lookimg")
    public String lookimg(String depid, String imgname, ModelMap map) {
        map.put("depid", depid);
        map.put("imgname", imgname);
        return "/bushu/img";
    }

    //下载流程图
    @RequestMapping("/bushu/img")
    public void getimg(String deptid, String imgname, HttpServletResponse response) {
        InputStream inputStream = activitiService
                .findimg(deptid, imgname);

        try {
            OutputStream outputStream = response.getOutputStream();
            int k = -1;
            while ((k = inputStream.read()) != -1) {
                outputStream.write(k);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //查询流程定义和部署请求
    @RequestMapping("/bushu/getall")
    public String getall(ModelMap map) {
        //查询部署信息
        List<Deployment> list = activitiService.getdeplist();
        map.put("deplist", list);

        //查询流程信息
        List<ProcessDefinition> processDefinitionList = activitiService.getprocesslist();

        map.put("processlist", processDefinitionList);

        return "/bushu/list";
    }

    //上传流程文件
    @RequestMapping("/bushu/add")
    public String add(MultipartFile depfile, String name) {

        try {
            activitiService.add(depfile.getInputStream(), name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/bushu/getall";
    }


}
