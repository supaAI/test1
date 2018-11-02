package com.web;


import com.bean.Information;
import com.service.InformationService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class MaterialController {
    @Autowired
    private InformationService service;

    //查询全部书籍信息
    @RequestMapping("/study/selectmaterial")
    public String select(ModelMap map) {
        List<Information> information = service.selectinformation();
        map.put("inf", information);
        return "/study/StudentMaterial";
    }

    //上传书籍
    @RequestMapping("/study/upmaterial")
    public String upmeterial(Information information, @RequestParam("myfile") MultipartFile myfile, HttpServletRequest request) {

        String ty = information.getFiletype();
        if (ty.equals("gif")) {
            information.setFiletype("doc.gif");
        } else {
            information.setFiletype("default.jpg");
        }
        service.insert(information);

        String path = request.getRealPath("/upload");
        try {
            myfile.transferTo(new File(path + "/" + information.getInformationid() + "." + ty));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/study/selectmaterial";
    }

    //下载书籍 将主键作为gif名

    @RequestMapping("/study/downmaterial")
    public ResponseEntity<byte[]> downmaterial(int name, String af, HttpServletRequest request) {
        String path = request.getRealPath("/upload");

        String filename = null;
        if (af.endsWith("gif")) {
            filename = name + "." + "gif";
        } else {
            filename = name + "." + "jpg";
        }
        System.out.println(filename);
        File f = new File(path + "/" + filename);
        ResponseEntity<byte[]> by = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode(filename, "utf-8"));
            by = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f), httpHeaders, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("down");
        return by;

    }

}
