package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 源代码目录（上传的实际保存位置）
    private static final String SRC_PICTURES_DIR = "D:/my rubbish/1 homework/1 now/ruangong/-2026-2-R-/demo/src/main/resources/pictures/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 优先从文件系统读取（上传后能立即访问）
        // 其次从 classpath 读取（编译后的文件）
        registry.addResourceHandler("/pictures/**")
                .addResourceLocations(
                        "file:" + SRC_PICTURES_DIR + "/",  // 文件系统优先
                        "classpath:/pictures/"              // classpath 后备
                );

        System.out.println("资源映射 /pictures/** -> file:" + SRC_PICTURES_DIR);
    }
}