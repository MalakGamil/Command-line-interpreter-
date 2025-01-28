import org.example.HelpCommand;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest {

    @Test
    void testHelpCommandOutput() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Execute the help command
            HelpCommand helpCommand = new HelpCommand();
            helpCommand.execute();

            // Convert captured output to string and verify it contains expected text
            String output = outputStream.toString();
            assertTrue(output.contains("Available commands:"), "Help output should contain 'Available commands:'");
            assertTrue(output.contains("pwd             - Print current working directory"), "Help output should contain 'pwd' command description");
            assertTrue(output.contains("cd <dir>       - Change directory to <dir>"), "Help output should contain 'cd' command description");
            assertTrue(output.contains("exit           - Exit the command line interpreter"), "Help output should contain 'exit' command description");

            // Additional assertions can be added for other commands as needed
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }
}
