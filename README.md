# Command Line Interpreter (CLI)

A Java-based Command Line Interpreter (CLI) that supports system commands, piping, and redirection. This tool provides a lightweight alternative for interacting with the file system and executing various commands directly from the terminal.

---

## Features

### Supported Commands:
1. **help**: Displays a list of all available commands and their usage.
2. **pwd**: Prints the current working directory.
3. **cd [directory]**: Changes the current directory.
4. **ls**: Lists the files and directories in the current directory.
5. **mkdir [directory_name]**: Creates a new directory.
6. **rmdir [directory_name]**: Removes an empty directory.
7. **touch [file_name]**: Creates an empty file or updates its timestamp.
8. **mv [source] [destination]**: Moves or renames files or directories.
9. **rm [file_name]**: Deletes a file.
10. **cat [file_name]**: Displays the content of a file.
11. **echo [text]**: Prints the provided text.
12. **exit**: Exits the application.

### Additional Features:
- **Piping (`|`)**: Chain multiple commands and pass the output of one as input to the next.
- **Redirection**:
  - `>`: Redirects command output to a file (overwriting if the file exists).
  - `>>`: Appends command output to a file.

---

## Usage

1. Clone or download the repository.
2. Compile the project using `javac`:
   ```bash
   javac -d bin src/org/example/*.java
