package itmo.lab5.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

import itmo.lab5.models.enums.*;
import itmo.lab5.cli.helpers.FlatComparatorFactory;
import itmo.lab5.models.*;

public class Reader {
  static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public HashMap<Integer, Flat> parseCSV(File file) throws FileNotFoundException, IllegalArgumentException {
    var collection = new HashMap<Integer, Flat>();
    var scanner = new Scanner(file);

    if (scanner.hasNextLine()) {
      var currentLine = scanner.nextLine();
      if (!currentLine.contains("id,name"))
        scanner = new Scanner(file);
    }

    while (scanner.hasNextLine()) {
      var currentLine = scanner.nextLine();
      Flat parsedFlat = null;

      try {
        parsedFlat = parseFlat(currentLine);
        collection.put(parsedFlat.getId(), parsedFlat);
      } catch (Exception e) {
        System.out.println(
            "There's an error while trying to parse line: '"
                + currentLine + "'; "
                + "The error is: " + e.getMessage());
      }

    }

    var sortedById = FlatComparatorFactory.sortFlats(
        collection,
        FlatComparatorFactory.SortField.ID);

    scanner.close();
    return sortedById;
  }

  private static Flat parseFlat(String lineToParse) throws IllegalArgumentException, ParseException {
    var values = lineToParse.split(",", -1);
    var flat = new Flat();

    flat.setId(Integer.parseInt(values[0]));
    flat.setName(values[1]);
    flat.setCoordinates(new Coordinates(Integer.parseInt(values[2]), Double.parseDouble(values[3])));
    flat.setCreationDate(LocalDate.parse(values[4], formatter));
    flat.setArea(Double.parseDouble(values[5]));
    flat.setNumberOfRooms(Integer.parseInt(values[6]));
    flat.setFurnish(Furnish.valueOf(values[7]));
    flat.setView(values[8].isEmpty() ? null : View.valueOf(values[8]));
    flat.setTransport(Transport.valueOf(values[9]));

    if (!values[10].isEmpty()) {
      House house = new House(values[10], Integer.parseInt(values[11]), Long.parseLong(values[12]));
      flat.setHouse(house);
    }

    return flat;
  }
}
