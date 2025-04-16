package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.*;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Represents a command that filters a collection of {@link Flat} objects
 * based on a specified classificator and a threshold value.
 * 
 * Implements the Command interface.
 */
public class FilterCommand implements Command {
  private String classificator;

  private static final String description = "command allows to filter collection's valus by provided classificator";

  public final String toString() {
    return this.description;
  }

  /**
   * Constructs a FilterCommand with the specified classificator.
   *
   * @param classificator the type of filtering to apply ("less" or "greater").
   */
  public FilterCommand(String classificator) {
    this.classificator = classificator;
  }

  /**
   * Executes the command, filtering and displaying the flats from the
   * provided collection based on the threshold value.
   *
   * @param args    an array of arguments where the first element is the
   *                threshold value for filtering.
   * @param context the context containing the collection of flats to be
   *                processed.
   * @return a message indicating the result of the execution. If no
   *         classificator is provided, it returns "There's no
   *         classificator provided!". If the collection cannot be parsed,
   *         it returns "Can't parse collection!". If the collection is
   *         empty or the threshold is null, it returns "Nothing to show!".
   */
  @Override
  public String execute(String args[], CommandContext context) {
    if (args.length < 1)
      return "There's no classificator provided!";

    var collection = new HashMap<Integer, Flat>();
    Integer threshold = null;
    List<Flat> result;

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
      threshold = Integer.valueOf(args[0]);
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    if (collection.isEmpty() || threshold == null)
      return "Nothing to show!";

    final int finalThreshold = threshold;
    if (classificator.equals("less")) {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null &&
              flat.getView().ordinal() < finalThreshold)
          .sorted(Comparator.comparingInt(flat -> flat.getView().ordinal()))
          .collect(Collectors.toList());
    } else {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null &&
              flat.getView().ordinal() > finalThreshold)
          .sorted(Comparator.comparingInt(flat -> flat.getView().ordinal()))
          .collect(Collectors.toList());
    }

    result.forEach(entry -> System.out.println(entry));

    return "";
  }
}
