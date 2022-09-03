package uk.gov.dwp.health.monitoring.interceptor;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CorrelationIdInterfaceTest {

  @Test
  void should_generate_random_UUID_as_correlation_id() {
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    var actual = cut.generateCorrelationId();
    assertNotNull(actual);
  }

  @Test
  void should_correlation_id_with_null_generate_correlation_id_when_invoked() {
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    cut.setCorrelationIdToContext(null);
    var actual = MDC.get("correlationId");
    assertNotNull(actual);
    verify(cut, times(1)).generateCorrelationId();
  }

  @Test
  void should_set_correlation_id_with_nonNull() {
    var id = "12345";
    CorrelationIdInterface cut = Mockito.spy(CorrelationIdInterface.class);
    cut.setCorrelationIdToContext(id);
    var actual = MDC.get("correlationId");
    assertEquals(id, actual);
    verify(cut, never()).generateCorrelationId();
  }

  @Test
  void should_remove_correlation_id_from_context() {
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
