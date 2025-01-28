package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class EchoCommandTest {
    private Path testDirectory;
    private EchoCommand echoCommand;

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary directory for testing
        testDirectory = Files.createTempDirectory("testDir");
        echoCommand = new EchoCommand(testDirectory);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary directory
        Files.walk(testDirectory)
                .map(Path::toFile)
                .forEach(File::delete);
    }
    @Test
    void testEchoPlainText() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        echoCommand.execute(new String[]{"echo", "Hello,", "world!"});

        String actualOutput = outContent.toString().trim();
        String expectedOutput = "Hello, world!";

        assertEquals(expectedOutput, actualOutput, "Output mismatch in plain echo command");
    }


    @Test
    void testRedirectToFile() throws IOException {
        Path testFile = testDirectory.resolve("output.txt");
        echoCommand.execute(new String[]{"echo", "Hello, file!", ">", "output.txt"});

        assertTrue(Files.exists(testFile));
        assertEquals("Hello, file!", Files.readString(testFile).trim());
    }

    @Test
    void testAppendToFile() throws IOException {
        Path testFile = testDirectory.resolve("output.txt");
        Files.writeString(testFile, "Existing content\n");

        echoCommand.execute(new String[]{"echo", "Appended text", ">>", "output.txt"});

        assertEquals("Existing content\nAppended text", Files.readString(testFile).trim());
    }

    @Test
    void testMissingFileNameAfterRedirect() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        echoCommand.execute(new String[]{"echo", "Hello", ">"});

        assertTrue(errContent.toString().contains("echo: missing file name after '>'"));
    }

    @Test
    void testMissingFileNameAfterAppend() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        echoCommand.execute(new String[]{"echo", "Hello", ">>"});

        assertTrue(errContent.toString().contains("echo: missing file name after '>>'"));
    }

    @Test
    void testNoArguments() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        echoCommand.execute(new String[]{"echo"});

        assertTrue(errContent.toString().contains("Usage: echo <text> [> <file> | >> <file>]"));
    }
}