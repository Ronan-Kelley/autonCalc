package autonCalc;
//!!!IMPORTANT!!! do NOT upload autons to github if using the files to test with this,

//that is NOT our property and we do NOT have permission.

import java.io.File;
import java.util.Scanner;

public class FileIO {
    //
    // instance variables
    //

    private File folder;
    private File[] files;
    private String[] fileNames;

    //
    // constructors
    //

    public FileIO() {
        run();
    }

    //
    // public methods
    //

    public void run() {
        readFiles();
        fileNames = new String[files.length];
        int i = 0;
        for (File tmp : files) {
            fileNames[i] = tmp.getName();
            i++;
        }
    }

    public String requestFileContents(String fileName) {
        Scanner fileIn;
        String fileContents = "";
        for (File tmp : files) {
            if (tmp.getName().toLowerCase().equals(fileName.toLowerCase()) || tmp.getName().toLowerCase().equals(fileName.toLowerCase() + ".java") || tmp.getName().toLowerCase().equals(fileName.toLowerCase() + ".txt") ) {
                //if the requested file exists the exact name given, or a txt or java file exists with the same name, do this stuff
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

        // make an array of all the files in the folder (ignores subfolders)
        files = folder.listFiles();
    }

    //
    // getters and setters
    //

    public File[] getListOfFiles() {
        return files;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}