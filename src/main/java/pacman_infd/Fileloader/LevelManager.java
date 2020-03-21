/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Fileloader;

import pacman_infd.Fileloader.FileLoader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Marinus
 */
public class LevelManager {

    private Queue<char[][]> levels;

    public LevelManager() {
        levels = new LinkedList();
        loadLevels();
    }

    private void loadLevels() {
        loadLevel("Levels/xlevel1.txt");
        loadLevel("Levels/xlevel2.txt");
        loadLevel("Levels/xlevel3.txt");
    }

    private void loadLevel(String path) {
        try {
            URI filePath = ClassLoader.getSystemResource(path).toURI();
            char[][] level = new FileLoader(filePath).openMap();
            if (level != null) {
                levels.add(level);
            }

        } catch (URISyntaxException | IOException e) {
            System.out.println(e);
        }
    }

    public char[][] getFirstLevel() {
        levels = new LinkedList();
        loadLevels();
        return getNextLevel();
    }

    public char[][] getNextLevel() {

        char[][] nextLevel = levels.poll();
        levels.offer(nextLevel);
        return nextLevel;
    }
}
