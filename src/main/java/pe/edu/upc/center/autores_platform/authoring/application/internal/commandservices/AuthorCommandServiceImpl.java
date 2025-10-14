package pe.edu.upc.center.autores_platform.authoring.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pe.edu.upc.center.autores_platform.authoring.application.clients.ProfileServiceClient;
import pe.edu.upc.center.autores_platform.authoring.domain.exceptions.*;
import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.CreateAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.DeleteAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.UpdateAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.valueobjects.ProfileId;
import pe.edu.upc.center.autores_platform.authoring.domain.services.AuthorCommandService;
import pe.edu.upc.center.autores_platform.authoring.infrastructure.persistence.jpa.repositories.AuthorRepository;

import java.util.Optional;

@Service
public class AuthorCommandServiceImpl implements AuthorCommandService {
  private final AuthorRepository authorRepository;
  private final ProfileServiceClient profileServiceClient;

  public AuthorCommandServiceImpl(AuthorRepository authorRepository, ProfileServiceClient profileServiceClient) {
    this.authorRepository = authorRepository;
    this.profileServiceClient = profileServiceClient;
  }

  @Override
  public Long handle(CreateAuthorCommand command) {
    // 1. **VERIFICACIÓN DE MICROSERVICIO**
    try {
      if (!profileServiceClient.doesProfileExist(command.profileId())) {
        // Caso de negocio: El perfil no existe (Error 404, no es fallo de red)
        throw new ProfileNotFoundException(command.profileId());
      }
    } catch (RestClientException e) {
      throw new ExternalServiceUnavailableException("Profile Service", e.getMessage());

    }

    ProfileId profileId = new ProfileId(command.profileId());
    if (authorRepository.existsByProfileId(profileId)) {
      String criteria = "ProfileId " + command.profileId();
      throw new ResourceAlreadyExistsException("Author", criteria);
    }

    Author author = new Author(
        command.name(),
        command.nationality(),
        command.biography(),
        profileId);

    try {
      authorRepository.save(author);
    } catch (Exception e) {
      throw new CommandExecutionException("CreateAuthorCommand", e.getMessage());
    }

    return author.getId();
  }

  @Override
  public void handle(DeleteAuthorCommand command) {
    if (!authorRepository.existsById(command.authorId())) {
      throw new ResourceNotFoundException("Author", command.authorId());
    }

    try {
      authorRepository.deleteById(command.authorId());
    } catch (Exception e) {
      throw new CommandExecutionException("DeleteAuthorCommand", e.getMessage());
    }
  }

  @Override
  public Optional<Author> handle(UpdateAuthorCommand command) {
    var author = authorRepository.findById(command.authorId());

    if (author.isEmpty()){
      throw new ResourceNotFoundException("Author", command.authorId());
    }
    var authorToUpdate = author.get();

    try {
      var updatedAuthor = authorRepository.save(
          authorToUpdate.update(
              command.name(),
              command.nationality(),
              command.biography()));
      return Optional.of(updatedAuthor);
    } catch (Exception e) {
      throw new CommandExecutionException("UpdateAuthorCommand", e.getMessage());
    }
  }
}
