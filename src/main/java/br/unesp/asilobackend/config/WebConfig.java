package br.unesp.asilobackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**") // Libera todos os endpoints em /api/
        // Permite o frontend em desenvolvimento (Vite default porta 5173)
        .allowedOrigins("http://localhost:3000", "http://localhost:5173") // URLs do seu frontend React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}