import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.Scanner;

public class CLIInterpreter {
    private Path currentDirectory;
    public CLIInterpreter(){
        this.currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    //prints the working directory
    public void pwd(){
        System.out.println(currentDirectory.toAbsolutePath().toString());
    }

    //changes the directory
    public void cd(String path){
        Path newPath = currentDirectory.resolve(path).normalize();
        if(Files.isDirectory(newPath)){
            currentDirectory=newPath;
        }
        else{
            System.out.println("Invalid directory: "+ path);
        }
    }

    //make directory
    public void mkdir(String dirName){
        Path newDir=currentDirectory.resolve(dirName);
        try{
            Files.createDirectory(newDir);
        }
        catch(IOException e){
            System.out.println("Failed to create directory: "+e.getMessage());
        }
    }

    //removes directory only if empty
    public void rmdir(String dirName){
        Path newDir=currentDirectory.resolve(dirName);
        try{
            Files.deleteIfExists(newDir);
        }
        catch(IOException e){
            System.out.println("Failed to remove directory: "+e.getMessage());
        }
    }

    //lists the contents of the current directory sorted alphabetically
    public void ls() {
        File files = currentDirectory.toFile();
        File[] filesList = files.listFiles();
        Arrays.sort(filesList, Comparator.comparing(File::getName));
        for (File file : filesList) {
            System.out.print("-" + file.getName() + " ");
        }
        System.out.println();
    }

    //same as ls but in reverse order
    public void ls_r() {
        File[] files = currentDirectory.toFile().listFiles();
        if(files!=null){
            Arrays.sort(files, Collections.reverseOrder(Comparator.comparing(File::getName)));
            for (File file : files) {
                System.out.print("-" + file.getName() + " ");
            }
        }
        System.out.println();
    }

    //display all entries even entries starting with '.'
    public void ls_a(){
        File[] files = currentDirectory.toFile().listFiles();
        if(files!=null){
            for (File file : files) {
                System.out.print("-" + file.getName() + " ");
            }
        }
        System.out.println();
    }

    //moves one or more files/directory to a directory
    public void mv(String source, String destination) {
        try {
            if (Files.isDirectory(Paths.get(this.currentDirectory.toString(), destination))) {
                Files.move(Paths.get(this.currentDirectory.toString(), source), Paths.get(this.currentDirectory.toString(), destination, source));
            } else {
                Files.move(Paths.get(this.currentDirectory.toString(), source), Paths.get(this.currentDirectory.toString(), destination));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //creates a file with each given name
    public void touch(String fileName){
        Path filePath = currentDirectory.resolve(fileName);
        try{
            Files.createFile(filePath);
        }
        catch(IOException e){
            System.out.println("Failed to create file: "+e.getMessage());
        }
    }

    //removes each given file
    public void rm(String fileName){
        Path filePath = currentDirectory.resolve(fileName);
        try{
            Files.deleteIfExists(filePath);
        }
        catch(IOException e){
            System.out.println("Failed to delete file: "+e.getMessage());
        }
    }

    //concatenates the content of the file and prints it
    public void cat(String fileName){
        Path filePath = currentDirectory.resolve(fileName);
        try(Stream<String> lines = Files.lines(filePath)){
            lines.forEach(System.out::println);
        }
        catch(IOException e){
            System.out.println("Error reading file: "+e.getMessage());
        }
    }

    //writes to a file, overwriting existing content
    public void writeToFile(String fileName,String content){
        Path filePath = currentDirectory.resolve(fileName);
        try(BufferedWriter writer=Files.newBufferedWriter(filePath)){
            writer.write(content);
        }
        catch(IOException e){
            System.out.println("Error writing to file: "+e.getMessage());
        }
    }

    //appends to a file
    public void appendToFile(String fileName, String content){
        Path filePath = currentDirectory.resolve(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(filePath,StandardOpenOption.APPEND)){
            writer.write(content);
        }
        catch(IOException e){
            System.out.println("Error appending to file: "+e.getMessage());

        }
    }

    public void help() {
        System.out.println("cat: Concatenates the content of the files and prints it.");
        System.out.println("cd: Changes the current directory");
        System.out.println("clear: Clears the current terminal screen");
        System.out.println("ls: Lists the contents (files & directories) of the current directory sorted alphabetically.");
        System.out.println("ls-a: display all contents even entries starting with.");
        System.out.println("ls-r: reverse order");
        System.out.println("mkdir: Creates a directory with each given name");
        System.out.println("mv: Moves one or more files/directories to a directory.");
        System.out.println("pwd: Prints the working directory");
        System.out.println("rm: Removes each given file.");
        System.out.println("rmdir: Removes each given directory only if it is empty");
        System.out.println("touch: Creates a file with each given name");
    }

    public void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void more(boolean sorted) {
        File files = new File(currentDirectory.toString());
        File[] filesList = files.listFiles();
        if (sorted) {
            Arrays.sort(filesList, Comparator.comparing(File::getName));
        } else {
            Arrays.sort(filesList, Collections.reverseOrder(Comparator.comparing(File::getName)));
        }
        if (filesList.length == 0) {
            System.out.println("File is Empty");
        } else {
            int i = 0;
            for (;i < filesList.length && i < 2; i++) {
                System.out.println(filesList[i].getName());
            }
            Scanner input = new Scanner(System.in);
            String command = "";
            while (!command.equals("q")) {
                System.out.print("--More--");
                command = input.nextLine().trim().toLowerCase();
                clear();
                i++;
                if (i > filesList.length) i = filesList.length;
                for (int j = 0; j < i; ++j) {
                    System.out.println(filesList[j].getName());
                }
            }
        }
    }

    public void less(boolean sorted) {
        File files = new File(currentDirectory.toString());
        File[] filesList = files.listFiles();

        if (sorted) {
            Arrays.sort(filesList, Comparator.comparing(File::getName));
        } else {
            Arrays.sort(filesList, Collections.reverseOrder(Comparator.comparing(File::getName)));
        }

        if (filesList.length == 0) {
            System.out.println("File is Empty");
        } else {
            int i = 0;
            for (;i < filesList.length && i < 2; i++) {
                System.out.println(filesList[i].getName());
            }
            Scanner input = new Scanner(System.in);
            String command = "";
            while (!command.equals("q")) {
                System.out.print(":");
                command = input.nextLine().trim().toLowerCase();
                clear();
                if (command.equals("w")) {
                    i++;
                    if (i > filesList.length) i = filesList.length;
                    for (int j = 0; j < i; ++j) {
                        System.out.println(filesList[j].getName());
                    }
                } else if (command.equals("s")) {
                    i--;
                    if (i < 2) i = 2;
                    for (int j = 0; j < i; ++j) {
                        System.out.println(filesList[j].getName());
                    }
                }
            }
        }
    }

}
