
/**
 * @author Anthony
 * @date 2026-06-03
 * Description:  class to play sounds.
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundPlayer {

	private static Clip musicClip;

	public static void playSound(String soundFilePath) {
		try {
			File soundFile = new File(soundFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		} catch (Exception e) {
			System.out.println("SFX Error: " + e.getMessage());
		}
	}

	public static void playMusic(String soundFilePath) {
		try {
			stopMusic();

			File soundFile = new File(soundFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

			musicClip = AudioSystem.getClip();
			musicClip.open(audioStream);

			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
			musicClip.start();
		} catch (Exception e) {
			System.out.println("Music Error: " + e.getMessage());
		}
	}

	public static void stopMusic() {
		if (musicClip != null && musicClip.isRunning()) {
			musicClip.stop();
			musicClip.close();
		}
	}
}