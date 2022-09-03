package uk.gov.dwp.health.monitoring.exception;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceUnderscores.class)
class RestResponseExceptionTest {

  @Test
  void test_response_custom_exception() {
    var message = "INTERNAL_ERROR";
    var code = 500;
    var cut = new RestResponseException(message, code);
    assertEquals(code, cut.getCode());
    assertEquals(message, cut.getMessage());
  }
}
