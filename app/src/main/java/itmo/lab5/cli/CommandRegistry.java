package itmo.lab5.cli;

import java.util.HashMap;
import java.util.Map;
import itmo.lab5.interfaces.Command;

public class CommandRegistry {
  private final Map<String, Command> commands = new HashMap<>();

  public void register(String name, Command newCommand) {
    this.commands.put(name, newCommand);
  }

  public Command getByName(String name) {
    return this.commands.get(name);
  }

  public Map<String, Command> getAllCommands() {
    return this.commands;
  }
}
