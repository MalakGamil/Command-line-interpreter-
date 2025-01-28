package org.example;

import java.io.*;

public class PipeCommand {
    private final String command;
    private final CommandLineInterpreter interpreter;

    public PipeCommand(String command, CommandLineInterpreter interpreter) {
        this.command = command;
        this.interpreter = interpreter;
    }

    public void execute() {
        // Split the command by pipe characters
        String[] commands = command.split("\\|");

        // Check for empty or whitespace-only command
        if (commands.length == 0 || command.trim().isEmpty()) {
            System.out.println("No commands to execute.");
            return;
        }

        // Save the original System.in and System.out
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            for (int i = 0; i < commands.length; ++i) {
                String cmd = commands[i].trim();

                // Skip empty command parts
                if (cmd.isEmpty()) {
                    System.out.println("No commands to execute.");
                    return;
                }

                // Set the input stream for the next command if this is not the first command
                if (i > 0) {
                    System.setIn(new ByteArrayInputStream(output.toByteArray()));
                }

                output.reset();

                try (ByteArrayOutputStream newOutput = new ByteArrayOutputStream();
                     PrintStream ps = new PrintStream(newOutput)) {
                    // Redirect output to newOutput
                    System.setOut(ps);
                    interpreter.executeCommand(cmd);  // This may throw IllegalArgumentException
                    output = newOutput;  // Capture the output
                } catch (IllegalArgumentException e) {
                    // Handle unknown command error here
                    System.out.println("Error handling pipe: Unknown command: " + cmd);
                    return;  // Exit if there's an error
                }
            }

            // Restore the original output and input streams
            System.setOut(originalOut);
            System.setIn(originalIn);

            // Print the final output from the command execution
            System.out.print(output.toString().trim());
        } catch (Exception e) {
            // Handle any unexpected errors
            System.out.println("Error handling pipe: " + e.getMessage());
        } finally {
            // Ensure original output and input streams are restored
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}
