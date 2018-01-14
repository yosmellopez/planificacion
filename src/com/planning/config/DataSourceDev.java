package com.planning.config;

import com.planning.util.DatabaseCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@Profile("dev")
@PropertySource(name = "configuracion-dev", value = {"/WEB-INF/application-dev.properties"})
public class DataSourceDev {

    @Autowired
    Environment environment;

    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getProperty("db.planning.driverClass"));
        driverManagerDataSource.setUrl(environment.getProperty("db.planning.url"));
        driverManagerDataSource.setUsername(environment.getProperty("db.planning.username"));
        driverManagerDataSource.setPassword(environment.getProperty("db.planning.password"));
        DatabaseCreator.createDatabaseIfNotExist(environment);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(true);
        populator.addScript(new ClassPathResource("inicio-db.sql"));
//        populator.execute(driverManagerDataSource);
        return driverManagerDataSource;
    }
}
