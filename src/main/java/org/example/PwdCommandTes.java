package org.example;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class PwdCommandTest {

    @Test
    void testExecutePrintsCurrentDirectory() {
        Path currentDirectory = Path.of(System.getProperty("user.dir"));
        PwdCommand pwdCommand = new PwdCommand(currentDirectory);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        pwdCommand.execute();

        String expectedOutput = "Current directory: " + currentDirectory + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString(), "The output should contain the current directory path.");

        System.setOut(System.out);  // Reset System.out to default
    }
}
