package itmo.lab5.cli.commands;

import itmo.lab5.models.*;
import itmo.lab5.models.enums.*;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.CommandContext;

import java.util.*;

public class UpdateCommand implements Command {
  private final Scanner scanner = new Scanner(System.in);

  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length < 1)
      return "Usage: update id {element}";

    int idToUpdate;
    try {
      idToUpdate = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      return "Invalid ID!";
    }

    Map<Integer, Flat> collection = (Map<Integer, Flat>) context.get("collection");
    if (!collection.containsKey(idToUpdate)) {
      return "No flat with such ID: " + idToUpdate;
    }

    Flat oldFlat = collection.get(idToUpdate);
    Flat updatedFlat = readFlat(idToUpdate, oldFlat);
    collection.put(idToUpdate, updatedFlat);
    return "Flat with ID " + idToUpdate + " updated.";
  }

  private Flat readFlat(int id, Flat oldFlat) {
    System.out.println("- New information for Flat #" + id);
    String name = readString("Enter new name: ", false, oldFlat.getName());
    Coordinates coordinates = readCoordinates(oldFlat.getCoordinates());
    Date creationDate = new Date();
    Double area = readNumber("Enter new square: ", 0.0, 626.0, Double::parseDouble, oldFlat.getArea());
    int rooms = readNumber("Enter new rooms count: ", 1, Integer.MAX_VALUE, Integer::parseInt,
        oldFlat.getNumberOfRooms());
    Furnish furnish = readEnum("Enter new furnish: ", Furnish.class, oldFlat.getFurnish());
    View view = readEnumNullable("Enter new view (or empty string)", View.class, oldFlat.getView());
    Transport transport = readEnum("Enter new transport: ", Transport.class, oldFlat.getTransport());
    House house = readHouse(oldFlat.getHouse());

    return new Flat(id, name, coordinates, creationDate, area, rooms, furnish, view, transport, house);
  }

  private Coordinates readCoordinates(Coordinates oldCoords) {
    int x = readNumber("Введите координату X", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer::parseInt,
        oldCoords.getX());
    Long y = readNumber("Введите координату Y", Long.MIN_VALUE, Long.MAX_VALUE, Long::parseLong, oldCoords.getY());
    return new Coordinates(x, y);
  }

  private House readHouse(House oldHouse) {
    System.out.print("Введите название дома (" + (oldHouse != null ? oldHouse.getName() : "null")
        + ", пусто — не изменять, полностью пусто — null): ");
    String name = scanner.nextLine().trim();
    if (name.isEmpty())
      return oldHouse;
    if (name.equals("null"))
      return null;

    int year = readNumber("Введите год постройки", 1, 959, Integer::parseInt, oldHouse.getYear());
    long floors = readNumber("Введите количество этажей", 1L, 77L, Long::parseLong, oldHouse.getNumberOfFloors());

    return new House(name, year, floors);
  }

  private String readString(String message, boolean allowEmpty, String oldValue) {
    while (true) {
      System.out.print(message + " (" + oldValue + "): ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        return oldValue;
      if (!input.isEmpty() || allowEmpty)
        return input;
      System.out.println("Строка не может быть пустой.");
    }
  }

  private <T extends Enum<T>> T readEnum(String message, Class<T> enumClass, T oldValue) {
    while (true) {
      System.out.println(message + " (" + oldValue + ", варианты: "
          + String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList()) + "):");
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        return oldValue;
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.println("Неверное значение. Повторите ввод.");
      }
    }
  }

  private <T extends Enum<T>> T readEnumNullable(String message, Class<T> enumClass, T oldValue) {
    while (true) {
      System.out.println(message + " (" + (oldValue != null ? oldValue : "null") + ", варианты: "
          + String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList()) + "):");
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        return oldValue;
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.println("Неверное значение. Повторите ввод.");
      }
    }
  }

  private <T extends Comparable<T>> T readNumber(String message, T min, T max, Parser<T> parser, T oldValue) {
    while (true) {
      System.out.print(message + " (" + oldValue + "): ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        return oldValue;
      try {
        T value = parser.parse(input);
        if (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)
          return value;
      } catch (Exception ignored) {
      }
      System.out.println("Некорректный ввод. Повторите попытку.");
    }
  }

  interface Parser<T> {
    T parse(String input) throws Exception;
  }
}
