package pe.edu.upc.center.autores_platform.authoring.domain.services;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.GetAllAuthorsQuery;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.GetAuthorByIdQuery;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.GetByProfileId;

import java.util.List;
import java.util.Optional;

public interface AuthorQueryService {

  List<Author> handle(GetAllAuthorsQuery query);

  Optional<Author> handle(GetAuthorByIdQuery query);

  Optional<Author> handle(GetByProfileId query);
}
