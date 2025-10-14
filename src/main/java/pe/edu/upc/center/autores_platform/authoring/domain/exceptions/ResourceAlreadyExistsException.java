package pe.edu.upc.center.autores_platform.authoring.domain.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String resourceName, String criteria) {
    super(String.format("%s already exists with criteria: %s.", resourceName, criteria));
  }
}
