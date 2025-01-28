package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

class CdCommandTest {
    private Path initialDirectory;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        initialDirectory = Paths.get(System.getProperty("user.dir"));
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testChangeToValidDirectory() {
        Path targetDirectory = initialDirectory.resolve("src");
        if (Files.isDirectory(targetDirectory)) {
            CdCommand cdCommand = new CdCommand(initialDirectory);
            cdCommand.execute(new String[]{"cd", "src"});

            assertEquals(targetDirectory, cdCommand.getCurrentDirectory(), "Should change to the 'src' directory.");
            assertTrue(outContent.toString().contains("Changed directory to: " + targetDirectory),
                    "Output should indicate the directory change to 'src'.");
        } else {
            fail("Directory 'src' does not exist for testing.");
        }
    }

    @Test
    void testChangeToParentDirectory() {
        CdCommand cdCommand = new CdCommand(initialDirectory);
        Path parentDirectory = initialDirectory.getParent();

        cdCommand.execute(new String[]{"cd", ".."});

        assertEquals(parentDirectory, cdCommand.getCurrentDirectory(), "Should change to the parent directory.");
        assertTrue(outContent.toString().contains("Changed directory to: " + parentDirectory),
                "Output should indicate the directory change to the parent directory.");
    }

    @Test
    void testChangeToInvalidDirectory() {
        CdCommand cdCommand = new CdCommand(initialDirectory);
        String invalidDirectory = "non_existent_dir";

        cdCommand.execute(new String[]{"cd", invalidDirectory});

        assertEquals(initialDirectory, cdCommand.getCurrentDirectory(),
                "Should remain in the initial directory if target directory does not exist.");
        assertTrue(errContent.toString().contains("No such directory: " + invalidDirectory),
                "Error output should indicate that the directory does not exist.");
    }

    @Test
    void testNoDirectoryArgument() {
        CdCommand cdCommand = new CdCommand(initialDirectory);

        cdCommand.execute(new String[]{"cd"});  // Missing directory argument

        assertEquals(initialDirectory, cdCommand.getCurrentDirectory(),
                "Should remain in the initial directory when no directory argument is given.");
        assertTrue(errContent.toString().contains("Usage: cd <directory>"),
                "Error output should indicate usage instructions when no argument is provided.");
    }
}


