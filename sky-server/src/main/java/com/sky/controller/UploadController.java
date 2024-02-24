package com.sky.controller;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * 该类用于上传图片
 */
@Slf4j
@RestController
@RequestMapping("admin/common")
@Api(tags = "文件上传")
public class UploadController {
    @Value("${sky.path}")
    String basePath;
    /**
     * TODO：客户端文件上传和下载
     * 1、file的命名有要求
     * 2、file是一个临时文件所以要转存一下
     */

    @PostMapping("upload")
    public Result<String> uploadFile(MultipartFile file){ //此处命名有说法
        log.info("file :{}",file.toString());
        String originalName=file.getOriginalFilename();
        String suffix=originalName.substring(originalName.lastIndexOf("."));
        String fileName=UUID.randomUUID().toString()+suffix;
        File dir=new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));//转存
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(fileName);
    }


}
