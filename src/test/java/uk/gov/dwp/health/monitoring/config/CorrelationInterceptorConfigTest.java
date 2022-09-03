package uk.gov.dwp.health.monitoring.config;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.dwp.health.monitoring.interceptor.CorrelationIdInterceptor;

import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CorrelationInterceptorConfigTest {

  @Test
  void should_add_interceptor_to_interceptor_registry() {
    var registry = spy(new InterceptorRegistry());
    var interceptor = mock(CorrelationIdInterceptor.class);
    var cut = new CorrelationInterceptorConfig(interceptor);
    cut.addInterceptors(registry);
    verify(registry).addInterceptor(interceptor);
  }
}
