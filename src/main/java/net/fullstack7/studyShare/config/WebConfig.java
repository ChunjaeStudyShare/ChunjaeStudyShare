package net.fullstack7.studyShare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /upload/images/** 패턴의 URL을 실제 파일 시스템의 /home/gyeongmini/upload/images/ 경로로 매핑
        registry.addResourceHandler("/upload/images/**")
                .addResourceLocations("file:///home/gyeongmini/upload/images/");
    }
}