package pe.edu.upc.center.autores_platform.authoring.domain.exceptions;

// Extender directamente de RuntimeException es suficiente si no necesitas HTTP 404
public class ProfileNotFoundException extends RuntimeException {
  public ProfileNotFoundException(Long profileId) {
    super(String.format("Profile with id %d was not found. Cannot create Author.", profileId));
  }
}