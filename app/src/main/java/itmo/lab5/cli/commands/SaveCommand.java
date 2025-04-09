package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.HashMap;

public class SaveCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var collection = new HashMap<Integer, Flat>();
    String filePath = null;

    try {
      collection = (HashMap<Integer, Flat>) context.get("collection");
      filePath = (String) context.get("path");
    } catch (ClassCastException e) {
      return "Can't parse collection!";
    }

    if (filePath != null && collection != null) {
      return saveCollectionToFile(collection, filePath);
    } else {
      return "Can't parse some object! Check filePath!";
    }
  }

  public String saveCollectionToFile(HashMap<Integer, Flat> flats, String filename) {
    try (FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter writer = new BufferedWriter(osw)) {

      writer.write("id,name,x,y,area,numberOfRooms,furnish,view,transport,houseName,houseYear,houseFloors\n");

      for (Flat flat : flats.values()) {
        String line = String.format("%d,%s,%d,%d,%.2f,%d,%s,%s,%s,%s,%d,%d\n",
            flat.getId(),
            flat.getName(),
            flat.getCoordinates().getX(),
            flat.getCoordinates().getY(),
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
