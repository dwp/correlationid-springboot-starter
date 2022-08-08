# Correlation-ID Springboot Starter

The `dwp-springboot-starter-correlationid` assigns a unique `Correlation ID` to every transaction across multiple
services. The ID is passed between services in the request header, and set
in [MDC](http://logback.qos.ch/manual/mdc.html) within the service. In addition, it provides a
configured `RestTemplate` with `x-correlation-id` set in request header.

## Usage

### Build and install locally

```shell

mvn clean verify install

```

### Reference in POM

```xml

<dependence>
  <groupId>uk.gov.dwp.health</groupId>
  <artifactId>springboot-starter-correlationId</artifactId>
  <version>${dwp.correlation.id.version}</version>
</dependence>
```

## Auto-Configuration

By default, the correlation ID is turned off. To enable it, set the following configuration
in the `application.yml`

```yaml
uk:
  gov:
    dwp:
      health:
        feature:
          correlation:
            enabled: true
```

## Override response handler

The starter provide a default RestTemplate with correlation ID capability. The RestTemplate came with a default
HttpResponseError handler, however
developer can override the default HttpResponseHandler to handle more complex response. This can be achieved by;

```java

@Configuration
public class HttpClientConfig {

  @Bean
  public ResponseErrorHandler responseErrorHandler() {
    return new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse response) throws IOException {
        //... test response and return as appropriate
        return false;
      }

      @Override
      public void handleError(ClientHttpResponse response) throws IOException {
        //... handle error as appropriate
      }
    };
  }
}
```

## Dependency and Logging

```xml

<dependency>
  <groupId>uk.gov.dwp.logging</groupId>
  <artifactId>encoded-logger-output</artifactId>
  <version>${dwp.encoded.logger.version}</version>
</dependency>
```
