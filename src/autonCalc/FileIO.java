package autonCalc;
//!!!IMPORTANT!!! do NOT upload autons to github if using the files to test with this,

//that is NOT our property and we do NOT have permission.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileIO {
    //
    // instance variables
    //

    private File folder;
    private File[] files;

    //
    // constructors
    //

    public FileIO() {

    }

    //
    // public methods
    //

    public void run() {
        readFiles();

    }

    public void test(String fileName) {
        run();
        System.out.println(requestFileContents(fileName));
    }

    public String requestFileContents(String fileName) {
        Scanner fileIn;
        String fileContents = "";
        for (File tmp : files) {
            if (tmp.getName().toLowerCase() == fileName.toLowerCase()) {
                try {
                    fileIn = new Scanner(tmp);
                    fileContents = fileIn.useDelimiter("\\Z").next();
                    fileIn.close();
                } catch (Exception FileNotFoundException) {
                    System.out.println("file not found!");
                }
            }
        }
        return fileContents;
    }

    //
    // private methods
    //

    private void readFiles() {
        // assign a folder to use as a root directory for autons
        folder = new File("src/resource/autons");
        // make an array of all the files in the folder (note that this ignores
        // subfolders)
        files = folder.listFiles();
    }

    //
    // getters and setters
    //

    public File[] getListOfFiles() {
        return files;
    }
}