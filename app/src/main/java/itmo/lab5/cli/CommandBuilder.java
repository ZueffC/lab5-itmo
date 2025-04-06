package itmo.lab5.cli;

import itmo.lab5.interfaces.Command;

public class CommandBuilder {
  private final CommandRegistry registry;

  public CommandBuilder() {
    this.registry = new CommandRegistry();
  }

  public CommandBuilder register(String name, Command newCommand) {
    this.registry.register(name, newCommand);
    return this;
  }

  public CommandRegistry build() {
    return this.registry;
  }
}
