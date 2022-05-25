package uk.gov.dwp.health.pip.monitoring.interceptor;

import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CorrelationIdInterceptorTest {

  @Test
  @DisplayName("test preHandle should get x-correlation-id from header")
  void testPreHandleShouldGetXCorrelationIdFromHeader() {
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
  @DisplayName("test preHandle should generate new correlation id")
  void testPreHandleShouldGenerateNewCorrelationId() {
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
  @DisplayName("test should read correlation id from MDC")
  void testShouldReadCorrelationIdFromMdc() {
    var cut = spy(new CorrelationIdInterceptor());
    var correlationId = UUID.randomUUID().toString();
    MDC.put("correlationId", correlationId);
    String actual = cut.getCorrelationIdFromContext();
    assertEquals(correlationId, actual);
  }

  @Test
  @DisplayName("test remove correlation id from context after request life cycle completes")
  void testRemoveCorrelationIdFromContextAfterRequestLifecycleComplete() {
    var cut = spy(new CorrelationIdInterceptor());
    var correlationId = UUID.randomUUID().toString();
    MDC.put("correlationId", correlationId);
    cut.removeCorrelationIdFromContext();
    var actual = MDC.get("correlationId");
    assertNull(actual);
  }
}
