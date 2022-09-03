package uk.gov.dwp.health.monitoring.handler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import uk.gov.dwp.health.monitoring.exception.RestResponseException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@NoArgsConstructor
public class HttpResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    String msg;
    final var statusCode = response.getStatusCode().value();
    if (response.getStatusCode().series() == SERVER_ERROR) {
      msg = String.format("Server error - Response code [%s]", statusCode);
      log.error(msg);
      throw new RestResponseException(msg, statusCode);
    } else if (response.getStatusCode().series() == CLIENT_ERROR) {
      if (response.getStatusCode() == UNAUTHORIZED) {
        msg = "Unauthorized";
        log.info(msg);
        throw new RestResponseException(msg, statusCode);
      }
      msg = String.format("Client error - Response code [%s]", statusCode);
      log.error(msg);
      throw new RestResponseException(msg, statusCode);
    }
  }

  @Override
  public boolean hasError(ClientHttpResponse resp) throws IOException {
    return resp.getStatusCode().series() == CLIENT_ERROR
        || resp.getStatusCode().series() == SERVER_ERROR;
  }
}
