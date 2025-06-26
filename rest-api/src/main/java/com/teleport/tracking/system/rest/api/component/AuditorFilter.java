package com.teleport.tracking.system.rest.api.component;

import com.teleport.tracking.system.repository.config.MongoAuditingConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AuditorFilter implements WebFilter {

  private static final String USERNAME_HEADER = "X-Username";

  private final MongoAuditingConfig mongoAuditingConfig;

  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    final String username = exchange.getRequest().getHeaders().getFirst(USERNAME_HEADER);

    mongoAuditingConfig.setAuditor(username);
    log.debug("Setting auditor to: {}", mongoAuditingConfig.getAuditor());

    return chain.filter(exchange)
        .contextWrite(ctx -> ctx.put(MongoAuditingConfig.AUDITOR_KEY,
            username != null && !username.isBlank() ? username : MongoAuditingConfig.DEFAULT_AUDITOR))
        .doFinally(signalType -> {
          mongoAuditingConfig.clearAuditor();
          log.debug("Cleared auditor after request");
        });
  }
}
