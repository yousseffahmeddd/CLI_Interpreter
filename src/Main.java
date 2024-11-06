import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CLIInterpreter terminal = new CLIInterpreter();
        boolean exit = false;
        while (!exit) {
            System.out.print(":-$ ");

            String command = input.nextLine().trim();
            String temp = command;
            int spaceIndex = command.indexOf(" ");
            String path = "";
            if (spaceIndex != -1) {
                path = command.substring(spaceIndex, command.length()).trim();
                command = command.substring(0, spaceIndex).trim();
            }

            String pipeCommand = "";
            boolean pipe = true;
            int pipeIndex = temp.indexOf("|");

            if (pipeIndex != -1) {
                pipeCommand = temp;
                temp = temp.substring(0, pipeIndex).trim();
                pipeCommand = pipeCommand.substring(pipeIndex + 1, pipeCommand.length()).trim();
            } else {
                pipe = false;
            }

            switch (command) {
                case "pwd":
                    terminal.pwd();
                    break;
                case "cd":
                    terminal.cd(path);
                    break;
                case "ls":
                    if (pipe) {
                        if (pipeCommand.equals("more")) {
                            terminal.more(true, true);
                        } else if (pipeCommand.equals("less")) {
                            terminal.less(true, true);
                        } else {
                            terminal.ls();
                        }
                    } else {
                        terminal.ls();
                    }
                    break;
                case "ls-r":
                    if (pipe) {
                        if (pipeCommand.equals("more")) {
                            terminal.more(false, true);
                        } else if (pipeCommand.equals("less")) {
                            terminal.less(false, true);
                        } else {
                            terminal.ls_r();
                        }
                    } else {
                        terminal.ls_r();
                    }
                    break;
                case "ls-a":
                    if (pipe) {
                        if (pipeCommand.equals("more")) {
                            terminal.more(true, false);
                        } else if (pipeCommand.equals("less")) {
                            terminal.less(true, false);
                        } else {
                            terminal.ls_a();
                        }
                    } else {
                        terminal.ls_a();
                    }
                    break;
                case "mkdir":
                    terminal.mkdir(path);
                    break;
                case "rmdir":
                    terminal.rmdir(path);
                    break;
                case "touch":
                    terminal.touch(path);
                    break;
                case "mv":
                    String path1 = path.substring(0, path.indexOf(" ")).trim();
                    String path2 = path.substring(path.indexOf(" "), path.length()).trim();
                    terminal.mv(path1, path2);
                    break;
                case "rm":
                    terminal.rm(path);
                    break;
                case "cat":

                    terminal.cat(path);
                    break;
                case "clear":
                    terminal.clear();
                    break;
                case "help":
                    terminal.help();
                    break;
                case ">":
                    String fileName = path.substring(0, path.indexOf(" ")).trim();
                    String content = path.substring(path.indexOf(" "), path.length()).trim();
                    terminal.writeToFile(fileName, content);
                    break;
                case ">>":
                    String nameOfFile = path.substring(0, path.indexOf(" ")).trim();
                    String FileAddedContent = path.substring(path.indexOf(" "), path.length()).trim();
                    terminal.appendToFile(nameOfFile, FileAddedContent);
                    break;
                case "q":
                    exit = true;
                    System.out.println("Command Line Has Been Terminated");
                    break;
                default:
                    System.out.println("Invalid Command");
            }
        }
    }
}