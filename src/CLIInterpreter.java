import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
            System.out.println("Failed to create directory"+e.getMessage());
        }
    }

    //removes directory only if empty
    public void rmdir(String dirName){
        Path newDir=currentDirectory.resolve(dirName);
        try{
            Files.deleteIfExists(newDir);
        }
        catch(IOException e){
            System.out.println("Failed to remove directory"+e.getMessage());
        }
    }

    public void ls() {
        File files = currentDirectory.toFile();
        File[] filesList = files.listFiles();
        Arrays.sort(filesList, Comparator.comparing(File::getName));
        for (File file : filesList) {
            System.out.print("-" + file.getName() + " ");
        }
        System.out.println();
    }

    public void ls_r() {
        File files = currentDirectory.toFile();
        File[] filesList = files.listFiles();
        Arrays.sort(filesList, Collections.reverseOrder(Comparator.comparing(File::getName)));
        for (File file : filesList) {
            System.out.print("-" + file.getName() + " ");
        }
        System.out.println();
    }

    public void mv(String source, String destination) {
        try {
            Files.move(Paths.get(source), Paths.get(destination));
        } catch (IOException e) {
            System.out.println(e);
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

}
