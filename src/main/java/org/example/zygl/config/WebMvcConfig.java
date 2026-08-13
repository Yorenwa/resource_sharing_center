package org.example.zygl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * <p>
 * 配置静态资源映射，使上传的文件可通过 HTTP 访问。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UploadConfig uploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadConfig.getPath() + "/";
        registry.addResourceHandler(uploadConfig.getUrlPrefix() + "/**")
                .addResourceLocations(location);
    }
}
