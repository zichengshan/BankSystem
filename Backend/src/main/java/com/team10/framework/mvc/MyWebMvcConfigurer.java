package com.team10.framework.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**").excludePathPatterns("/login","static/**","/user/create");

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/img/upload/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    //CorsFilter xss request
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Domain names that allow cross-domain requests
        corsConfiguration.addAllowedOrigin("*");
        //Set allowed method（GET\POST\PUT\Delete）
        corsConfiguration.addAllowedMethod("*");
        //Allow any header
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("token");
        //CorsFilter depend on UrlBasedCorsConfigurationSource
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }
}
