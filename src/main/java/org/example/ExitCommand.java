package org.example;

public class ExitCommand {
    private final Runnable exitHandler;

    // Default constructor for production use
    public ExitCommand() {
        this.exitHandler = () -> System.exit(0);
    }

    // Constructor for testing to inject a mock exit handler
    public ExitCommand(Runnable exitHandler) {
        this.exitHandler = exitHandler;
    }

    public void execute() {
        System.out.println("Exiting the CLI. Goodbye!");
        exitHandler.run();
    }
}
