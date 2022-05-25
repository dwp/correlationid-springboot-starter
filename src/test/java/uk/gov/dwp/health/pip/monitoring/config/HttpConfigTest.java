package uk.gov.dwp.health.pip.monitoring.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.dwp.health.pip.monitoring.interceptor.CorrelationIdInterceptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class HttpConfigTest {

  @Test
  @DisplayName("test should add interceptor to registry")
  void testShouldAddInterceptorToRegistry() {
    var registry = spy(new InterceptorRegistry());
    var interceptor = mock(CorrelationIdInterceptor.class);
    var cut = new HttpConfig(interceptor);
    cut.addInterceptors(registry);
    verify(registry).addInterceptor(interceptor);
  }
}
