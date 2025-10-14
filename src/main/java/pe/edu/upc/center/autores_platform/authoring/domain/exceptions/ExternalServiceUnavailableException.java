package pe.edu.upc.center.autores_platform.authoring.domain.exceptions;

public class ExternalServiceUnavailableException extends RuntimeException {
  // Constructor para indicar qué servicio falló
  public ExternalServiceUnavailableException(String serviceName, String message) {
    super(String.format("The external service [%s] is currently unavailable or returned an unexpected error. Details: %s", serviceName, message));
  }
}
