package pe.edu.upc.center.autores_platform.authoring.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.valueobjects.ProfileId;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
  Optional<Author> findByProfileId(ProfileId profileId);
}
