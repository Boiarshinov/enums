package dev.boiarshinov.enumsinapi.config;

import dev.boiarshinov.enumsinapi.api.EnumComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeanAppConfig {

    @Bean
    public EnumComponentFactory enumComponentFactory() {
        return new EnumComponentFactory();
    }

    @Bean
    public EnumComponent enumComponent() {
        return enumComponentFactory().getObject();
    }
}
