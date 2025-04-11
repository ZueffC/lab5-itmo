package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

import java.util.*;
import java.util.stream.Collectors;

public class FieldCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    if (collection.isEmpty() || collection.size() == 0) {
      return "Nothing to show!";
    }

    List<Map.Entry<Integer, Flat>> sortedEntries = collection.entrySet().stream()
        .sorted(Comparator.comparingInt(entry -> entry.getValue().getNumberOfRooms()))
        .collect(Collectors.toList());

    sortedEntries.forEach(entry -> {
      System.out.println("Key: " + entry.getKey() +
          ", Rooms: " + entry.getValue().getNumberOfRooms());
    });

    return "";
  }
}
