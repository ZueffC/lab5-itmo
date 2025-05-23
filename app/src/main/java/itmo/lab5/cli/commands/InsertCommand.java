package itmo.lab5.cli.commands;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.enums.*;
import itmo.lab5.models.*;
import itmo.lab5.cli.helpers.ReaderUtil;

/**
 * This class implements the Command interface and provides
 * functionality to insert a new Flat object into the collection.
 * 
 * This command can be executed in two modes: interactively, where the user is
 * prompted for input, or with command-line arguments, where parameters are
 * passed as key-value pairs. The command gathers all necessary information to
 * create a new flat and adds it to the collection.
 * 
 */
public class InsertCommand implements Command {
    private final Scanner scanner = new Scanner(System.in);
    private final ReaderUtil inputReader = new ReaderUtil(scanner);

    private static final String description = "command allows to insert data by passing it in k=v format or interactively";

    /**
     * Public function that returns class representation as a string
     *
     * @return string with description
     */
    public final String toString() {
        return this.description;
    }

    /**
     * Executes the insert command, either interactively or with provided
     * arguments.
     *
     * @param args    an array of arguments passed to the command; if empty, the
     *                command will execute interactively
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation, or an error message
     *         if the collection cannot be parsed
     */
    @Override
    public String execute(String[] args, CommandContext context) {
        if (args.length == 0)
            return executeInteractive(context);

        return executeWithArgs(args, context);
    }

    /**
     * Executes the insert command interactively, prompting the user for input
     * to create a new flat.
     *
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation
     */
    private String executeInteractive(CommandContext context) {
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
        while (transport == null || transport.name() == "NONE") {
            System.out.println("\t Transport can't be empty!");
            transport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);
        }

        // Reading House values from terminal
        // Strange situation: by the task field House in the Flat class can be null
        // btw, the fields of House can't be null. So it seems like House can't be null
        // anyway
        House house = null;
        System.out.println("- House");

        String houseName = inputReader.promptString("\t Enter house name: ", false, null);
        int year = inputReader.promptNumber("\t Enter house year: ", 1, 959, Integer::parseInt, null);
        long floors = inputReader.promptNumber("\t Enter house numberOfFloors: ", 1L, 77L, Long::parseLong, null);

        house = new House(houseName, year, floors);

        try {
            HashMap<Integer, Flat> collection = (HashMap<Integer, Flat>) context.get("collection");
            int newID = collection.size() + 1;
            Flat flat = new Flat(
                    newID,
                    name,
                    coordinates,
                    creationDate,
                    area,
                    numberOfRooms,
                    furnish,
                    view,
                    transport,
                    house);

            collection.put(newID, flat);
        } catch (ClassCastException e) {
            return "There's an error while trying to add new element. Collection is broken.";
        }

        return "New flat was successfully inserted!";
    }

    /**
     * Executes the insert command with provided arguments, creating a new flat
     * based on the key-value pairs.
     *
     * @param args    an array of arguments passed to the command
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation
     */
    private String executeWithArgs(String[] args, CommandContext context) {
        HashMap<String, String> params = new HashMap<>();

        for (String arg : args) {
            String[] parts = arg.split("=", 2);

            if (parts.length == 2)
                params.put(parts[0], parts[1]);
        }

        var creationDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String name = params.containsKey("name") ? params.get("name")
                : inputReader.promptString("- Enter name: ", false, null);

        System.out.println("- Coordinates ");
        int x = params.containsKey("x") ? Integer.parseInt(params.get("x"))
                : inputReader.promptNumber("\t Enter x: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer::parseInt,
                        null);

        Double y = params.containsKey("y") ? Double.parseDouble(params.get("y"))
                : inputReader.promptNumber("\t Enter y: ", Double.MIN_VALUE, Double.MAX_VALUE, Double::parseDouble,
                        null);
        var coordinates = new Coordinates(x, y);

        Double area = params.containsKey("area") ? Double.parseDouble(params.get("area").replace(',', '.'))
                : inputReader.promptNumber("\t Enter area: ", 0.0000000001, 626.0, Double::parseDouble, null);
        int numberOfRooms = params.containsKey("numberOfRooms") ? Integer.parseInt(params.get("numberOfRooms"))
                : inputReader.promptNumber("\t Enter numberOfRooms: ", 1, 77, Integer::parseInt, null);

        System.out.println("- Furnish");
        Furnish furnish = null;
        if (params.containsKey("furnish")) {
            String value = params.get("furnish").toUpperCase();
            while (!ReaderUtil.isValidEnumValue(Furnish.class, value)) {
                System.out.println("Invalid furnish value: " + value + ". Please enter a valid furnish type.");
                var t_furnish = inputReader.promptEnum("\t Enter furnish type: ", Furnish.class, null);
                value = t_furnish.toString();
            }
            furnish = Furnish.valueOf(value);
        } else {
            furnish = inputReader.promptEnum("\t Enter furnish type: ", Furnish.class, null);
        }

        System.out.println("- View");
        View view = null;
        if (params.containsKey("view")) {
            String value = params.get("view").toUpperCase();
            while (!ReaderUtil.isValidEnumValue(View.class, value)) {
                System.out.println("Invalid view value: " + value + ". Please enter a valid view type.");
                var t_view = (View) inputReader.promptEnum("\t Enter furnish type: ", View.class, null);
                value = t_view.toString();
            }

            view = View.valueOf(value);
        } else {
            view = inputReader.promptEnumNullable("\t Enter view type: ", View.class, null);
        }

        System.out.println("- Transport");
        Transport transport = null;
        if (params.containsKey("transport")) {
            String value = params.get("transport").toUpperCase();
            while (!ReaderUtil.isValidEnumValue(Transport.class, value)) {
                System.out.println("Invalid transport value: " + value + ". Please enter a valid transport type.");
                var t_trapsport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);
                value = t_trapsport.toString();
            }

            transport = Transport.valueOf(value);
        } else {
            transport = inputReader.promptEnum("\t Enter transport type: ", Transport.class, null);
        }

        System.out.println("- House");
        String houseName = params.getOrDefault("houseName", "");
        while (houseName.isEmpty()) {
            System.out.print("\t Enter house name: ");
            houseName = scanner.nextLine().trim();
        }

        House house = null;

        int year = params.containsKey("houseYear") ? Integer.parseInt(params.get("houseYear"))
                : inputReader.promptNumber("\t Enter house year: ", 1, 959, Integer::parseInt, null);
        long floors = params.containsKey("houseFloors") ? Long.parseLong(params.get("houseFloors"))
                : inputReader.promptNumber("\t Enter house numberOfFloors: ", 1L, 77L, Long::parseLong, null);
        house = new House(houseName, year, floors);

        try {
            HashMap<Integer, Flat> collection = (HashMap<Integer, Flat>) context.get("collection");
            int newID = collection.size() + 1;
            Flat flat = new Flat(
                    newID,
                    name,
                    coordinates,
                    creationDate,
                    area,
                    numberOfRooms,
                    furnish,
                    view,
                    transport,
                    house);

            collection.put(newID, flat);

        } catch (ClassCastException e) {
            return "There's an error while trying to add new element. Collection is broken.";
        }

        return "New flat was successfully inserted!";
    }
}
