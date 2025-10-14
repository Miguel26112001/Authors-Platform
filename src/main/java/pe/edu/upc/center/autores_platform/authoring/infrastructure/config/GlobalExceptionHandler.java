package pe.edu.upc.center.autores_platform.authoring.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.center.autores_platform.authoring.domain.exceptions.*;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
  // NUEVO: Mapea Conflicto (409)
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409
  }

  // NUEVO: Mapea Dependencia No Encontrada (404)
  @ExceptionHandler(ProfileNotFoundException.class)
  public ResponseEntity<String> handleProfileNotFoundException(ProfileNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404
  }

  // EXISTENTE: Mapea Recurso No Encontrado (404)
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404
  }

  // NUEVO: Mapea Errores de Ejecución de Comando (500)
  @ExceptionHandler(CommandExecutionException.class)
  public ResponseEntity<String> handleCommandExecutionException(CommandExecutionException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
  }

  // Maneja IllegalArgumentException (Ej: de ProfileId) -> HTTP 400
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    // Retorna 400 Bad Request y el mensaje de la excepción de dominio
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // NUEVO: Mapea Fallo de Servicio Externo (503)
  @ExceptionHandler(ExternalServiceUnavailableException.class)
  public ResponseEntity<String> handleExternalServiceUnavailableException(ExternalServiceUnavailableException ex) {
    // Retorna 503 Service Unavailable
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
  }
}
