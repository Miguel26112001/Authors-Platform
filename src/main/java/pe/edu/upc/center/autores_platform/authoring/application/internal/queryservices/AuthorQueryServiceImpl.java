package pe.edu.upc.center.autores_platform.authoring.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.*;
import pe.edu.upc.center.autores_platform.authoring.domain.model.valueobjects.ProfileId;
import pe.edu.upc.center.autores_platform.authoring.domain.services.AuthorQueryService;
import pe.edu.upc.center.autores_platform.authoring.infrastructure.persistence.jpa.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorQueryServiceImpl implements AuthorQueryService {
  private final AuthorRepository authorRepository;

  public AuthorQueryServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  public List<Author> handle(GetAllAuthorsQuery query) {
    return authorRepository.findAll();
  }

  @Override
  public Optional<Author> handle(GetAuthorByIdQuery query) {
    return authorRepository.findById(query.authorId());
  }

  @Override
  public Optional<Author> handle(GetAuthorByProfileIdQuery query) {
    ProfileId profileId = new ProfileId(query.profileId());
    return authorRepository.findByProfileId(profileId);
  }

  @Override
  public List<Author> handle(GetAuthorsByNameQuery query) {
    return authorRepository.findByNameContainingIgnoreCase(query.name());
  }

  @Override
  public List<Author> handle(GetAuthorsByNationalityQuery query) {
    return authorRepository.findByNationalityContainingIgnoreCase(query.nationality());
  }

  @Override
  public List<Author> handle(GetAuthorsByNameAndNationalityQuery query) {
    return authorRepository.findByNameContainingIgnoreCaseAndNationalityContainingIgnoreCase(query.name(), query.nationality());
  }
}
