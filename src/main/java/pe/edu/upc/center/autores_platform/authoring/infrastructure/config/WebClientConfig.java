package pe.edu.upc.center.autores_platform.authoring.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  // URL BASE DEL MICROSERVICIO PROFILE
  // En un entorno de producción, esto usaría un Service Discovery como Eureka o Consul,
  // o una URL de Kubernetes. Para la prueba local, usamos localhost y el puerto.
  private final String profileServiceBaseUrl = "http://localhost:8080";

  @Bean
  public WebClient profileWebClient() {
    return WebClient.builder()
        .baseUrl(profileServiceBaseUrl)
        .build();
  }
}
