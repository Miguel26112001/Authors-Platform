package pe.edu.upc.center.autores_platform.authoring.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String EXCHANGE_NAME = "profile-updates-exchange"; // Debe coincidir con Profiles
  public static final String QUEUE_NAME = "authors.profile-email-updates"; // Nombre de la cola de Authors
  public static final String ROUTING_KEY_EMAIL = "profile.email.updated"; // Clave que escucha Authors

  public static final String ROUTING_KEY_PROFILE_DELETED = "profile.deleted"; // La clave que Profiles enviará
  public static final String QUEUE_PROFILE_DELETED = "authors.profile-deleted"; // La nueva cola que recibirá el borrado

  // Bean de cola para la actualización de EMAIL (ya existente)
  @Bean
  public Queue queue() {
    return new Queue(QUEUE_NAME, true); // durable: true
  }

  // Bean de cola para la eliminación de PERFILES (NUEVO)
  @Bean
  public Queue profileDeletedQueue() {
    return new Queue(QUEUE_PROFILE_DELETED, true);
  }

  // Bean de Exchange (ya existente)
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  // Binding para la actualización de EMAIL (ya existente)
  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(ROUTING_KEY_EMAIL); // Sólo los mensajes con esta clave irán a esta cola
  }

  // Binding para la eliminación de PERFILES (NUEVO)
  @Bean
  public Binding profileDeletedBinding(Queue profileDeletedQueue, TopicExchange exchange) {
    return BindingBuilder.bind(profileDeletedQueue)
        .to(exchange)
        .with(ROUTING_KEY_PROFILE_DELETED);
  }

  // Conversor JSON (ya existente)
  @Bean
  public MessageConverter jsonMessageConverter() {
    // Usa Jackson (la biblioteca de serialización por defecto de Spring) para convertir objetos a JSON y viceversa.
    return new Jackson2JsonMessageConverter();
  }
}
