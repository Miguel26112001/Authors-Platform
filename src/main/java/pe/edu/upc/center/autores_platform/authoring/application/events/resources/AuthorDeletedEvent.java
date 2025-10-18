package pe.edu.upc.center.autores_platform.authoring.application.events.resources;

// Contrato de evento para notificar la eliminación de un autor
public record AuthorDeletedEvent(
    Long profileId) { // Enviamos el ProfileId para que Profiles sepa qué borrar
}