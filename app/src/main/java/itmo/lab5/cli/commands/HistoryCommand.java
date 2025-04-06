package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.helpers.*;

public class HistoryCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var history = new History();

    try {
      history = (History) context.get("history");
    } catch (ClassCastException e) {
      return "There's problem with history. It might be null :(";
    }

    if (history == null)
      return "We reach End of History. Somewhere in the world, one Fukuyama is rejoicing :_)";

    return history.toString();
  }
}
