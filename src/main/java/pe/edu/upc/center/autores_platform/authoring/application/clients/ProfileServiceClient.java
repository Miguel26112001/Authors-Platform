package pe.edu.upc.center.autores_platform.authoring.application.clients;

import pe.edu.upc.center.autores_platform.authoring.application.clients.resources.ProfileResource;

import java.util.Optional;

public interface ProfileServiceClient {
  /**
   * Verifica si un perfil con el ID dado existe en el microservicio Profile.
   * @param profileId El ID del perfil.
   * @return true si el perfil existe, false en caso contrario.
   */
  boolean doesProfileExist(Long profileId);

  /**
   * Obtiene la información del perfil por ID.
   * @param profileId El ID del perfil.
   * @return Un Optional que contiene el ProfileResource si existe, Optional.empty() si no se encuentra.
   */
  Optional<ProfileResource> fetchProfileByProfileId(Long profileId);
}