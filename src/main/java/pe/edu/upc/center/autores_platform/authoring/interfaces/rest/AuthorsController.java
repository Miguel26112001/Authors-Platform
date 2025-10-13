package pe.edu.upc.center.autores_platform.authoring.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.center.autores_platform.authoring.domain.model.commands.DeleteAuthorCommand;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.GetAllAuthorsQuery;
import pe.edu.upc.center.autores_platform.authoring.domain.model.queries.GetAuthorByIdQuery;
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
    var course = authorQueryService.handle(getAuthorByIdQuery);

    if (course.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    var courseResource = AuthorResourceFromEntityAssembler
        .toResourceFromEntity(course.get());

    return ResponseEntity.ok(courseResource);
  }

  @GetMapping
  public ResponseEntity<List<AuthorResource>> getAllAuthors() {
    var getAllAuthorsQuery = new GetAllAuthorsQuery();
    var authors = authorQueryService.handle(getAllAuthorsQuery);

    var authorResources = authors.stream().map(AuthorResourceFromEntityAssembler::toResourceFromEntity).toList();

    return ResponseEntity.ok(authorResources);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AuthorResource> updateCourse(
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
