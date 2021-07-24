package com.example.enumsingleton.config;

import com.example.enumsingleton.service.EnumService;
import com.example.enumsingleton.service.RegularService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EnumServiceFactory enumServiceFactory() {
        return new EnumServiceFactory();
    }

    @Bean
    public EnumService enumService(RegularService regularService) {
        EnumService enumService = enumServiceFactory().getObject();
        enumService.setRegularService(regularService);
        return enumService;
    }
}
