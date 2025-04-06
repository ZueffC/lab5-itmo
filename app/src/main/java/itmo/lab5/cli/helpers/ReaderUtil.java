package itmo.lab5.cli.helpers;

import java.util.Arrays;
import java.util.Scanner;

public class ReaderUtil {
  private final Scanner scanner;

  public ReaderUtil(Scanner scanner) {
    this.scanner = scanner;
  }

  public String promptString(String message, boolean allowEmpty, String oldValue) {
    while (true) {
      System.out.print(message);
      if (oldValue != null)
        System.out.print(" (" + oldValue + "): ");

      String input = scanner.nextLine().trim();

      if (input.isEmpty())
        return oldValue;
      if (!input.isEmpty() || allowEmpty)
        return input;

      System.out.println("String can't be empty ");
    }
  }

  public <T extends Enum<T>> T promptEnum(String message, Class<T> enumClass, T oldValue) {
    while (true) {
      if (oldValue != null)
        System.out.println("Now it's: " + oldValue + ". ");

      System.out.print(
          message + "(options: " +
              String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList()) +
              "): ");

      String input = scanner.nextLine().trim();

      if (input.isEmpty())
        return oldValue;
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid name; Please, try again: ");
      }

    }
  }

  public <T extends Enum<T>> T promptEnumNullable(String message, Class<T> enumClass, T oldValue) {
    while (true) {
      if (oldValue != null)
        System.out.println("Now it's: " + oldValue + ". ");

      System.out.print(
          message + "(options: " +
              String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList()) +
              "): ");

      String input = scanner.nextLine().trim();

      if (input.isEmpty())
        return oldValue;
      try {
        return Enum.valueOf(enumClass, input);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid name; Please, try again: ");
      }
    }
  }

  public <T extends Comparable<T>> T promptNumber(String message, T min, T max, Parser<T> parser, T oldValue) {
    while (true) {
      System.out.print(message + " (" + oldValue + "): ");
      String input = scanner.nextLine().trim();
      if (input.isEmpty())
        if (oldValue != null)
          return oldValue;

      try {
        T value = parser.parse(input);
        if (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)
          return value;
      } catch (Exception e) {
      }

      System.out.println("Invalid value; Please, try again: ");
    }
  }

  public interface Parser<T> {
    T parse(String input) throws Exception;
  }
}
