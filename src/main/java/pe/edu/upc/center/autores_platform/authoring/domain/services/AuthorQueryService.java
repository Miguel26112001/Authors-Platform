package pe.edu.upc.center.autores_platform.authoring.domain.services;

import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface AuthorQueryService {
  List<Author> handle(GetAllAuthorsQuery query);
  Optional<Author> handle(GetAuthorByIdQuery query);
  Optional<Author> handle(GetAuthorByProfileIdQuery query);
  List<Author> handle(GetAuthorsByNameQuery getAuthorsByNameQuery);
  List<Author> handle(GetAuthorsByNationalityQuery query);
  List<Author> handle(GetAuthorsByNameAndNationalityQuery query);
}
