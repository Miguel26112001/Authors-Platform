package pe.edu.upc.center.autores_platform.authoring.domain.model.commands;

public record CreateAuthorCommand(String name, String surname, String biography, Long profileId) {
}
