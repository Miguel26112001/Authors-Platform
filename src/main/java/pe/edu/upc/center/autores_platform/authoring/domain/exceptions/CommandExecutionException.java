package pe.edu.upc.center.autores_platform.authoring.domain.exceptions;

public class CommandExecutionException extends RuntimeException {
  public CommandExecutionException(String commandName, String message) {
    super(String.format("Error executing command %s: %s", commandName, message));
  }
}
