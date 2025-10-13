package pe.edu.upc.center.autores_platform.authoring.domain.model.commands;

public record UpdateAuthorCommand(
    Long authorId,
    String name,
    String nationality,
    String biography) {
}
