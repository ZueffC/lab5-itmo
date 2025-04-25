package itmo.lab5.cli.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import itmo.lab5.interfaces.*;
import itmo.lab5.cli.*;

public class ExecuteCommand implements Command {
    private static final Deque<String> executingScripts = new ArrayDeque<>();
    private static final String description = "command allows to execute script by provided absolute path";

    public final String toString() {
        return this.description;
    }

    @Override
    public String execute(String[] args, CommandContext context) {
        if (args.length < 1)
            return "Usage: execute_script <file_name>";

        String fileName = args[0];
        File scriptFile = new File(fileName);

        if (executingScripts.contains(scriptFile.getAbsolutePath()))
            return "Error: Cross-script recursion detected for file: " + fileName;

        executingScripts.push(scriptFile.getAbsolutePath());

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
            executingScripts.pop();
        }
    }
}
