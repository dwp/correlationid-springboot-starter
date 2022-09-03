package uk.gov.dwp.health.monitoring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.health.monitoring.handler.HttpResponseErrorHandler;
import uk.gov.dwp.health.monitoring.interceptor.RestTemplateInterceptor;

import java.util.List;

@Slf4j
@Configuration
@ConditionalOnProperty(
    prefix = "uk.gov.dwp.health.correlation.rest",
    name = {"enabled"},
    havingValue = "true")
public class RestTemplateConfig {

  /**
   * note: override ResponseErrorHandler in parent project through @Configuration see README.md
   * `Override response handler` section for details
   */
  @Bean
  @ConditionalOnMissingBean(value = ResponseErrorHandler.class)
  public HttpResponseErrorHandler errorHandler() {
    return new HttpResponseErrorHandler();
  }

  @Bean
  public RestTemplateInterceptor interceptor() {
    return new RestTemplateInterceptor();
  }

  /**
   * note: override ResponseErrorHandler in parent project through @Configuration see README.md
   * `Override response handler` section for details
   */
  @Bean
  public RestTemplate restTemplate(
      RestTemplateInterceptor interceptor, ResponseErrorHandler errorHandler) {
    log.info("Initialize RestTemplate with CorrelationID interceptor");
    var restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(errorHandler);
    restTemplate.setInterceptors(List.of(interceptor));
    return restTemplate;
  }
}
