package uk.gov.dwp.health.monitoring.config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.health.monitoring.handler.HttpResponseErrorHandler;
import uk.gov.dwp.health.monitoring.interceptor.RestTemplateInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {

  @InjectMocks private RestTemplateConfig cut;

  @Test
  void should_create_http_response_errorHandler() {
    var actual = cut.errorHandler();
    assertThat(actual).isNotNull().isInstanceOf(HttpResponseErrorHandler.class);
  }

  @Test
  void should_create_restTemplate_interceptor() {
    var actual = cut.interceptor();
    assertThat(actual).isNotNull().isInstanceOf(RestTemplateInterceptor.class);
  }

  @Test
  void should_returns_restTemplate_with_custom_interceptor_and_custom_errorHandler() {
    var interceptor = mock(RestTemplateInterceptor.class);
    var errorHandler = mock(HttpResponseErrorHandler.class);
    var actual = cut.restTemplate(interceptor, errorHandler);
    assertThat(actual).isNotNull().isInstanceOf(RestTemplate.class);
    assertThat(actual.getErrorHandler()).isEqualTo(errorHandler);
    assertThat(actual.getInterceptors()).containsExactly(interceptor);
  }
}
