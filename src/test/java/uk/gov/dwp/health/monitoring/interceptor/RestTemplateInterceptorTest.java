package uk.gov.dwp.health.monitoring.interceptor;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(ReplaceUnderscores.class)
class RestTemplateInterceptorTest {

  @Test
  void should_intercept_exist_correlation_id_passed_on() throws IOException {
    var correlationId = UUID.randomUUID().toString();
    MDC.put("correlationId", correlationId);
    var req = mock(HttpRequest.class);
    var headers = spy(new HttpHeaders());
    when(req.getHeaders()).thenReturn(headers);
    var body = "data".getBytes(StandardCharsets.UTF_8);
    var exe = mock(ClientHttpRequestExecution.class);

    var cut = spy(new RestTemplateInterceptor());

    cut.intercept(req, body, exe);
    var captor = ArgumentCaptor.forClass(String.class);

    verify(headers).add(captor.capture(), captor.capture());
    assertEquals(Arrays.asList("x-correlation-id", correlationId), captor.getAllValues());
    verify(exe).execute(req, body);
    verify(cut, never()).generateCorrelationId();
  }

  @Test
  void should_intercept_new_correlation_id_generated_and_passed_on() throws IOException {
    var req = mock(HttpRequest.class);
    var headers = spy(new HttpHeaders());
    when(req.getHeaders()).thenReturn(headers);
    var body = "data".getBytes(StandardCharsets.UTF_8);
    var exe = mock(ClientHttpRequestExecution.class);

    var cut = spy(new RestTemplateInterceptor());

    cut.intercept(req, body, exe);
    var captor = ArgumentCaptor.forClass(String.class);

    verify(headers).add(captor.capture(), captor.capture());
    assertEquals("x-correlation-id", captor.getAllValues().get(0));
    assertNotNull(captor.getAllValues().get(1));
    verify(exe).execute(req, body);
  }
}
