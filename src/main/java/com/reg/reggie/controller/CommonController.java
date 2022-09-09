package com.reg.reggie.controller;

import com.reg.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 *
 * @author e1hax
 * @date 2022-09-09
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());

        //获取原始文件名，及后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID作为文件名
        String filename = UUID.randomUUID().toString() + suffix;

        //判断目录是否存在,不存在就创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //将临时文件保存到本地
        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将文件名返回
        return R.success(filename);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //获取一个输入流，读取图片
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //获取一个输出流，写图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
