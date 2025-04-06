package itmo.lab5.cli.commands;

import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import itmo.lab5.cli.helpers.*;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.enums.*;
import itmo.lab5.models.*;

public class InsertCommand implements Command {
  private final Scanner scanner = new Scanner(System.in);
  private final ReaderUtil inputReader = new ReaderUtil(scanner);

  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length < 1) {
      return "Usage: insert null {element}";
    }

    Date creationDate = new Date();
    String name = inputReader.promptString("- Enter name: ", false, null);

    System.out.println("- Coordinates ");
    int x = inputReader.promptNumber("\t Enter x: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer::parseInt, null);
    Long y = inputReader.promptNumber("\t Enter y: ", Long.MIN_VALUE, Long.MAX_VALUE, Long::parseLong, null);
    var coordinates = new Coordinates(x, y);

    Double area = inputReader.promptNumber("\t Enter square: ", 0.0, 626.0, Double::parseDouble, null);
    int numberOfRooms = inputReader.promptNumber("\t Enter rooms count: ", 1, Integer.MAX_VALUE, Integer::parseInt,
        null);

    System.out.println("- Furnish");
    Furnish furnish = inputReader.promptEnum("\t Enter furnish type: ", Furnish.class, null);

    System.out.println("- View");
    View view = inputReader.promptEnumNullable("\t Enter view type: ", View.class, null);

    System.out.println("- Transport");
    Transport transport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);

    System.out.println("- House");
    System.out.print("\t Enter house name: ");
    String houseName = scanner.nextLine().trim();

    House house = null;

    if (!houseName.isEmpty()) {
      int year = inputReader.promptNumber("\t Enter house age: ", 1, 959, Integer::parseInt, null);
      long floors = inputReader.promptNumber("\t Enter house floors count: ", 1L, 77L, Long::parseLong, null);
      house = new House(houseName, year, floors);
    }

    try {
      HashMap<Integer, Flat> collection = (HashMap<Integer, Flat>) context.get("collection");
      int newID = collection.size() + 1;
      Flat flat = new Flat(
          newID,
          name,
          coordinates,
          creationDate,
          area,
          numberOfRooms,
          furnish,
          view,
          transport,
          house);

      collection.put(newID, flat);
    } catch (ClassCastException e) {
      return "There's an error while trying to add new element. Collection is broken.";
    }

    return "New flat was successfully inserted!";
  }
}
