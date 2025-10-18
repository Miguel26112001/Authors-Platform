package pe.edu.upc.center.autores_platform.authoring.application.internal.outboundservices;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.center.autores_platform.authoring.application.events.resources.AuthorDeletedEvent;
import pe.edu.upc.center.autores_platform.authoring.infrastructure.config.RabbitMQConfig;

@Service
public class AuthorMessagingService {
  private final RabbitTemplate rabbitTemplate;
  private static final String ROUTING_KEY_DELETED = "author.deleted";

  public AuthorMessagingService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendAuthorDeletedEvent(Long profileId) {
    var event = new AuthorDeletedEvent(profileId);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE_NAME, // Usamos el mismo Exchange
        ROUTING_KEY_DELETED,
        event
    );
  }
}
