package com.teleport.tracking.system.repository.config;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Configuration
@EnableReactiveMongoAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "reactiveAuditorAware")
public class MongoAuditingConfig {

  public static final String AUDITOR_KEY = "current-auditor";
  public static final String DEFAULT_AUDITOR = "system";

  private static final ThreadLocal<String> currentAuditorHolder = new ThreadLocal<>();

  @Value("${server.timezone:+08:00}")
  private String timezone;

  @Bean
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now(ZoneOffset.of(timezone)).toInstant());
  }

  @Bean
  public ReactiveAuditorAware<String> reactiveAuditorAware() {
    return () -> Mono.deferContextual(ctx -> {
      // First try to get from reactor context
      if (ctx.hasKey(AUDITOR_KEY)) {
        return Mono.just(ctx.get(AUDITOR_KEY));
      }

      // Then try ThreadLocal as fallback
      String auditor = currentAuditorHolder.get();
      if (auditor != null) {
        return Mono.just(auditor);
      }

      // Default to system if no auditor found
      return Mono.just(DEFAULT_AUDITOR);
    });
  }

  public String getAuditor() {
    String auditor = currentAuditorHolder.get();
    return auditor != null ? auditor : DEFAULT_AUDITOR;
  }

  public void setAuditor(String username) {
    if (Strings.isNotBlank(username)) {
      currentAuditorHolder.set(username);
    } else {
      currentAuditorHolder.remove();
    }
  }

  public void clearAuditor() {
    currentAuditorHolder.remove();
  }
}
