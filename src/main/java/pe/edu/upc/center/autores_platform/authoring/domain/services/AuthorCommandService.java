package pe.edu.upc.center.autores_platform.authoring.domain.services;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.CreateAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.DeleteAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.UpdateAuthorBiographyByEmailCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.UpdateAuthorCommand;

import java.util.Optional;

public interface AuthorCommandService {

  Long handle(CreateAuthorCommand command);

  void handle(DeleteAuthorCommand command);

  Optional<Author> handle(UpdateAuthorCommand command);

  void handle(UpdateAuthorBiographyByEmailCommand command);
}
