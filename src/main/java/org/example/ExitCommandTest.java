package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExitCommandTest {

    @Test
    public void testExecuteExitCommand() {
        // Flag to check if the exit handler is called
        final boolean[] exitCalled = {false};

        // Inject a mock exit handler that sets the flag
        ExitCommand exitCommand = new ExitCommand(() -> exitCalled[0] = true);

        // Execute the command
        exitCommand.execute();

        // Assert that the mock exit handler was called
        assertTrue(exitCalled[0], "Exit handler should be called.");
    }
}
