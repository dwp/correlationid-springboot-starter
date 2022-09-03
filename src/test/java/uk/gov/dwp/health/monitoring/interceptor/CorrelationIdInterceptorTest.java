package uk.gov.dwp.health.monitoring.interceptor;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CorrelationIdInterceptorTest {

  @Test
  void should_preHandle_get_xCorrelation_id_from_header() {
    var cut = spy(new CorrelationIdInterceptor());
    var req = mock(HttpServletRequest.class);
    var resp = mock(HttpServletResponse.class);
    var handler = mock(Object.class);
    var correlationId = UUID.randomUUID().toString();
    when(req.getHeader(anyString())).thenReturn(correlationId);
    boolean actual = cut.preHandle(req, resp, handler);
    assertTrue(actual);
    var captor = ArgumentCaptor.forClass(String.class);
    verify(req).getHeader(captor.capture());
    assertEquals("x-correlation-id", captor.getValue());
    verify(cut, never()).generateCorrelationId();
    assertEquals(correlationId, MDC.get("correlationId"));
  }

  @Test
  void should_preHandle_generate_new_correlation_id() {
    var cut = spy(new CorrelationIdInterceptor());
    var req = mock(HttpServletRequest.class);
    var resp = mock(HttpServletResponse.class);
    var handler = mock(Object.class);
    when(req.getHeader(anyString())).thenReturn(null);
    boolean actual = cut.preHandle(req, resp, handler);
    assertTrue(actual);
    var captor = ArgumentCaptor.forClass(String.class);
    verify(req).getHeader(captor.capture());
    assertEquals("x-correlation-id", captor.getValue());
    verify(cut, times(1)).generateCorrelationId();
    assertNotNull(MDC.get("correlationId"));
  }

  @Test
  void should_read_correlation_id_from_mdc() {
    var cut = spy(new CorrelationIdInterceptor());
    var correlationId = UUID.randomUUID().toString();
    MDC.put("correlationId", correlationId);
    String actual = cut.getCorrelationIdFromContext();
    assertEquals(correlationId, actual);
  }

  @Test
  void should_remove_correlation_id_from_context_after_request_lifecycle_completed() {
    var cut = spy(new CorrelationIdInterceptor());
    var correlationId = UUID.randomUUID().toString();
    MDC.put("correlationId", correlationId);
    cut.removeCorrelationIdFromContext();
    var actual = MDC.get("correlationId");
    assertNull(actual);
  }
}
