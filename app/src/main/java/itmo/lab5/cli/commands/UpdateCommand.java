package itmo.lab5.cli.commands;

import java.util.*;

import itmo.lab5.cli.helpers.*;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.enums.*;
import itmo.lab5.models.*;

public class UpdateCommand implements Command {
  private final Scanner scanner = new Scanner(System.in);
  private final ReaderUtil inputReader = new ReaderUtil(scanner);

  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length < 1) {
      return "Usage: update id {element}";
    }

    Map<Integer, Flat> collection = null;

    int idToUpdate;
    try {
      idToUpdate = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "Invalid ID format.";
    }

    try {
      collection = (Map<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "There's an error while trying to parse collection";
    }

    if (!collection.containsKey(idToUpdate))
      return "No flat with ID: " + idToUpdate;

    Flat oldFlat = collection.get(idToUpdate);

    System.out.println("Updating flat with ID: " + idToUpdate);
    String name = inputReader.promptString("- Enter name:", false, oldFlat.getName());

    System.out.println("- Coordinates:");
    int x = inputReader.promptNumber("\t Enter x:", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer::parseInt,
        oldFlat.getCoordinates().getX());
    Long y = inputReader.promptNumber("\t Enter y:", Long.MIN_VALUE, Long.MAX_VALUE, Long::parseLong,
        oldFlat.getCoordinates().getY());
    Coordinates coordinates = new Coordinates(x, y);

    Double area = inputReader.promptNumber("\t Enter square:", 0.0, 626.0, Double::parseDouble, oldFlat.getArea());
    int rooms = inputReader.promptNumber("\t Enter rooms count:", 1, Integer.MAX_VALUE, Integer::parseInt,
        oldFlat.getNumberOfRooms());

    System.out.println("- Furnish:");
    Furnish furnish = inputReader.promptEnum("\t Enter furnish type:", Furnish.class, oldFlat.getFurnish());

    System.out.println("- View:");
    View view = inputReader.promptEnumNullable("\t Enter view type:", View.class, oldFlat.getView());

    System.out.println("- Transport:");
    Transport transport = inputReader.promptEnum("\t Enter transport type:", Transport.class, oldFlat.getTransport());

    System.out.println("- House:");
    System.out.print("\t Enter house name: ");
    String houseName = scanner.nextLine().trim();

    House house = oldFlat.getHouse();
    if (!houseName.isEmpty()) {
      int year = inputReader.promptNumber("\t Enter house age: ", 1, 959, Integer::parseInt,
          (house != null ? house.getYear() : 1));

      long floors = inputReader.promptNumber("\t Enter house floors count: ", 1L, 77L, Long::parseLong,
          (house != null ? house.getNumberOfFloors() : 1L));

      house = new House(houseName, year, floors);
    }

    Date creationDate = new Date();

    Flat updatedFlat = new Flat(
        idToUpdate,
        name,
        coordinates,
        creationDate,
        area,
        rooms,
        furnish,
        view,
        transport,
        house);

    collection.put(idToUpdate, updatedFlat);
    return "Flat with ID " + idToUpdate + " was successfully updated.";
  }
}
