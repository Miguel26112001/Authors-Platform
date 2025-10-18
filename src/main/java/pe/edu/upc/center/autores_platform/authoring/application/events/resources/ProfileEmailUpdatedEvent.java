package pe.edu.upc.center.autores_platform.authoring.application.events.resources;

public record ProfileEmailUpdatedEvent(
    Long profileId,
    String newEmail) {
}
