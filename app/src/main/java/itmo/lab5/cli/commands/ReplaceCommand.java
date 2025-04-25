package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.*;
import itmo.lab5.models.*;
import itmo.lab5.models.enums.*;

import java.util.Date;
import java.util.HashMap;

/**
 * Represents a command that replaces a flat in a collection based on a
 * specified classificator ("greater" or "lower") and a given id.
 * 
 * Implements the {@link Command} interface.
 */
public class ReplaceCommand implements Command {
  private final String classificator;
  private static final String description = "command allows to replace some value of element by it's id";

  public final String toString() {
    return this.description;
  }

  /**
   * Constructs a ReplaceCommand with the specified classificator.
   *
   * @param classificator the type of comparison for replacement, must be
   *                      either "greater" or "lower".
   * @throws IllegalArgumentException if the classificator is not valid.
   */
  public ReplaceCommand(String classificator) {
    if (!classificator.equalsIgnoreCase("greater") &&
        !classificator.equalsIgnoreCase("lower")) {
      throw new IllegalArgumentException("Classificator must be either " +
          "'greater' or 'lower'");
    }
    this.classificator = classificator.toLowerCase();
  }

  /**
   * Executes the command, replacing a flat in the collection if the
   * specified condition is met.
   *
   * @param args    an array of arguments where the first element is the id
   *                of the flat to be replaced, followed by optional parameters
   *                for the new flat.
   * 
   * @param context the context containing the collection of flats to be
   *                processed.
   * 
   * @return a message indicating the result of the execution. If no
   *         arguments are provided, it returns "No arguments provided.".
   *         If the id format is invalid, it returns "Invalid id format.".
   *         If the collection cannot be parsed, it returns "Collection is
   *         corrupted or not found in context.". If the flat with the
   *         specified id does not exist, it returns "No element with id
   *         {id}". If the flat is successfully replaced, it returns a
   *         success message; otherwise, it indicates that the condition
   *         for replacement was not met.
   */
  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length == 0)
      return "No arguments provided.";

    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "Invalid id format.";
    }

    HashMap<Integer, Flat> collection;
    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
    } catch (ClassCastException e) {
      return "Collection is corrupted or not found in context.";
    }

    if (!collection.containsKey(id))
      return "No element with id " + id;

    Flat oldFlat = collection.get(id);
    Flat updatedFlat = buildFlatFromArgs(args, oldFlat);

    if (updatedFlat == null)
      return "Failed to create a new flat from arguments.";

    int result = compareFlats(updatedFlat, oldFlat);
    boolean shouldReplace = classificator.equals("greater") ? result > 0 : result < 0;

    if (shouldReplace) {
      collection.put(id, updatedFlat);
      return "Flat with id " + id + " was replaced.";
    } else {
      return "Flat was not replaced: condition not met.";
    }
  }

  /**
   * Builds a new Flat object from the provided arguments, using the
   * existing flat's properties as defaults.
   *
   * @param args    an array of arguments containing the new flat's properties.
   * @param oldFlat the existing flat to use as a reference for default values.
   * @return a new Flat object or null if the creation fails.
   */
  private Flat buildFlatFromArgs(String[] args, Flat oldFlat) {
    HashMap<String, String> params = new HashMap<>();
    for (String arg : args) {
      String[] parts = arg.split("=", 2);

      if (parts.length == 2)
        params.put(parts[0], parts[1]);
    }

    try {
      String name = params.getOrDefault("name", oldFlat.getName());

      Coordinates coordinates = oldFlat.getCoordinates();
      if (params.containsKey("x") || params.containsKey("y")) {
        int x = params.containsKey("x") ? Integer.parseInt(params.get("x")) : coordinates.getX();
        double y = params.containsKey("y") ? Double.parseDouble(params.get("y")) : coordinates.getY();
        coordinates = new Coordinates(x, y);
      }

      Double area = params.containsKey("area") ? Double.parseDouble(params.get("area")) : oldFlat.getArea();
      int numberOfRooms = params.containsKey("numberOfRooms") ? Integer.parseInt(params.get("numberOfRooms"))
          : oldFlat.getNumberOfRooms();
      Furnish furnish = params.containsKey("furnish") ? Furnish.valueOf(params.get("furnish").toUpperCase())
          : oldFlat.getFurnish();
      View view = params.containsKey("view") ? View.valueOf(params.get("view").toUpperCase()) : oldFlat.getView();
      Transport transport = params.containsKey("transport") ? Transport.valueOf(params.get("transport").toUpperCase())
          : oldFlat.getTransport();

      House house = oldFlat.getHouse();
      if (params.containsKey("name") || params.containsKey("year") || params.containsKey("floors")) {
        String houseName = params.containsKey("name") ? params.get("name") : house.getName();
        int year = params.containsKey("year") ? Integer.parseInt(params.get("year")) : house.getYear();
        long floors = params.containsKey("houseFloors") ? Long.parseLong(params.get("houseFloors"))
            : house.getNumberOfFloors();
        house = new House(houseName, year, floors);
      }

      return new Flat(
          oldFlat.getId(),
          name,
          coordinates,
          new Date(),
          area,
          numberOfRooms,
          furnish,
          view,
          transport,
          house);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Compares two Flat objects based on various attributes such as area,
   * number of rooms, furnish type, view, transport, and house details.
   *
   * @param f1 the first flat to compare.
   * @param f2 the second flat to compare.
   * @return a negative integer, zero, or a positive integer as the first
   *         flat is less than, equal to, or greater than the second flat.
   */
  private int compareFlats(Flat f1, Flat f2) {
    int result = safeCompare(f1.getArea(), f2.getArea());
    if (result != 0)
      return result;

    result = Integer.compare(f1.getNumberOfRooms(), f2.getNumberOfRooms());
    if (result != 0)
      return result;

    result = safeCompare(f1.getFurnish(), f2.getFurnish());
    if (result != 0)
      return result;

    result = safeCompare(f1.getView(), f2.getView());
    if (result != 0)
      return result;

    result = safeCompare(f1.getTransport(), f2.getTransport());
    if (result != 0)
      return result;

    House h1 = f1.getHouse(), h2 = f2.getHouse();
    if (h1 != null && h2 != null) {
      result = Integer.compare(h1.getYear(), h2.getYear());
      if (result != 0)
        return result;

      result = Long.compare(h1.getNumberOfFloors(), h2.getNumberOfFloors());
      if (result != 0)
        return result;
    }

    return 0;
  }

  /**
   * Safely compares two comparable objects, handling null values.
   *
   * @param a   the first object to compare.
   * @param b   the second object to compare.
   * @param <T> the type of the objects being compared, which must extend
   *            Comparable.
   *
   * @return a negative integer, zero, or a positive integer as the first
   *         object is less than, equal to, or greater than the second
   *         object. If both are null, returns 0; if one is null, the
   *         other is considered greater.
   */
  private <T extends Comparable<T>> int safeCompare(T a, T b) {
    if (a == null && b == null)
      return 0;
    if (a == null)
      return -1;
    if (b == null)
      return 1;
    return a.compareTo(b);
  }
}
