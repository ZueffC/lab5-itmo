package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a command that processes a collection of Flat objects.
 * This command sorts the flats by the number of rooms and prints the sorted
 * entries.
 * 
 * Implements the Command interface.
 */
public class FieldCommand implements Command {
  private static final String description = "command allows to see flat's id soted by their number of rooms";

  public final String toString() {
    return this.description;
  }

  /**
   * Executes the command, sorting and displaying the flats from the provided
   * collection.
   *
   * @param args    an array of arguments passed to the command.
   * @param context the context containing the collection of flats to be
   *                processed.
   * @return a message indicating the result of the execution. If the collection
   *         is empty,
   *         it returns "Nothing to show!". If the collection cannot be parsed, it
   *         returns
   *         "Can't parse collection!".
   */
  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();
    var result = new StringBuilder();

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    if (collection.isEmpty())
      return "Nothing to show!";

    var sortedEntries = collection.entrySet().stream()
        .sorted(Comparator.comparingInt(entry -> entry.getValue().getNumberOfRooms()))
        .collect(Collectors.toList());

    sortedEntries.forEach(entry -> result.append("Key: " + entry.getKey() +
        ", Rooms: " + entry.getValue().getNumberOfRooms() + "\n"));

    return result.toString().substring(0, result.length() - 2);
  }
}
