package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources;

public record CreateAuthorResource(
    String name,
    String nationality,
    String biography,
    Long profileId) {
}
