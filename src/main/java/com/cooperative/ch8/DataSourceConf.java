package com.cooperative.ch8;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * -Dspring.profiles.active=prod
 */
//@Configuration
public class DataSourceConf {
    @Bean(name = "dataSource")
    public DataSource devSource(Environment env) {
        HikariDataSource prod = getDataSource(env);
        prod.setMaximumPoolSize(2);
        return prod;
    }

    @Bean(name = "dataSource")
    @Profile("test")
    public DataSource testDatasource(Environment env) {
        HikariDataSource test = getDataSource(env);
        test.setMaximumPoolSize(10);
        return test;
    }

    @Bean(name = "dataSource")
    @Profile("prod")
    public DataSource prodSource(Environment env) {
        HikariDataSource prod = getDataSource(env);

        prod.setMaximumPoolSize(100);
        return prod;

    }

    private HikariDataSource getDataSource(Environment env) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        return ds;
    }
}

