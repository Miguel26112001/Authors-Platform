package pe.edu.upc.center.autores_platform.authoring.domain.model.commands;

public record CreateAuthorCommand(
    String name,
    String nationality,
    String biography,
    Long profileId) {
}
