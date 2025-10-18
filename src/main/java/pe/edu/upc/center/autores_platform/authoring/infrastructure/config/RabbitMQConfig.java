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

  @Bean
  public Queue queue() {
    return new Queue(QUEUE_NAME, true); // durable: true
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  // El Binding conecta la cola con el Exchange a través de la clave de enrutamiento
  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(ROUTING_KEY_EMAIL); // Sólo los mensajes con esta clave irán a esta cola
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    // Usa Jackson (la biblioteca de serialización por defecto de Spring) para convertir objetos a JSON y viceversa.
    return new Jackson2JsonMessageConverter();
  }
}
