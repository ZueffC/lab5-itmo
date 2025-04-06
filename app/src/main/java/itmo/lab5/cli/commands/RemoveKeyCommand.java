package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

public class RemoveKeyCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    HashMap<Integer, Flat> flats = new HashMap<Integer, Flat>();
    Integer idToDelete = null;

    try {
      flats = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "There's a problem with collection parsing";
    }

    if (flats == null)
      return "Collection is empty. Can't delete anything :(";

    try {
      idToDelete = Integer.parseInt(args[0]);
    } catch (Exception e) {
      return "You provide misstyped argument!";
    }

    if (idToDelete == null)
      return "Check argument's value twice";

    if (!flats.containsKey(idToDelete))
      return "Can't find flat with such id";

    flats.remove(idToDelete);
    return "Successfuly deleted flat!";
  }
}
