package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources;

public record CreatedAuthorResource(
    Long id,
    String name,
    String biography,
    Long profileId) {
}
