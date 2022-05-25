package uk.gov.dwp.health.pip.monitoring.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestResponseExceptionTest {

  @Test
  @DisplayName("test response exception")
  void testResponseException() {
    var message = "INTERNAL_ERROR";
    var code = 500;
    var cut = new RestResponseException(message, code);
    assertEquals(code, cut.getCode());
    assertEquals(message, cut.getMessage());
  }
}
