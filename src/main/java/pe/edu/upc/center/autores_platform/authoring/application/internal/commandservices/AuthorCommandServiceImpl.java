package pe.edu.upc.center.autores_platform.authoring.application.internal.commandservices;

import org.springframework.stereotype.Service;
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

  public AuthorCommandServiceImpl(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Override
  public Long handle(CreateAuthorCommand command) {
    ProfileId profileId = new ProfileId(command.profileId());

    Author author = new Author(
        command.name(),
        command.nationality(),
        command.biography(),
        profileId);

    authorRepository.save(author);

    return author.getId();
  }

  @Override
  public void handle(DeleteAuthorCommand command) {
    if (!authorRepository.existsById(command.authorId())) {
      throw new IllegalArgumentException("Author with id " + command.authorId() + " don't exist");
    }

    try {
      authorRepository.deleteById(command.authorId());
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting author: " + e.getMessage());
    }
  }

  @Override
  public Optional<Author> handle(UpdateAuthorCommand command) {
    var author = authorRepository.findById(command.authorId());

    if (author.isEmpty()){
      return Optional.empty();
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
      throw new IllegalArgumentException("Error while updating course: " + e.getMessage());
    }
  }
}
