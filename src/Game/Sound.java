/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2015
 *
 * Name: Ryan Greene, Jack Napor, Danny Toback, & Richard Huffman
 * Date: Dec 6, 2015
 * Time: 11:10:07 PM
 *
 * Project: csci205FinalProject
 * Package: Game
 * File: Sound
 * Description:
 *
 * ****************************************
 */
package Game;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * http://www.dreamincode.net/forums/topic/343804-how-to-add-background-music-to-my-2d-platformer-game/
 *
 * @author rth013
 */
public class Sound {

    private Clip clip;

    public static Sound song = new Sound("/Sounds/ZombieMusic.wav");
    public static Sound bullet = new Sound("/Sounds/bullet.wav");
    public static Sound powerup = new Sound("/Sounds/powerup.wav");

    public Sound(String path) {
        try {
            URL url = this.getClass().getResource(path);
            AudioInputStream aiStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(aiStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loops a clip in separate thread
     */
    public void loop() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //plays a clip in separate thread
    public void play() {
        try {
            if (clip != null) {
                new Thread() {
                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
