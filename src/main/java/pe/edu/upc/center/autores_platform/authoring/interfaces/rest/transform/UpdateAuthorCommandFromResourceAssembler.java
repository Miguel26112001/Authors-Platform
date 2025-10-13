package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform;

import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.UpdateAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.UpdateAuthorResource;

public class UpdateAuthorCommandFromResourceAssembler {

  public static UpdateAuthorCommand toCommandFromResource(Long authorId, UpdateAuthorResource resource) {
    return new UpdateAuthorCommand(
        authorId,
        resource.name(),
        resource.nationality(),
        resource.biography()
    );
  }
}
