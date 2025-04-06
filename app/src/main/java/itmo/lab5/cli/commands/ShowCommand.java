package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.models.Flat;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.CommandContext;

public class ShowCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      System.out.println("Can't parse collection!");
    }

    if (collection.isEmpty() || collection.size() == 0) {
      return "Nothing to show!";
    }

    for (Flat flat : collection.values()) {
      System.out.println(flat);
    }

    return "";
  }
}
