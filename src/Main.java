import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        CLIInterpreter termenal = new CLIInterpreter();
        boolean exit = false;
        while (!exit) {
            System.out.print(":-$ ");

            String command = input.nextLine().trim();
            int spaceIndex = command.indexOf(" ");
            String path = "";
            if (spaceIndex != -1) {
                path = command.substring(spaceIndex, command.length()).trim();
                command = command.substring(0, spaceIndex).trim();
            }

            String pipeCommand = "";
            boolean pipe = true;
            int pipeIndex = command.indexOf("|");

            if (pipeIndex != -1) {
                pipeCommand = command;
                command = command.substring(0, pipeIndex).trim();
                pipeCommand = pipeCommand.substring(pipeIndex + 1, pipeCommand.length()).trim();
            } else {
                pipe = false;
            }

            System.out.println(path);
            System.out.println(command);

            switch (command) {
                case "pwd":
                    termenal.pwd();
                    break;
                case "cd":
                    termenal.cd(path);
                    break;
                case "ls":
                    if (pipe) {
                        if (pipeCommand.equals("more")) {
                            termenal.more(true);
                        } else if (pipeCommand.equals("less")) {
                            termenal.less(true);
                        } else {
                            System.out.println("Invalid Pipe Command");
                        }
                    } else {
                        termenal.ls();
                    }
                    break;
                case "ls-r":
                    if (pipe) {
                        if (pipeCommand.equals("more")) {
                            termenal.more(false);
                        } else if (pipeCommand.equals("less")) {
                            termenal.less(false);
                        } else {
                            System.out.println("Invalid Pipe Command");
                        }
                    } else {
                        termenal.ls_r();
                    }
                    break;
                case "ls-a":
                    termenal.ls_a();
                    break;
                case "mkdir":
                    termenal.mkdir(path);
                    break;
                case "rmdir":
                    termenal.rmdir(path);
                    break;
                case "touch":
                    termenal.touch(path);
                    break;
                case "mv":
                    String path1 = path.substring(0, path.indexOf(" ")).trim();
                    String path2 = path.substring(path.indexOf(" "), path.length()).trim();
                    termenal.mv(path1, path2);
                    break;
                case "rm":
                    termenal.rm(path);
                    break;
                case "cat":
                    termenal.cat(path);
                    break;
                case "clear":
                    termenal.clear();
                    break;
                case "help":
                    termenal.help();
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