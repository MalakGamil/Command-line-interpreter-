package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PipeCommandTest {

    private CommandLineInterpreter interpreter;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        interpreter = new CommandLineInterpreter() {
            @Override
            public void executeCommand(String command) {
                if (command.equals("echo Hello")) {
                    System.out.print("Hello");
                } else if (command.equals("echo World")) {
                    System.out.print("World");
                } else if (command.equals("echo Pipe")) {
                    System.out.print("Pipe");
                } else {
                    throw new IllegalArgumentException("Unknown command: " + command);
                }
            }
        };

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);  // Reset System.out to original
    }

    @Test
    void testSingleCommand() {
        PipeCommand pipeCommand = new PipeCommand("echo Hello", interpreter);
        pipeCommand.execute();
        assertEquals("Hello", outputStream.toString().trim());
    }

    @Test
    void testPipedCommands() {
        PipeCommand pipeCommand = new PipeCommand("echo Hello | echo World", interpreter);
        pipeCommand.execute();
        assertEquals("World", outputStream.toString().trim());
    }

    @Test
    void testEmptyCommand() {
        PipeCommand pipeCommand = new PipeCommand("", interpreter);
        pipeCommand.execute();
        assertEquals("No commands to execute.", outputStream.toString().trim());
    }

//    @Test
//    void testUnknownCommand() {
//        PipeCommand pipeCommand = new PipeCommand("unknown", interpreter);
//        pipeCommand.execute();
//        assertTrue(outputStream.toString().contains("Error handling pipe"));
//    }

    @Test
    void testMultiplePipedCommands() {
        PipeCommand pipeCommand = new PipeCommand("echo Hello | echo Pipe | echo World", interpreter);
        pipeCommand.execute();
        assertEquals("World", outputStream.toString().trim());
    }
}
