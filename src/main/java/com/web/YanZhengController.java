package com.web;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.util.YanZheng;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class YanZhengController {

    @RequestMapping("/getyanzheng")
    public void getyangzheng(HttpServletResponse response) {
        try {
            YanZheng.createcode();
            BufferedImage image= YanZheng.createimage();
            //以流的方式返回给客户端
            response.setContentType("image/jpeg");
            ByteArrayOutputStream bt=new ByteArrayOutputStream();

            //将图片转换成字节流
            ImageIO.write(image,"jpeg",bt);

            //得到输出流，返回给客户端
            ServletOutputStream outputStream=response.getOutputStream();
            outputStream.write(bt.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
