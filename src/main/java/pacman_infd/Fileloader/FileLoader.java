

package pacman_infd.Fileloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

/**
 *
 * @author Marinus
 */
class FileLoader {
    
    private final URI path;
    
    FileLoader(URI path){
        this.path = path;
    }
    
    /**
     * Opens a text file and reads all lines and puts then in a String array.
     * @return String[] of all lines in the text file.
     * @throws IOException 
     */
    private String[] openFile() throws IOException {
        
        FileReader fr = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(fr);
        
        int lines = readLines();
        String[] textData = new String[lines];
        
        for(int i = 0; i < lines; i++){
            textData[i] = textReader.readLine();
        }
        
        textReader.close();
        return textData;
        
    }
    
    /**
     * Opens a file using openFile() and parses the string array to a 
     * 2D array of characters. This is used by the gameWorld to populate the gameworld
     * with different GameElements.
     * @return elementMap
     * @throws IOException 
     */
    char[][] openMap() throws IOException {
        String[] textData = openFile();
        
        int mapWidth = textData[0].length();
        int mapHeight = textData.length;
      
        char[][] map = new char[mapHeight][mapWidth];
        
        for(int i = 0; i < mapHeight; i++){
            for(int j = 0; j < mapWidth; j++){
                map[i][j] = textData[i].charAt(j);
            }
        }
        
        return map;        
    }
    
    /**
     * Counts the number of lines in a textFile. This is used to create the
     * String array in openFile() with the correct size.
     * @return number of lines
     * @throws IOException 
     */
    private int readLines() throws IOException {
        FileReader file_to_read = new FileReader(new File(path));
        BufferedReader bf = new BufferedReader(file_to_read);
        
        String line;
        int numberOfLines = 0;
        
        while((line = bf.readLine()) != null){
            numberOfLines++;
        }
        bf.close();
        
        return numberOfLines;
    }
}
