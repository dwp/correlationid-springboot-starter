package uk.gov.dwp.health.pip.monitoring.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.dwp.health.pip.monitoring.handler.HttpResponseErrorHandler;
import uk.gov.dwp.health.pip.monitoring.interceptor.RestTemplateInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {

  @InjectMocks private RestTemplateConfig cut;

  @Test
  @DisplayName("test returns HttpResponseErrorHandler")
  void testReturnsHttpResponseErrorHandler() {
    var actual = cut.errorHandler();
    assertThat(actual).isNotNull().isInstanceOf(HttpResponseErrorHandler.class);
  }

  @Test
  @DisplayName("test returns RestTemplateInterceptor")
  void testReturnsRestTemplateInterceptor() {
    var actual = cut.interceptor();
    assertThat(actual).isNotNull().isInstanceOf(RestTemplateInterceptor.class);
  }

  @Test
  @DisplayName("test returns restTemplate with custom interceptor and errorHandler")
  void testReturnsRestTemplateWithCustomInterceptorAndErrorHandler() {
    var interceptor = mock(RestTemplateInterceptor.class);
    var errorHandler = mock(HttpResponseErrorHandler.class);
    var actual = cut.restTemplate(interceptor, errorHandler);
    assertThat(actual).isNotNull().isInstanceOf(RestTemplate.class);
    assertThat(actual.getErrorHandler()).isEqualTo(errorHandler);
    assertThat(actual.getInterceptors()).containsExactly(interceptor);
  }
}
