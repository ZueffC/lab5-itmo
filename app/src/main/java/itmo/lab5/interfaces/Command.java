package itmo.lab5.interfaces;

import itmo.lab5.cli.CommandContext;

public interface Command {
  String execute(String[] args, CommandContext context);
}
