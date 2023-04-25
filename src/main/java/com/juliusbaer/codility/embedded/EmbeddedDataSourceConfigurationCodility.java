package com.juliusbaer.codility.embedded;

import com.juliusbaer.codility.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = {UserRepository.class},
        entityManagerFactoryRef = "embeddedDataSourceEntityManagerFactory")
@ConditionalOnProperty(prefix = "environment", name = "type", havingValue = "embedded", matchIfMissing = true)
//@ConditionalOnExpression("'${spring.profile}'.startsWith('embedded')")
public class EmbeddedDataSourceConfigurationCodility {

    private final DataSourceProperties dataSourceProperties;

    public static GenericContainer axualKafka;

    @Value("${environment.db}")
    private String embeddedDBType;

    public EmbeddedDataSourceConfigurationCodility(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Primary
    @Bean
    DataSource primaryDataSource(EmeddedLocalDatabase database) {
        dataSourceProperties.setPassword(database.getPassword());
        dataSourceProperties.setUsername(database.getUsername());
        dataSourceProperties.setUrl(database.getUrl());
        System.out.println("CONNECTION_URL: " + database.getUrl());
        System.out.println("USER: " + database.getUsername());
        System.out.println("PASSWORD: " + database.getPassword());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    EmeddedLocalDatabase initializeEmbeddedDatabase() {
        if ("oracle".equals(embeddedDBType)) {
            return new EmbeddedOracleDatabase();
        }

        return new EmbeddedPostgresDatabase();
    }

    @Primary
    @Bean("embeddedDataSourceEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean embeddedDataSourceEntityManagerFactory(EntityManagerFactoryBuilder builder, EmeddedLocalDatabase db) {
        return builder
                .dataSource(primaryDataSource(db))
                .packages(UserRepository.class)
                .build();
    }
}
