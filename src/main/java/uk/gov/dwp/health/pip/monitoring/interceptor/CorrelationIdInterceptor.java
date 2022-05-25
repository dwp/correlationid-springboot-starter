package uk.gov.dwp.health.pip.monitoring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CorrelationIdInterceptor extends HandlerInterceptorAdapter
    implements CorrelationIdInterface {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    setCorrelationIdToContext(request.getHeader(X_CORRELATION_ID));
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    removeCorrelationIdFromContext();
  }
}
