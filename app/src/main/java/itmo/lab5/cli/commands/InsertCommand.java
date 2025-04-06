package itmo.lab5.cli.commands;

import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.enums.*;
import itmo.lab5.models.*;

public class InsertCommand implements Command {
  @Override
  public String execute(String[] args, CommandContext context) {
    if (args.length < 1 || !"null".equals(args[0])) {
      return "Usage: insert null {element}";
    }

    Scanner scanner = new Scanner(System.in);
    Date creationDate = new Date();

    System.out.print("- Enter name: ");
    String name = readNonEmptyString(scanner);

    System.out.println("- Coordinates:");
    System.out.print("Enter x (int): ");
    int x = readInt(scanner);
    System.out.print("Enter y (Long): ");
    Long y = readLongNotNull(scanner);
    Coordinates coordinates = new Coordinates(x, y);

    System.out.print("Enter square (Double > 0, <= 626): ");
    Double area = readDoubleInRange(scanner, 0.0, 626.0);

    System.out.print("Enter room count (int > 0): ");
    int numberOfRooms = readIntMin(scanner, 1);

    System.out.println("- Furnish");
    Furnish furnish = readEnum(scanner, Furnish.class);

    System.out.println("- View");
    View view = readEnumNullable(scanner, View.class);

    System.out.println("- Transport");
    Transport transport = readEnum(scanner, Transport.class);

    System.out.println("- House");
    System.out.print("Enter house's name: ");

    String houseName = scanner.nextLine();
    House house = null;

    if (!houseName.isBlank()) {
      System.out.print("Enter house age (1-959): ");
      int year = readIntInRange(scanner, 1, 959);
      System.out.print("Enter house's floors count (1-77): ");
      long numberOfFloors = readLongInRange(scanner, 1, 77);
      house = new House(houseName, year, numberOfFloors);
    }

    try {
      HashMap<Integer, Flat> collection = (HashMap<Integer, Flat>) context.get("collection");
      var newID = collection.size() + 1;

      Flat flat = new Flat(newID, name, coordinates, creationDate, area,
          numberOfRooms, furnish, view, transport, house);
      collection.put(newID, flat);
    } catch (ClassCastException e) {
      return "There's an error while trying to add new element. Collection im some kind of broken.";
    }

    return "New flat was successfully inserted!";
  }

  private String readNonEmptyString(Scanner scanner) {
    while (true) {
      String line = scanner.nextLine().trim();

      if (!line.isEmpty())
        return line;

      System.out.print("Value can't be empty. Retry input: ");
    }
  }

  private int readInt(Scanner scanner) {
    while (true) {
      try {
        return Integer.parseInt(scanner.nextLine().trim());
      } catch (NumberFormatException e) {
        System.out.print("Please, enter integer: ");
      }
    }
  }

  private Long readLongNotNull(Scanner scanner) {
    while (true) {
      String line = scanner.nextLine().trim();
      if (!line.isEmpty()) {
        try {
          return Long.parseLong(line);
        } catch (NumberFormatException e) {
          System.out.print("Please, enter Long: ");
        }
      } else {
        System.out.print("Field can't be null. Retry: ");
      }
    }
  }

  private int readIntMin(Scanner scanner, int min) {
    while (true) {
      int value = readInt(scanner);
      if (value >= min)
        return value;
      System.out.print("Number must be bigger than " + min + ". Retry: ");
    }
  }

  private int readIntInRange(Scanner scanner, int min, int max) {
    while (true) {
      int value = readInt(scanner);
      if (value >= min && value <= max)
        return value;
      System.out.print("Enter number in range [" + min + " ... " + max + "] ");
    }
  }

  private long readLongInRange(Scanner scanner, long min, long max) {
    while (true) {
      try {
        long value = Long.parseLong(scanner.nextLine().trim());
        if (value >= min && value <= max)
          return value;
        System.out.print("Enter number in range [" + min + " ... " + max + "]");
      } catch (NumberFormatException e) {
        System.out.print("Enter Long number: ");
      }
    }
  }

  private Double readDoubleInRange(Scanner scanner, double min, double max) {
    while (true) {
      try {
        Double value = Double.parseDouble(scanner.nextLine().trim());
        if (value > min && value <= max)
          return value;
        System.out.print("Enter float in range [" + (min + 0.0001) + " ... " + max + "] ");
      } catch (NumberFormatException e) {
        System.out.print("Enter float number: ");
      }
    }
  }

  private <T extends Enum<T>> T readEnum(Scanner scanner, Class<T> enumClass) {
    System.out.println("Allowed values: " + Arrays.toString(enumClass.getEnumConstants()));
    while (true) {
      String input = scanner.nextLine().trim();
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.print("You must enter one of values from the previous list: ");
      }
    }
  }

  private <T extends Enum<T>> T readEnumNullable(Scanner scanner, Class<T> enumClass) {
    System.out
        .println("Allowed values (or empty string): " + Arrays.toString(enumClass.getEnumConstants()));
    while (true) {
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        return null;
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.print("You must enter one of values from the previous list: ");
      }
    }
  }
}
