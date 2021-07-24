package dev.boiarshinov.enumsinapi.config;

import dev.boiarshinov.enumsinapi.api.EnumComponent;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

public class EnumComponentFactory implements FactoryBean<EnumComponent> {

    @Override
    public EnumComponent getObject() {
        return EnumComponent.INSTANCE;
    }

    @Override
    public Class<?> getObjectType() {
        return EnumComponent.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
