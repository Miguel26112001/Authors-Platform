package pe.edu.upc.center.autores_platform.authoring.interfaces.rest.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.center.autores_platform.authoring.application.events.resources.ProfileDeletedEvent;
import pe.edu.upc.center.autores_platform.authoring.application.events.resources.ProfileEmailUpdatedEvent;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.DeleteAuthorByProfileIdCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.UpdateAuthorBiographyByEmailCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.services.AuthorCommandService;
import pe.edu.upc.center.autores_platform.authoring.infrastructure.config.RabbitMQConfig;

@Component
public class AuthorProfileEventHandler {
  private final AuthorCommandService authorCommandService;

  public AuthorProfileEventHandler(AuthorCommandService authorCommandService) {
    this.authorCommandService = authorCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) // Escucha la cola que configuramos
  public void handleProfileEmailUpdatedEvent(ProfileEmailUpdatedEvent event) {
    System.out.println("LOG: Received Profile Email Update Event for Profile ID: " + event.profileId() + " with new email: " + event.newEmail());

    // 1. Mapear el evento a un Comando de Aplicación
    var command = new UpdateAuthorBiographyByEmailCommand(
        event.profileId(),
        event.newEmail()
    );

    // 2. Ejecutar el comando. Si falla, Spring AMQP intentará reejecutar (transaccionalidad)
    authorCommandService.handle(command);
  }

  // Nueva cola para borrados de Profiles (debes configurarla en RabbitMQConfig)
  @RabbitListener(queues = RabbitMQConfig.QUEUE_PROFILE_DELETED)
  public void handleProfileDeletedEvent(ProfileDeletedEvent event) {
    // Mapear el evento al nuevo comando de eliminación
    var command = new DeleteAuthorByProfileIdCommand(event.profileId());

    // Ejecutar el comando a través del servicio
    authorCommandService.handle(command);
  }
}
