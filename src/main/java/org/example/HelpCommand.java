//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

public class HelpCommand {
    public HelpCommand() {
    }

    public void execute() {
        System.out.println("Available commands:");
        System.out.println("  pwd             - Print current working directory");
        System.out.println("  cd <dir>       - Change directory to <dir>");
        System.out.println("  ls [-a] [-r]   - List files in current directory");
        System.out.println("  mkdir <dir>    - Create a new directory");
        System.out.println("  rmdir <dir>    - Remove a directory");
        System.out.println("  touch <file>   - Create a new file or update the timestamp");
        System.out.println("  mv <src> <dest>- Move or rename files/directories");
        System.out.println("  rm <file>      - Remove a file");
        System.out.println("  cat <file>     - Print the content of a file");
        System.out.println("  echo <text> > <file>  - Write text to a file");
        System.out.println("  echo <text> >> <file> - Append text to a file");
        System.out.println(" <command>  | <command> - pipe output of one command into another");
        System.out.println("  help           - Display this help message");
        System.out.println("  exit           - Exit the command line interpreter");
    }
}
