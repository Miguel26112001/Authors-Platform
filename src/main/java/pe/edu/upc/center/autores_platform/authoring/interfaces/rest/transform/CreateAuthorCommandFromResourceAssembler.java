package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform;

import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.CreateAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.CreateAuthorResource;

public class CreateAuthorCommandFromResourceAssembler {

  public static CreateAuthorCommand toCommandFromResource(CreateAuthorResource resource) {
    return new CreateAuthorCommand(
        resource.name(),
        resource.nationality(),
        resource.biography(),
        resource.profileId());
  }
}
