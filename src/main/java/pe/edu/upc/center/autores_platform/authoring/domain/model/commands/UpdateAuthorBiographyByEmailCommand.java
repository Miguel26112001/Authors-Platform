package pe.edu.upc.center.autores_platform.authoring.domain.model.commands;

// Este comando se lanza cuando llega el evento de cambio de email.
public record UpdateAuthorBiographyByEmailCommand(
    Long profileId,
    String newEmail) {
}