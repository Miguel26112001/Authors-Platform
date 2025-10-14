package pe.edu.upc.center.autores_platform.authoring.domain.model.queries;

public record GetAuthorsByNameAndNationalityQuery(
    String name,
    String nationality
) {
}
