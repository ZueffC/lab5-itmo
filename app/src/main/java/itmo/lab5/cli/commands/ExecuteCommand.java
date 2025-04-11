package itmo.lab5.cli.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import itmo.lab5.cli.CommandInvoker;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.*;

public class ExecuteCommand implements Command {
  private static final Set<String> executingScripts = new HashSet<String>();

  @Override
  public String execute(String args[], CommandContext context) {
    if (args.length < 1) {
      return "Usage: execute_script <file_name>";
    }

    String fileName = args[0];
    File scriptFile = new File(fileName);

    if (executingScripts.contains(scriptFile.getAbsolutePath())) {
      return "Error: Recursive script execution detected for file: " + fileName;
    }

    executingScripts.add(scriptFile.getAbsolutePath());

    try (Scanner fileScanner = new Scanner(scriptFile)) {
      CommandInvoker commandInvoker = (CommandInvoker) context.get("commandInvoker");
      StringBuilder output = new StringBuilder();

      while (fileScanner.hasNextLine()) {
        String line = fileScanner.nextLine().trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }

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
