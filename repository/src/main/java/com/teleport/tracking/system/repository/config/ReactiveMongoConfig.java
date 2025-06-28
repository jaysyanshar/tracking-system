package com.teleport.tracking.system.repository.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories(basePackages = {
    "com.teleport.tracking.system.repository",
    "com.teleport.tracking.system.entity.dao"
})
@Import(MongoAuditingConfig.class)
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Value("${spring.data.mongodb.auto-index-creation:true}")
  private boolean autoIndexCreation;

  @Value("${spring.data.mongodb.connect-timeout:4000}")
  private int connectTimeout;

  @Value("${spring.data.mongodb.socket-timeout:4000}")
  private int socketTimeout;

  @Override
  @NonNull
  protected String getDatabaseName() {
    return databaseName;
  }

  @Override
  @NonNull
  @Bean
  public MongoClient reactiveMongoClient() {
    ConnectionString connectionString = new ConnectionString(mongoUri);

    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .applyToSocketSettings(builder ->
            builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                   .readTimeout(socketTimeout, TimeUnit.MILLISECONDS))
        .build();

    return MongoClients.create(settings);
  }

  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
  }

  @Override
  protected boolean autoIndexCreation() {
    return autoIndexCreation;
  }
}
