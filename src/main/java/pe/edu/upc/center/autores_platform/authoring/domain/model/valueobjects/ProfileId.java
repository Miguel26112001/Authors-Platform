package pe.edu.upc.center.autores_platform.authoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProfileId(Long profileId) {
  public ProfileId {
    if (profileId == null || profileId <= 0) {
      throw new IllegalArgumentException("Profile profileId can not be negative or null");
    }
  }

  public ProfileId() {
    this(0L);
  }
}