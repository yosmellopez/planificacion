package com.planning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(name = "configuracion-production", value = {"/WEB-INF/application-oracle.properties"})
@Profile("prod")
public class DataSourceProd {

    @Autowired
    Environment environment;

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getProperty("db.planning.driverClass"));
        driverManagerDataSource.setUrl(environment.getProperty("db.planning.url"));
        driverManagerDataSource.setUsername(environment.getProperty("db.planning.username"));
        driverManagerDataSource.setPassword(environment.getProperty("db.planning.password"));
        return driverManagerDataSource;
    }
}
