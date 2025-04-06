package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;

public class ExitCommand implements Command {
  @Override
  public String execute(String[] args, CommandContext context) {
    System.exit(0);
    return "";
  }
}
