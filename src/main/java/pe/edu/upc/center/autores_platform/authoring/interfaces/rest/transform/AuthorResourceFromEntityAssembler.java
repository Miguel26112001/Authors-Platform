package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.AuthorResource;

public class AuthorResourceFromEntityAssembler {

  public static AuthorResource toResourceFromEntity(Author entity) {
    Long profileId = entity.getProfileId().profileId();

    return new AuthorResource(
        entity.getId(),
        entity.getName(),
        entity.getNationality(),
        entity.getBiography(),
        profileId
    );
  }
}
