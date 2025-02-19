package com.regional.customerdashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // CORS for all API endpoints
                        .allowedOrigins("http://localhost:5173") // requests from the frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // requests method
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}