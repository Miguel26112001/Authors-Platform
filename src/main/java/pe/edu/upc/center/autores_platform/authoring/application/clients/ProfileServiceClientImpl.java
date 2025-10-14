package pe.edu.upc.center.autores_platform.authoring.application.clients;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.center.autores_platform.authoring.application.clients.resources.ProfileResource;
import pe.edu.upc.center.autores_platform.authoring.domain.exceptions.ExternalServiceUnavailableException;

import java.util.Optional;

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
      // Mapeamos cualquier otro error de conexión/red a la excepción de dominio
      throw new ExternalServiceUnavailableException("Profile Service", e.getMessage());
    } catch (Exception e) {
      throw new ExternalServiceUnavailableException("Profile Service", e.getMessage());
    }
  }

  @Override
  public Optional<ProfileResource> fetchProfileByProfileId(Long profileId) {
    try {
      // Utilizamos retrieve().bodyToMono().block() para obtener el cuerpo de la respuesta
      ProfileResource resource = webClientBuilder.get()
          .uri("/{profileId}", profileId)
          .retrieve()
          .bodyToMono(ProfileResource.class) // Especificamos el DTO de destino
          .block(); // Bloqueamos (ya que no estamos en un contexto reactivo puro)

      return Optional.ofNullable(resource);
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        // Si la respuesta es 404, el perfil no existe, devolvemos Optional.empty()
        return Optional.empty();
      }
      // Si es cualquier otro error (4xx, 5xx), lanzamos la excepción de indisponibilidad
      throw new ExternalServiceUnavailableException("Profile Service", e.getMessage());
    } catch (Exception e) {
      // Capturamos errores de conexión (timeouts, DNS, etc.)
      throw new ExternalServiceUnavailableException("Profile Service", e.getMessage());
    }
  }
}
