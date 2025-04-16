package itmo.lab5.parser;

import itmo.lab5.cli.helpers.FlatComparatorFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import itmo.lab5.models.Flat;

public class Writer {
  static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  public String writeCollection(String filename, HashMap<Integer, Flat> flats) {
    try (FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter writer = new BufferedWriter(osw)) {

      flats = FlatComparatorFactory.sortFlats(
          flats,
          FlatComparatorFactory.SortField.ID);

      for (Flat flat : flats.values()) {
        String date = "";

        try {
          date = dateFormat.format(flat.getCreationDate()).toString();
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }

        String line = String.format("%d,%s,%d,%d,%s,%.5f,%d,%s,%s,%s,%s,%d,%d\n",
            flat.getId(),
            flat.getName(),
            flat.getCoordinates().getX(),
            flat.getCoordinates().getY(),
            date,
            flat.getArea(),
            flat.getNumberOfRooms(),
            flat.getFurnish(),
            flat.getView() != null ? flat.getView() : "",
            flat.getTransport(),
            flat.getHouse() != null ? flat.getHouse().getName() : "",
            flat.getHouse() != null ? flat.getHouse().getYear() : 0,
            flat.getHouse() != null ? flat.getHouse().getNumberOfFloors() : 0);

        writer.write(line);
      }

      return "Collection has been saved to the file successfully.";
    } catch (IOException e) {
      e.printStackTrace();
      return "An error occurred while saving the collection to file.";
    }
  }
}
