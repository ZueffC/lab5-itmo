package itmo.lab5.cli.commands;

import java.util.*;

import itmo.lab5.cli.helpers.*;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.enums.*;
import itmo.lab5.models.*;

/**
 * This class implements the Command interface and provides
 * functionality to update an existing Flat object in the collection based on 
 * its ID.
 * 
 * When executed, this command retrieves the flat with the specified ID from 
 * the collection, prompts the user for new values to update the flat's 
 * properties, and then saves the updated flat back to the collection.
 * 
 */
public class UpdateCommand implements Command {
    private final Scanner scanner = new Scanner(System.in);
    private final ReaderUtil inputReader = new ReaderUtil(scanner);

    /**
     * Executes the update command, updating the flat with the specified ID.
     *
     * @param args an array of arguments passed to the command, where the first element is expected to be the ID of the flat to update
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation, or an error message if the ID is invalid or the collection cannot be parsed
     */
    @Override
    public String execute(String[] args, CommandContext context) {
        if (args.length < 1)
            return "Usage: update id {element}";

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
            return "There's an error while trying to parse collection.";
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
