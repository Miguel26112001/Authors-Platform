package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.CreatedAuthorResource;

public class CreatedAuthorResourceFromEntityAssembler {

  public static CreatedAuthorResource toResourceFromEntity(Author entity) {
    Long profileId = entity.getProfileId().profileId();

    return new CreatedAuthorResource(
        entity.getId(),
        entity.getDisplayName(),
        entity.getBiography(),
        profileId
    );
  }
}
