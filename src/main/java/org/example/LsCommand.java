//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LsCommand {
    private Path currentDirectory;

    public LsCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        boolean listAll = false;
        boolean reverse = false;

        // Check for flags
        for (String part : parts) {
            if ("-a".equals(part)) listAll = true;
            if ("-r".equals(part)) reverse = true;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.currentDirectory)) {
            List<String> fileList = new ArrayList<>();

            // Add files to the list based on flags
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                if (listAll || !fileName.startsWith(".")) { // include hidden files if -a
                    fileList.add(fileName);
                }
            }

            // Sort and reverse list if -r is set
            Collections.sort(fileList);
            if (reverse) {
                Collections.reverse(fileList);
            }

            // Print the result
            for (String file : fileList) {
                System.out.println(file);
            }

        } catch (IOException e) {
            System.err.println("ls: Error listing files: " + e.getMessage());
        }
    }
}
