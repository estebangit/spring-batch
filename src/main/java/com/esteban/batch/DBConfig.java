package com.esteban.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Bean(name = "dbSource")
    @Primary
    @ConfigurationProperties(prefix = "source")
    public DataSource sourceDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("dbSource")
                .addScript("schema-a.sql")
                .addScript("initdb.sql")
                .build();
        // return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbDestination")
    @ConfigurationProperties(prefix = "destination")
    public DataSource destinationDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("dbDestination")
                .addScript("schema-b.sql")
                .build();
        // return DataSourceBuilder.create().build();
    }

//    private ResourcelessTransactionManager transactionManager() {
//        return new ResourcelessTransactionManager();
//    }


}
