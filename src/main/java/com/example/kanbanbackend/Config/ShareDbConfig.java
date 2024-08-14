package com.example.kanbanbackend.Config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "shareEntityManagerFactory",
        transactionManagerRef = "shareTransactionManager",
        basePackages = {
                "com.example.kanbanbackend.Repository.Share"
        }
)
public class ShareDbConfig {
    @Primary
    @Bean(name = "shareDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.share")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }



    @Primary
    @Bean(name = "shareEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("shareDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder.dataSource(dataSource)
                .properties(properties).packages("com.example.kanbanbackend.Entitites.Share").persistenceUnit("share").build();
    }

    @Primary
    @Bean(name = "shareTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("shareEntityManagerFactory") EntityManagerFactory shareEntityManagerFactory) {
        return new JpaTransactionManager(shareEntityManagerFactory);
    }
}
