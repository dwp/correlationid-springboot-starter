package uk.gov.dwp.health.monitoring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class RestTemplateInterceptor
    implements ClientHttpRequestInterceptor, CorrelationIdInterface {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution exe) throws IOException {
    final var correlationId = getCorrelationIdFromContext();
    log.info(
        "Current request correlation id {}", correlationId == null ? "UNKNOWN" : correlationId);
    final var xCorrelationId = correlationId == null ? generateCorrelationId() : correlationId;
    request.getHeaders().add(X_CORRELATION_ID, xCorrelationId);
    log.info("Set rest-template header with X-Correlation-id {}", xCorrelationId);
    return exe.execute(request, body);
  }
}
