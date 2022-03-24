package com.example.mongodb.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

/**
 * 세팅 참고 url: https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/connection/mongoclientsettings/
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.example.mongodb.repository")
public class MongoDbConfig {

    @Value("${spring.data.mongodb.connect}")
    private String connection;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(connection))
                .applyToConnectionPoolSettings(builder ->
                        builder.maxWaitTime(3, TimeUnit.SECONDS)
                                .maxSize(30)
                                .minSize(10)
                                .build())
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}
