package itmo.lab5.interfaces;

import itmo.lab5.cli.CommandContext;

/**
 * Represents a command that can be executed with a set of arguments
 * and a command context.
 */
public interface Command {
  
  /**
   * Executes the command with the provided arguments and context.
   *
   * @param args an array of arguments to be used during execution.
   * @param context the context in which the command is executed.
   * @return a string representing the result of the command execution.
   */
  String execute(String[] args, CommandContext context);
}