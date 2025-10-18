package pe.edu.upc.center.autores_platform.authoring.domain.services;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.*;

import java.util.Optional;

public interface AuthorCommandService {

  Long handle(CreateAuthorCommand command);

  void handle(DeleteAuthorCommand command);

  Optional<Author> handle(UpdateAuthorCommand command);

  void handle(UpdateAuthorBiographyByEmailCommand command);

  void handle(DeleteAuthorByProfileIdCommand command);
}
