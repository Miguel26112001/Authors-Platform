package pe.edu.upc.center.autores_platform.authoring.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException{
  public ResourceNotFoundException(String resourceName, Long resourceId) {
    // Llama al constructor de la clase padre (RuntimeException) con un mensaje específico.
    super(String.format("%s with id %d not found.", resourceName, resourceId));
  }

  public ResourceNotFoundException(String resourceName, String criteria) {
    // Constructor sobrecargado para buscar por criterios no-ID (como ProfileId)
    super(String.format("%s not found with criteria: %s.", resourceName, criteria));
  }
}
