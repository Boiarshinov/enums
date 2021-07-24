package com.example.enumsingleton.config;

import com.example.enumsingleton.service.EnumService;
import org.springframework.beans.factory.FactoryBean;

public class EnumServiceFactory implements FactoryBean<EnumService> {

    @Override
    public EnumService getObject() {
        return EnumService.INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return EnumService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
