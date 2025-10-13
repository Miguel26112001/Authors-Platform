package pe.edu.upc.center.autores_platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI authorsPlatformOpenAPI() {
    var openApi = new OpenAPI();
    openApi
        .info(new Info()
            .title("Authors Platform API")
            .description("Authors Platform application REST API documentation.")
            .version("v1.0.0")
            .license(new License().name("Apache 2.0")
                .url("https://springdoc.org")))
        .externalDocs(new ExternalDocumentation()
            .description("Authors Platform Documentation")
            .url("https://github.com/juanvaldez/autores-platform"));
    return openApi;
  }
}
