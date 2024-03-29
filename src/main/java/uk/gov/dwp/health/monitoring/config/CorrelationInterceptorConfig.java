package uk.gov.dwp.health.monitoring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.dwp.health.monitoring.interceptor.CorrelationIdInterceptor;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"uk.gov.dwp.health.monitoring"})
@ConditionalOnProperty(
    prefix = "uk.gov.dwp.health.feature.correlation",
    name = {"enabled"},
    havingValue = "true",
    matchIfMissing = true
)
public class CorrelationInterceptorConfig implements WebMvcConfigurer {

  private final CorrelationIdInterceptor interceptor;

  public CorrelationInterceptorConfig(CorrelationIdInterceptor interceptor) {
    log.info("Initialize correlation id");
    this.interceptor = interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    log.info("Add correlationId header interceptor");
    registry.addInterceptor(interceptor);
  }
}
