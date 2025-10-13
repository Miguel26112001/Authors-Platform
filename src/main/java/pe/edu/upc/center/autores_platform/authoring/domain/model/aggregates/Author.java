package pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import pe.edu.upc.center.autores_platform.authoring.domain.model.valueobjects.ProfileId;
import pe.edu.upc.center.autores_platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Author extends AuditableAbstractAggregateRoot<Author> {

  @NotBlank(message = "El nombre del autor es obligatorio")
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(nullable = false, length = 100)
  private String name;

  @NotBlank(message = "La nacionalidad del autor es obligatorio")
  @Size(max = 50, message = "La nacionalidad no puede exceder 50 caracteres")
  @Column(length = 50)
  private String nationality;

  @Size(max = 255, message = "La biografía no puede exceder 255 caracteres")
  private String biography;

  @Embedded
  private ProfileId profileId;

  public Author update(String name, String nationality, String biography) {
    this.name = name;
    this.nationality = nationality;
    this.biography = biography;
    return this;
  }

  public String getDisplayName() {
    return name + (nationality != null ? " (" + nationality + ")" : "");
  }
}