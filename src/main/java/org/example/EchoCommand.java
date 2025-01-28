
package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EchoCommand {
    private Path currentDirectory;

    public EchoCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: echo <text> [> <file> | >> <file>]");
            return;
        }

        StringBuilder text = new StringBuilder();
        String fileName = null;
        boolean isRedirecting = false;
        boolean isAppending = false;

        // تجميع النص واستخراج اسم الملف
        for (int i = 1; i < parts.length; ++i) {
            if (">".equals(parts[i])) {
                isRedirecting = true;
                if (i + 1 >= parts.length) {
                    System.err.println("echo: missing file name after '>'");
                    return;
                }
                fileName = parts[i + 1];
                break;
            } else if (">>".equals(parts[i])) {
                isAppending = true;
                if (i + 1 >= parts.length) {
                    System.err.println("echo: missing file name after '>>'");
                    return;
                }
                fileName = parts[i + 1];
                break;
            } else {
                text.append(parts[i]).append(" ");

            }
        }

        // طباعة النص أو الكتابة إلى الملف
        if (!isRedirecting && !isAppending) {
            System.out.println(text.toString().trim());
            return;
        } else {
            Path outputPath = this.currentDirectory.resolve(fileName.trim());
            try {
                if (isAppending) {
                    Files.writeString(outputPath, text.toString().trim(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                    return;
                } else {
                    Files.writeString(outputPath, text.toString().trim(), StandardOpenOption.CREATE);

                }
                System.out.println("Output written to " + outputPath.getFileName());
                return;
            } catch (IOException e) {
                System.err.println("echo: cannot write to '" + fileName + "': " + e.getMessage());
                return;
            }
        }
    }
}







/*
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EchoCommand {
    private Path currentDirectory;

    public EchoCommand(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public void execute(String[] parts) {
        if (parts.length < 2) {
            System.err.println("Usage: echo <text> [> <file> | >> <file>]");
        } else {
            StringBuilder text = new StringBuilder();
            String fileName = null;
            boolean isRedirecting = false;
            boolean isAppending = false;

            for(int i = 1; i < parts.length; ++i) {
                if (">".equals(parts[i])) {
                    isRedirecting = true;
                    if (i + 1 >= parts.length) {
                        System.err.println("echo: missing file name after '>'");
                        return;
                    }

                    fileName = parts[i + 1];
                    break;
                }

                if (">>".equals(parts[i])) {
                    isAppending = true;
                    if (i + 1 >= parts.length) {
                        System.err.println("echo: missing file name after '>>'");
                        return;
                    }

                    fileName = parts[i + 1];
                    break;
                }

                text.append(parts[i]).append(" ");
            }

            if (!isRedirecting && !isAppending) {
                System.out.println(text.toString().trim());
            } else {
                Path outputPath = this.currentDirectory.resolve(fileName.trim());

                try {
                    if (isAppending) {
                        Files.writeString(outputPath, text.toString().trim(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                    } else {
                        Files.writeString(outputPath, text.toString().trim(), StandardOpenOption.CREATE);
                    }

                    System.out.println("Output written to " + outputPath.getFileName());
                } catch (IOException var8) {
                    IOException e = var8;
                    System.err.println("echo: cannot write to '" + fileName + "': " + e.getMessage());
                }



            }

        }
    }
}
*/