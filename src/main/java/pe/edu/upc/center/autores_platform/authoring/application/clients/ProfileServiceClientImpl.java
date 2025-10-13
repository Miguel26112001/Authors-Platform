package pe.edu.upc.center.autores_platform.authoring.application.clients;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ProfileServiceClientImpl implements ProfileServiceClient{
  private final WebClient profileWebClient;

  public ProfileServiceClientImpl(WebClient profileWebClient) {
    this.profileWebClient = profileWebClient;
  }

  @Override
  public boolean doesProfileExist(Long profileId) {
    try {
      // Llama al endpoint GET /api/v1/profiles/{id}
      profileWebClient.get()
          .uri("/api/v1/profiles/{id}", profileId)
          .retrieve()
          // Si el status es 2xx (éxito), bodyToMono se procesa.
          // Si el status es 4xx/5xx, retrieve lanza una excepción (WebClientResponseException)
          // No necesitamos el cuerpo, solo el estado.
          .toBodilessEntity()
          .block(); // <--- ESTO FUERZA LA LLAMADA SÍNCRONA

      // Si block() termina sin excepción, el Profile existe (200 OK)
      return true;

    } catch (WebClientResponseException e) {
      // Si el Profile Controller devuelve 404 Not Found, la validación falla
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return false;
      }
      // Para otros errores (p. ej., 5xx), asumimos que la validación falla
      // o se podría lanzar una excepción de sistema
      System.err.println("Error al contactar Profile Service: " + e.getMessage());
      return false;
    } catch (Exception e) {
      // Manejo de errores de conexión o DNS (servicio caído)
      System.err.println("Error de conexión con Profile Service: " + e.getMessage());
      return false;
    }
  }
}
