package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.*;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.*;

public class FilterCommand implements Command {
  private String classificator;

  @Override
  public String execute(String args[], CommandContext context) {
    if (args.length < 1)
      return "There's no classificator provided!";

    var collection = new HashMap<Integer, Flat>();
    Integer threshold = null;
    List<Flat> result;

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
      threshold = Integer.parseInt(args[0]);
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    if (collection.isEmpty() || collection.size() == 0 || threshold == null) {
      return "Nothing to show!";
    }

    final int finalThreshold = threshold;
    if (classificator == "less") {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null && flat.getView().ordinal() < finalThreshold)
          .sorted(Comparator.comparingInt(flat -> flat.getView().ordinal()))
          .collect(Collectors.toList());
    } else {
      result = collection.values().stream()
          .filter(flat -> flat.getView() != null && flat.getView().ordinal() > finalThreshold)
          .sorted(Comparator.comparingInt(flat -> flat.getView().ordinal()))
          .collect(Collectors.toList());
    }

    result.forEach(entry -> {
      System.out.println(entry);
    });

    return "";
  }

  public FilterCommand(String classificator) {
    this.classificator = classificator;
  }
}
