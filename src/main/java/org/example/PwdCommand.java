//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.nio.file.Path;

public class PwdCommand {
    private Path currentDirectory;

    public PwdCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute() {
        System.out.println("Current directory: " + this.currentDirectory);
    }
}
