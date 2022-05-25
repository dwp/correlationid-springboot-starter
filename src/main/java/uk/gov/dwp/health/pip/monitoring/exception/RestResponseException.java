package uk.gov.dwp.health.pip.monitoring.exception;

import lombok.Generated;
import lombok.Getter;

@Generated
@Getter
public class RestResponseException extends RuntimeException {

  private final int code;

  public RestResponseException(String message, int code) {
    super(message);
    this.code = code;
  }
}
