package uk.gov.dwp.health.monitoring.interceptor;

import org.slf4j.MDC;

import java.util.UUID;

public interface CorrelationIdInterface {

  String CORRELATION_ID = "correlationId";
  String X_CORRELATION_ID = "x-correlation-id";

  default String generateCorrelationId() {
    return UUID.randomUUID().toString();
  }

  default String getCorrelationIdFromContext() {
    return MDC.get(CORRELATION_ID);
  }

  default void setCorrelationIdToContext(String correlationId) {
    MDC.put(CORRELATION_ID, correlationId != null ? correlationId : generateCorrelationId());
  }

  default void removeCorrelationIdFromContext() {
    MDC.remove(CORRELATION_ID);
  }
}
