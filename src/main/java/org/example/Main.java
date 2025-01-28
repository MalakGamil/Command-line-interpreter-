package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CommandLineInterpreter {
    private static Path currentDirectory = Paths.get(System.getProperty("user.dir"));

    public CommandLineInterpreter() {}

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to My CLI. Type 'help' for a list of commands or 'exit' to quit.");

        CommandLineInterpreter interpreter = new CommandLineInterpreter();
        try {
            while (true) {
                System.out.print(currentDirectory + " >> ");
                String command = reader.readLine().trim();
                if (command.equalsIgnoreCase("exit")) {
                    new ExitCommand().execute();
                }


                interpreter.executeCommand(command);
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    public void executeCommand(String command) {
        if (command.contains("|")) {
            new PipeCommand(command, this).execute();
        } else {
            String[] commandParts = command.split(" ");
            List<String> commandList = new ArrayList<>(Arrays.asList(commandParts));
            if (!commandList.contains(">") && !commandList.contains(">>")) {
                String cmd = commandParts[0].toLowerCase();
                switch (cmd) {
                    case "help" -> handleHelp();
                    case "pwd" -> handlePrintWorkingDirectory();
                    case "cd" -> handleChangeDirectory(commandParts);
                    case "ls" -> handleList(commandParts);
                    case "mkdir" -> handleMakeDirectory(commandParts);
                    case "rmdir" -> handleRemoveDirectory(commandParts);
                    case "touch" -> handleTouch(commandParts);
                    case "mv" -> handleMove(commandParts);
                    case "rm" -> handleRemove(commandParts);
                    case "cat" -> handleCat(commandParts);
                    case "echo" -> handleEcho(commandParts);
                    default -> throw new IllegalArgumentException("Unknown command: " + cmd);
                }
            } else {
                handleRedirection(commandList);
            }
        }
    }


    private void handleEcho(String[] parts) {
        new EchoCommand(currentDirectory).execute(parts);
    }

    private void handleRedirection(List<String> commandList) {
        String redirectionOperator = commandList.contains(">") ? ">" : ">>";
        int operatorIndex = commandList.indexOf(redirectionOperator);
        String command = String.join(" ", commandList.subList(0, operatorIndex));
        String fileName = commandList.get(operatorIndex + 1).trim();
        boolean append = redirectionOperator.equals(">>");

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, append))) {
            if (command.startsWith("echo ")) {
                writer.print(command.substring(5));
            } else {
                // Store the current output stream
                PrintStream originalOut = System.out;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(output);
                System.setOut(ps);

                executeCommand(command);
                System.out.flush();

                System.setOut(originalOut);
                writer.print(output.toString());
            }
        } catch (IOException e) {
            System.err.println("Error handling redirection: " + e.getMessage());
        }
    }

    private void handlePrintWorkingDirectory() {
        new PwdCommand(currentDirectory).execute();
    }

    private void handleChangeDirectory(String[] parts) {
        CdCommand cdCommand = new CdCommand(currentDirectory);
        cdCommand.execute(parts);
        currentDirectory = cdCommand.getCurrentDirectory();
    }

    private void handleList(String[] parts) {
        new LsCommand(currentDirectory).execute(parts);
    }

    private void handleMakeDirectory(String[] parts) {
        new MkdirCommand(currentDirectory).execute(parts);
    }

    private void handleRemoveDirectory(String[] parts) {
        new RmdirCommand(currentDirectory).execute(parts);
    }

    private void handleTouch(String[] parts) {
        new TouchCommand(currentDirectory).execute(parts);
    }

    private void handleMove(String[] parts) {
        new MvCommand(currentDirectory).execute(parts);
    }

    private void handleRemove(String[] parts) {
        new RmCommand(currentDirectory).execute(parts);
    }

    private void handleCat(String[] parts) {
        new CatCommand(currentDirectory).execute(parts);
    }

    private void handleHelp() {
        new HelpCommand().execute();
    }
}
