package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.*;
import itmo.lab5.models.Flat;

import java.util.HashMap;

public class ReplaceCommand implements Command {
  private String classificator;

  public ReplaceCommand(String classificator) {
    this.classificator = classificator;
  }

  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();
    int idToUpdate;

    HashMap<String, String> params = new HashMap<>();

    for (String arg : args) {
      String[] parts = arg.split("=", 2);
      if (parts.length == 2)
        params.put(parts[0], parts[1]);
    }

    if (args.length < 1)
      return "Can't get value without id!";

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    try {
      idToUpdate = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "Can't parse provided id!";
    }

    if (collection.isEmpty() || collection.size() == 0)
      return "Nothing to show!";

    var currentFlat = collection.get(idToUpdate);

    return "";
  }
}
