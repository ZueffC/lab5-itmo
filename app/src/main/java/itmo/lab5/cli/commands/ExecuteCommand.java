package itmo.lab5.cli.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import itmo.lab5.cli.CommandInvoker;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.*;

/**
 * This class implements the Command} interface and provides
 * functionality to execute a script file containing a series of commands.
 *
 * When executed, this command reads commands from the specified script file and
 * executes them sequentially. It also checks for recursive script execution
 * to prevent infinite loops.
 */
public class ExecuteCommand implements Command {
    private static final Set<String> executingScripts = new HashSet<String>();

    /**
     * Executes the script command, running the commands specified in the given
     * script file.
     *
     * @param args    an array of arguments passed to the command, where the first
     *                element is expected to be the name of the script file
     * @param context the command context that contains the command invoker
     * @return a string containing the output of the executed commands, or an error
     *         message if the script file cannot be found or executed
     */
    @Override
    public String execute(String args[], CommandContext context) {
        if (args.length < 1)
            return "Usage: execute_script <file_name>";

        String fileName = args[0];
        File scriptFile = new File(fileName);

        if (executingScripts.contains(scriptFile.getAbsolutePath()))
            return "Error: Recursive script execution detected for file: " + fileName;

        executingScripts.add(scriptFile.getAbsolutePath());

        try (Scanner fileScanner = new Scanner(scriptFile)) {
            CommandInvoker commandInvoker = (CommandInvoker) context.get("commandInvoker");
            StringBuilder output = new StringBuilder();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                try {
                    String[] parts = line.split(" ", 2);
                    String commandName = parts[0];
                    String[] commandArgs = parts.length > 1 ? parts[1].split(" ") : new String[0];

                    String result = commandInvoker.executeCommand(commandName, commandArgs);
                    output.append("> ").append(line).append("\n").append(result).append("\n");
                } catch (Exception e) {
                    output.append("Error executing command '").append(line).append("': ")
                            .append(e.getMessage()).append("\n");
                }
            }

            return output.toString();
        } catch (FileNotFoundException e) {
            return "Error: Script file not found: " + fileName;
        } finally {
            executingScripts.remove(scriptFile.getAbsolutePath());
        }
    }
}
