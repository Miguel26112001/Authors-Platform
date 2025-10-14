package pe.edu.upc.center.autores_platform.authoring.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.center.autores_platform.authoring.domain.exceptions.ResourceNotFoundException;
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

  // POST: CREATE AUTHOR
  @Operation(summary = "Create an Author", description = "Creates a new Author profile.")
  @ApiResponse(
      responseCode = "201",
      description = "Author created successfully",
      content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreatedAuthorResource.class))
  )
  @ApiResponse(responseCode = "400", description = "Invalid input (e.g., ProfileId <= 0 or invalid request body).")
  @ApiResponse(responseCode = "404", description = "Profile ID not found in the external service.") // <--- AÑADIDO: Maneja ProfileNotFoundException
  @ApiResponse(responseCode = "409", description = "Author already exists for this Profile ID.") // <--- AÑADIDO: Maneja ResourceAlreadyExistsException

  @PostMapping()
  public ResponseEntity<CreatedAuthorResource> createAuthor(
      @RequestBody CreateAuthorResource createAuthorResource
  ) {
    var createAuthorCommand = CreateAuthorCommandFromResourceAssembler
        .toCommandFromResource(createAuthorResource);

    // Si hay un error (ej.: 409, 404), el servicio lanza una excepción que es capturada por el GlobalExceptionHandler.
    var authorId = authorCommandService.handle(createAuthorCommand);

    // Después de una creación exitosa, garantizamos la búsqueda con orElseThrow
    var getAuthorByIdQuery = new GetAuthorByIdQuery(authorId);
    var author = authorQueryService.handle(getAuthorByIdQuery)
        .orElseThrow(() -> new ResourceNotFoundException("Author", authorId));

    var createdAuthorResource = CreatedAuthorResourceFromEntityAssembler
        .toResourceFromEntity(author);

    return new ResponseEntity<>(createdAuthorResource, HttpStatus.CREATED);
  }

  // GET: GET AUTHOR BY ID
  @Operation(summary = "Get Author by ID", description = "Retrieves an Author profile by its internal ID.")
  @ApiResponse(
      responseCode = "200",
      description = "Author found successfully",
      content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResource.class))
  )
  @ApiResponse(responseCode = "404", description = "Author not found")
  @GetMapping("/{id}")
  public ResponseEntity<AuthorResource> getAuthorById(
      @Parameter(description = "Author's internal ID") @PathVariable Long id
  ) {
    var getAuthorByIdQuery = new GetAuthorByIdQuery(id);
    var author = authorQueryService.handle(getAuthorByIdQuery).orElseThrow(() -> new ResourceNotFoundException("Author", id));

    var authorResource = AuthorResourceFromEntityAssembler
        .toResourceFromEntity(author);

    return ResponseEntity.ok(authorResource);
  }

  // GET: GET ALL AUTHORS WITH OPTIONAL FILTERS (Name and/or Nationality)
  @Operation(
      summary = "Get All Authors with Optional Filtering",
      description = "Retrieves a list of Authors. Can be optionally filtered by partial name and/or nationality."
  )
  @Parameters({
      @Parameter(
          name = "name",
          description = "Name or part of the name to filter by (partial, case-insensitive match).",
          in = ParameterIn.QUERY
      ),
      @Parameter(
          name = "nationality",
          description = "Nationality to filter by (partial, case-insensitive match).",
          in = ParameterIn.QUERY
      )
  })
  @ApiResponse(
      responseCode = "200",
      description = "List of Authors retrieved successfully (can be empty)",
      content = @Content(mediaType = APPLICATION_JSON_VALUE)
  )
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

  // GET: GET AUTHOR BY PROFILE ID
  @Operation(summary = "Get Author by Profile ID", description = "Retrieves an Author profile using their associated unique Profile ID.")
  @ApiResponse(
      responseCode = "200",
      description = "Author found successfully",
      content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResource.class))
  )
  @ApiResponse(responseCode = "404", description = "Author not found for the given Profile ID")
  @GetMapping("/profile/{profileId}")
  public ResponseEntity<AuthorResource> getAuthorByProfileId(
      @Parameter(description = "The unique Profile ID associated with the author") @PathVariable Long profileId
  ) {
    try {
      var getAuthorByProfileIdQuery = new GetAuthorByProfileIdQuery(profileId);
      var author = authorQueryService.handle(getAuthorByProfileIdQuery)
          .orElseThrow(() -> new ResourceNotFoundException("Author", "ProfileId " + profileId));

      var authorResource = AuthorResourceFromEntityAssembler
          .toResourceFromEntity(author);

      return ResponseEntity.ok(authorResource);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // PUT: UPDATE AUTHOR
  @Operation(summary = "Update an Author", description = "Updates the details of an existing Author profile.")
  @ApiResponse(
      responseCode = "200",
      description = "Author updated successfully",
      content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResource.class))
  )
  @ApiResponse(responseCode = "400", description = "Invalid request or Author ID not found")
  @PutMapping("/{id}")
  public ResponseEntity<AuthorResource> updateAuthor(
      @Parameter(description = "Author's internal ID") @PathVariable Long id,
      @RequestBody UpdateAuthorResource updateAuthorResource
  ) {
    var updateAuthorCommand = UpdateAuthorCommandFromResourceAssembler
        .toCommandFromResource(id, updateAuthorResource);
    // Si el Author no existe, el CommandService lanza una excepción (404)
    var updatedAuthor = authorCommandService.handle(updateAuthorCommand)
        .orElseThrow(() -> new ResourceNotFoundException("Author", id));

    var authorResource = AuthorResourceFromEntityAssembler.toResourceFromEntity(updatedAuthor);

    return ResponseEntity.ok(authorResource);
  }

  // DELETE: DELETE AUTHOR
  @Operation(summary = "Delete an Author", description = "Deletes an Author profile by its internal ID.")
  @ApiResponse(responseCode = "204", description = "Author deleted successfully (No Content)")
  @ApiResponse(responseCode = "404", description = "Author ID not found")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteAuthor(
      @Parameter(description = "Author's internal ID") @PathVariable Long id)
  {
    var deleteAuthorCommand = new DeleteAuthorCommand(id);
    authorCommandService.handle(deleteAuthorCommand);

    return ResponseEntity.noContent().build();
  }
}
