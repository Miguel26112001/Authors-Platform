package pe.edu.upc.center.autores_platform.authoring.application.clients;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Service
public class ProfileServiceClientImpl implements ProfileServiceClient{
  private final WebClient webClientBuilder;

  public ProfileServiceClientImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder
        .baseUrl("http://profiles-service/api/v1/profiles")
        .build();
  }

  @Override
  public boolean doesProfileExist(Long profileId) {
    try {
      webClientBuilder.get()
          .uri("/{profileId}", profileId)
          .retrieve()
          .toBodilessEntity()
          .block();
      return true;
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return false;
      }
      System.err.println("Error al contactar Profile Service: " + e.getMessage());
      return false;
    } catch (Exception e) {
      System.err.println("Error de conexión con Profile Service: " + e.getMessage());
      return false;
    }
  }
}
