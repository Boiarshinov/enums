package com.example.enumsingleton;

import com.example.enumsingleton.service.EnumService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EnumSingletonApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(EnumSingletonApplication.class, args);

        EnumService enumService = (EnumService) context.getBean("enumService");
        String enumDependencyInjectionResult = enumService.doWork();

        if(!"Enum dependency injection works well!".equals(enumDependencyInjectionResult)) {
            throw new IllegalStateException();
        }
    }
}
