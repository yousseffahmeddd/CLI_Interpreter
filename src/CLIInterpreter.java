import java.io.File;
import java.io.IOException;
import java.nio.file.*;
public class CLIInterpreter {
    private Path currentDirectory;
    public CLIInterpreter(){
        this.currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    //prints the working directory
    public void pwd(){
        System.out.println(currentDirectory.toAbsolutePath().toString());
    }

    // Function to navigate through folders
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

}
