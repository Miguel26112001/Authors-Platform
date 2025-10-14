package pe.edu.upc.center.autores_platform.authoring.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.center.autores_platform.authoring.domain.model.aggregates.Author;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.DeleteAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.*;
import pe.edu.upc.center.autores_platform.authoring.domain.services.AuthorCommandService;
import pe.edu.upc.center.autores_platform.authoring.domain.services.AuthorQueryService;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.AuthorResource;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.CreateAuthorResource;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.CreatedAuthorResource;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.resources.UpdateAuthorResource;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform.AuthorResourceFromEntityAssembler;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform.CreateAuthorCommandFromResourceAssembler;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform.CreatedAuthorResourceFromEntityAssembler;
import pe.edu.upc.center.autores_platform.authoring.interfaces.rest.transform.UpdateAuthorCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/authors", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authors", description = "Authors Management Endpoints")
public class AuthorsController {
  private final AuthorCommandService authorCommandService;
  private final AuthorQueryService authorQueryService;

  public AuthorsController(AuthorCommandService authorCommandService, AuthorQueryService authorQueryService) {
    this.authorCommandService = authorCommandService;
    this.authorQueryService = authorQueryService;
  }

  @PostMapping()
  public ResponseEntity<CreatedAuthorResource> createAuthor(
      @RequestBody CreateAuthorResource createAuthorResource
  ) {
    var createAuthorCommand = CreateAuthorCommandFromResourceAssembler
        .toCommandFromResource(createAuthorResource);

    var authorId = authorCommandService.handle(createAuthorCommand);

    if (authorId == null ) {
      return ResponseEntity.badRequest().build();
    }

    var getAuthorByIdQuery = new GetAuthorByIdQuery(authorId);
    var author = authorQueryService.handle(getAuthorByIdQuery);

    if (author.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var createdAuthorResource = CreatedAuthorResourceFromEntityAssembler
        .toResourceFromEntity(author.get());

    return new ResponseEntity<>(createdAuthorResource, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuthorResource> getAuthorById(
      @PathVariable Long id
  ) {
    var getAuthorByIdQuery = new GetAuthorByIdQuery(id);
    var author = authorQueryService.handle(getAuthorByIdQuery);

    if (author.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    var authorResource = AuthorResourceFromEntityAssembler
        .toResourceFromEntity(author.get());

    return ResponseEntity.ok(authorResource);
  }

  @Operation(
      summary = "Get All Authors with Optional Filtering",
      description = "Retrieves a list of Authors. Can be optionally filtered by partial name and/or nationality."
  )
  @Parameters({
      @Parameter(
          name = "name",
          description = "Name or part of the name to filter by.",
          required = false,
          in = ParameterIn.QUERY
      ),
      @Parameter(
          name = "nationality",
          description = "Nationality to filter by.",
          required = false,
          in = ParameterIn.QUERY
      )
  })
  @GetMapping
  public ResponseEntity<List<AuthorResource>> getAllAuthors(
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "nationality", required = false) String nationality
  ) {
    List<Author> authors;
    boolean hasName = name != null && !name.isBlank();
    boolean hasNationality = nationality != null && !nationality.isBlank();

    if (hasName && hasNationality) {
      // Caso 1: Filtrar por Nombre Y Nacionalidad
      var query = new GetAuthorsByNameAndNationalityQuery(name, nationality);
      authors = authorQueryService.handle(query);

    } else if (hasName) {
      // Caso 2: Filtrar solo por Nombre
      var query = new GetAuthorsByNameQuery(name);
      authors = authorQueryService.handle(query);

    } else if (hasNationality) {
      // Caso 3: Filtrar solo por Nacionalidad
      // Nota: Debes crear esta Query simple (GetAuthorsByNationalityQuery)
      var query = new GetAuthorsByNationalityQuery(nationality);
      authors = authorQueryService.handle(query);

    } else {
      // Caso 4: Listado Completo (Sin filtros)
      var query = new GetAllAuthorsQuery();
      authors = authorQueryService.handle(query);
    }

    var authorResources = authors.stream().map(AuthorResourceFromEntityAssembler::toResourceFromEntity).toList();

    return ResponseEntity.ok(authorResources);
  }

  @GetMapping("/profile/{profileId}")
  public ResponseEntity<AuthorResource> getAuthorByProfileId(
      @PathVariable Long profileId
  ) {
    var getAuthorByIdQuery = new GetAuthorByProfileIdQuery(profileId);
    var author = authorQueryService.handle(getAuthorByIdQuery);

    if (author.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    var authorResource = AuthorResourceFromEntityAssembler
        .toResourceFromEntity(author.get());

    return ResponseEntity.ok(authorResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AuthorResource> updateAuthor(
      @PathVariable Long id,
      @RequestBody UpdateAuthorResource updateAuthorResource
  ) {
    var updateAuthorCommand = UpdateAuthorCommandFromResourceAssembler
        .toCommandFromResource(id, updateAuthorResource);
    var updatedAuthor = authorCommandService.handle(updateAuthorCommand);

    if (updatedAuthor.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    var authorResource = AuthorResourceFromEntityAssembler.toResourceFromEntity(updatedAuthor.get());

    return ResponseEntity.ok(authorResource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteAuthor(
      @PathVariable Long id)
  {
    var deleteAuthorCommand = new DeleteAuthorCommand(id);
    authorCommandService.handle(deleteAuthorCommand);

    return ResponseEntity.ok("Author with given id was successfully deleted");
  }
}
