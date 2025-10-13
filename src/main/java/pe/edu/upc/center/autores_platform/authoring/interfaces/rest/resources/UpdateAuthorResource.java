package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources;

public record UpdateAuthorResource(
    String name,
    String nationality,
    String biography) {
}
