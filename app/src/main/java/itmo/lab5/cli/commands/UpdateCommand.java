package itmo.lab5.cli.commands;

import java.time.ZoneId;
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
    private static final String description = "command allows to update collection's element by provided id in k=v manner";

    public final String toString() {
        return this.description;
    }

    /**
     * Executes the update command, updating the flat with the specified ID.
     *
     * @param args    an array of arguments passed to the command, where the first
     *                element is expected to be the ID of the flat to update
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation, or an error message
     *         if the ID is invalid or the collection cannot be parsed
     */
    @Override
    public String execute(String[] args, CommandContext context) {
        if (args.length < 1)
            return "Can't update element without ID!";

        HashMap<Integer, Flat> collection;
        int id;

        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            return "You provided wrong id!";
        }

        try {
            collection = (HashMap<Integer, Flat>) context.get("collection");
        } catch (ClassCastException e) {
            return "Collection is corrupted or not found in context.";
        }

        if (!collection.containsKey(id))
            return "No element with id " + id;

        if (args.length > 1) {
            var updatedFlat = updateByArgs(args, collection.get(id));
            if (updatedFlat == null)
                return "Failed to update flat from arguments.";

            collection.put(id, updatedFlat);
            return "The id=" + id + " flat was successfully updated!";
        }

        return updateInteractive(context, id);
    }

    private String updateInteractive(CommandContext context, Integer id) {
        var creationDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Reading name from terminal then validating it (fix after first attempt)
        String name = inputReader.promptString("- Enter name: ", false, null);

        // Reading coordinates from terminal then compares it to null (fix after first
        // attempt)
        System.out.println("- Coordinates ");

        int x = inputReader.promptNumber("\t Enter x: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer::parseInt, null);
        Double y = inputReader.promptNumber("\t Enter y: ", Double.MIN_VALUE, Double.MAX_VALUE, Double::parseDouble,
                null);
        var coordinates = new Coordinates(x, y);

        // Reading flat area from terminal then validating it.
        // Also renamed "square" -> "area" after first attempt
        Double area = inputReader.promptNumber("\t Enter area: ", 0.0, 626.0, Double::parseDouble, null);
        int numberOfRooms = inputReader.promptNumber("\t Enter numberOfRooms: ", 1, Integer.MAX_VALUE,
                Integer::parseInt,
                null);

        // Reading FURNISH ENUM value from terminal
        System.out.println("- Furnish (can't be empty)");

        Furnish furnish = inputReader.promptEnum("\t Enter furnish type: ", Furnish.class, null);
        while (furnish == null) {
            System.out.println("\t Furnish can't be empty!");
            furnish = inputReader.promptEnum("\t Enter furnish type: ", Furnish.class, null);
        }

        // Reading View ENUM from terminal, it can be empty
        System.out.println("- View (can be empty)");
        View view = inputReader.promptEnumNullable("\t Enter view type: ", View.class, null);

        // Reading Transport Enum from terminal, it can't be empty
        System.out.println("- Transport (can't be empty)");
        Transport transport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);

        while (transport == null) {
            System.out.println("\t Transport can't be empty!");
            transport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);
        }

        // Reading House values from terminal
        // Strange situation: by the task field House in the Flat class can be null
        // btw, the fields of House can't be null. So it seems like House can't be null
        // anyway
        House house = null;
        System.out.println("- House");

        String houseName = inputReader.promptString("\t Enter House name: ", false, null);
        int year = inputReader.promptNumber("\t Enter house age: ", 1, 959, Integer::parseInt, null);
        long floors = inputReader.promptNumber("\t Enter house floors count: ", 1L, 77L, Long::parseLong, null);

        house = new House(houseName, year, floors);

        try {
            HashMap<Integer, Flat> collection = (HashMap<Integer, Flat>) context.get("collection");
            Flat flat = new Flat(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    area,
                    numberOfRooms,
                    furnish,
                    view,
                    transport,
                    house);

            if (flat == null)
                return "Failed to update flat from arguments.";

            collection.put(id, flat);
        } catch (ClassCastException e) {
            return "There's an error while trying to add new element. Collection is broken.";
        }

        return "New flat was successfully inserted!";
    }

    private Flat updateByArgs(String[] args, Flat oldFlat) {
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
            Transport transport = params.containsKey("transport")
                    ? Transport.valueOf(params.get("transport").toUpperCase())
                    : oldFlat.getTransport();

            House house = oldFlat.getHouse();
            if (params.containsKey("name") || params.containsKey("year") || params.containsKey("floors")) {
                String houseName = params.containsKey("name") ? params.get("name") : house.getName();
                int year = params.containsKey("year") ? Integer.parseInt(params.get("year")) : house.getYear();
                long floors = params.containsKey("houseFloors") ? Long.parseLong(params.get("houseFloors"))
                        : house.getNumberOfFloors();
                house = new House(houseName, year, floors);
            }

            var currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            return new Flat(
                    oldFlat.getId(),
                    name,
                    coordinates,
                    currentDate,
                    area,
                    numberOfRooms,
                    furnish,
                    view,
                    transport,
                    house);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
