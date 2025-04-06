package itmo.lab5.cli.commands;

import java.util.HashMap;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

public class InfoCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var flats = new HashMap<Integer, Flat>();

    try {
      flats = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Can't parse collection. Something goes wrong!";
    }

    if (flats == null || flats.isEmpty()) {
      return "Collection is empty now!";
    }

    var anyFlat = flats.values().iterator().next();

    return ("Information about collection: \n" +
        "Collections stores in: " + flats.getClass().getName() + "\n" +
        "Collection consists of: " + anyFlat.getClass().getName() + "\n" +
        "Items: " + flats.size());
  }
}
