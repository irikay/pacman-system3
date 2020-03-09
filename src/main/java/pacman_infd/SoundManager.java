/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Marinus
 */
public class SoundManager {


    private Map<String, File> soundFiles;

    public SoundManager() {
        soundFiles = new HashMap();
        loadSoundFiles();
    }

    private void loadSoundFiles() {
        try {
            soundFiles.put("chomp", new File(ClassLoader.getSystemResource("SFX/wakawaka.wav").toURI()));
            soundFiles.put("cherry", new File(ClassLoader.getSystemResource("SFX/eatCherry.wav").toURI()));
            soundFiles.put("ghost", new File(ClassLoader.getSystemResource("SFX/eatGhost.wav").toURI()));
            soundFiles.put("death", new File(ClassLoader.getSystemResource("SFX/death.wav").toURI()));
            soundFiles.put("win", new File(ClassLoader.getSystemResource("SFX/intermission.wav").toURI()));
            soundFiles.put("portal", new File(ClassLoader.getSystemResource("SFX/portalOpen3 2.wav").toURI()));

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playSound(String sound) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundFiles.get(sound));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
