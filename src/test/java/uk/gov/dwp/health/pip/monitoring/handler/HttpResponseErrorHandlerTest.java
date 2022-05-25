package uk.gov.dwp.health.pip.monitoring.handler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import uk.gov.dwp.health.pip.monitoring.exception.RestResponseException;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpResponseErrorHandlerTest {

  private static HttpResponseErrorHandler cut;

  @BeforeAll
  static void setupSpec() {
    cut = new HttpResponseErrorHandler();
  }

  @ParameterizedTest
  @DisplayName("test has error with allowed status code")
  @MethodSource(value = "testCase")
  void testHasErrorWithAllowedStatusCode(HttpStatus respStatus, boolean expect) throws IOException {
    var underTest = new HttpResponseErrorHandler();
    var response = mock(ClientHttpResponse.class);
    when(response.getStatusCode()).thenReturn(respStatus);
    assertEquals(expect, underTest.hasError(response));
  }

  private static Stream<Arguments> testCase() {
    return Stream.of(
        Arguments.of(HttpStatus.BAD_REQUEST, true),
        Arguments.of(HttpStatus.INTERNAL_SERVER_ERROR, true));
  }

  @DisplayName("test handle error")
  @ParameterizedTest
  @MethodSource(value = "errorTestCases")
  void testHandleError(HttpStatus status) throws Exception {
    var resp = mock(ClientHttpResponse.class);
    when(resp.getStatusCode()).thenReturn(status);
    assertThrows(RestResponseException.class, () -> cut.handleError(resp));
  }

  private static Stream<HttpStatus> errorTestCases() {
    return Stream.of(
        HttpStatus.UNAUTHORIZED,
        HttpStatus.INTERNAL_SERVER_ERROR,
        HttpStatus.METHOD_NOT_ALLOWED);
  }
}
