package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.*;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;
import itmo.lab5.models.enums.*;

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
  private final String classifier;

  public FilterCommand(String classifier) {
    this.classifier = classifier;
  }

  private static final String description = "command allows to filter collection's valus by provided classificator";

  public final String toString() {
    return this.description;
  }

  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length == 0) {
      return "Usage: filter_less_than_view [view] / filter_greater_than_view [view]";
    }

    HashMap<Integer, Flat> collection;
    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Failed to retrieve collection.";
    }

    if (collection.isEmpty()) {
      return "Collection is empty.";
    }

    String input = args[0].trim();
    View targetView = parseViewInput(input);

    if (targetView == null) {
      return String.format("Invalid view value. Valid options are: %s", getValidViewOptions());
    }

    List<Flat> result;
    if ("less".equalsIgnoreCase(classifier)) {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null && flat.getView().ordinal() < targetView.ordinal())
          .collect(Collectors.toList());
    } else {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null && flat.getView().ordinal() > targetView.ordinal())
          .collect(Collectors.toList());
    }

    if (result.isEmpty()) {
      return "No matching flats found.";
    }

    StringBuilder output = new StringBuilder();
    for (Flat flat : result) {
      output.append(flat.toString()).append("\n");
    }

    return output.toString();
  }

  /**
   * Parses the input string as either an ordinal index or as a case-insensitive
   * enum name.
   *
   * @param input user-provided value representing a View enum.
   * @return the corresponding View enum constant or null if invalid.
   */
  private View parseViewInput(String input) {
    try {
      int index = Integer.parseInt(input);
      if (index >= 0 && index < View.values().length) {
        return View.values()[index];
      }
    } catch (NumberFormatException ignored) {
    }

    try {
      return View.valueOf(input.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * Returns a comma-separated list of valid View enum names.
   *
   * @return string representation of available View options.
   */
  private String getValidViewOptions() {
    return String.join(", ", Arrays.stream(View.values()).map(Enum::name).toList());
  }
}
