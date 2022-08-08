package uk.gov.dwp.health.monitoring.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CorrelationIdInterfaceTest {

  @Test
  @DisplayName("test generate random uuid as correlation id")
  void testGenerateRandomUuidAsCorrelationId() {
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    var actual = cut.generateCorrelationId();
    assertNotNull(actual);
  }

  @Test
  @DisplayName("test set correlation id with null generateCorrelationId invoked")
  void testCorrelationIdWithNullGenerateCorrelationIdInvoked() {
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    cut.setCorrelationIdToContext(null);
    var actual = MDC.get("correlationId");
    assertNotNull(actual);
    verify(cut, times(1)).generateCorrelationId();
  }

  @Test
  @DisplayName("test set correlation id with nonNull")
  void testSetCorrelationIdWithNonNull() {
    var id = "12345";
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    cut.setCorrelationIdToContext(id);
    var actual = MDC.get("correlationId");
    assertEquals(id, actual);
    verify(cut, never()).generateCorrelationId();
  }

  @Test
  @DisplayName("test remove correlation id from context")
  void testRemoveCorrelationIdFromContext() {
    var id = "12345";
    MDC.put("correlationId", id);
    var actual = MDC.get("correlationId");
    assertNotNull(actual);
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    cut.removeCorrelationIdFromContext();
    actual = MDC.get("correlationId");
    assertNull(actual);
  }
}
