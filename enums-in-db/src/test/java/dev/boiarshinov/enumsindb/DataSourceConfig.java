package dev.boiarshinov.enumsindb;

import org.postgresql.Driver;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class DataSourceConfig {

    private static final PostgreSQLContainer POSTGRESQL_CONTAINER;

    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("postgres")
            .withUsername("user")
            .withPassword("pass");
        POSTGRESQL_CONTAINER.start();
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
            .create()
            .url(POSTGRESQL_CONTAINER.getJdbcUrl())
            .username(POSTGRESQL_CONTAINER.getUsername())
            .password(POSTGRESQL_CONTAINER.getPassword())
            .driverClassName(Driver.class.getName())
            .build();
    }

}
