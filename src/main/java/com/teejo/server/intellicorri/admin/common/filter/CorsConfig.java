package com.teejo.server.intellicorri.admin.common.filter;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
           registry.addMapping("/**")
           .allowedOrigins("*")
           .allowedMethods("GET", "POST", "DELETE", "PUT")
           .allowCredentials(false)
           .maxAge(3600);
    }
    
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }
    
}