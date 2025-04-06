package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

public class ClearCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Can't clear collection. It might be missed";
    }

    collection.clear();
    return "Collection was successfuly cleared!";
  }
}
