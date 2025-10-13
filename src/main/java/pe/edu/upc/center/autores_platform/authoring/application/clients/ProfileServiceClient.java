package pe.edu.upc.center.autores_platform.authoring.application.clients;

public interface ProfileServiceClient {
  /**
   * Verifica si un perfil con el ID dado existe en el microservicio Profile.
   * @param profileId El ID del perfil.
   * @return true si el perfil existe, false en caso contrario.
   */
  boolean doesProfileExist(Long profileId);
}