package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ========== 修复：将上传目录移到项目同级，避免Spring Boot DevTools自动重启 ==========
    // 项目根路径: D:\my rubbish\1 homework\1 now\ruangong\-2026-2-R-
    // 上传目录:   D:\my rubbish\1 homework\1 now\ruangong\cat-uploads\pictures
    // 不在classpath中，DevTools不会监控，不会触发自动重启
    private static final String UPLOAD_BASE_DIR =
            "D:/my rubbish/1 homework/1 now/ruangong/cat-uploads/pictures/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保目录存在
        File dir = new File(UPLOAD_BASE_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("创建上传目录 " + (created ? "成功" : "失败") + ": " + dir.getAbsolutePath());
        }

        // 将 /pictures/** 映射到外部文件系统目录
        registry.addResourceHandler("/pictures/**")
                .addResourceLocations("file:" + UPLOAD_BASE_DIR);

        System.out.println("资源映射 /pictures/** -> file:" + UPLOAD_BASE_DIR);
    }
}