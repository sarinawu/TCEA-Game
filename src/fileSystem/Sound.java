package fileSystem;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Handles play, pause, and looping of sounds for the game.
 * 
 * @author Eric Lee
 */
public class Sound {
	private Clip myClip;

	/**
	 * Takes a sound file and save in a class.
	 * 
	 * @param filename
	 *            wav file name of the sound
	 */
	public Sound(String fileName) {
		try {
			myClip = AudioSystem.getClip();
			java.net.URL url = getClass().getResource(fileName);
			if (url == null)
				url = getClass().getResource("/" + fileName);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);

			myClip.open(ais);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Sound: Malformed URL: " + e);
		} catch (UnsupportedAudioFileException e) {
			throw new RuntimeException("Sound: Unsupported Audio File: " + e);
		} catch (IOException e) {
			throw new RuntimeException("Sound: Input/Output Error: " + e);
		} catch (LineUnavailableException e) {
			throw new RuntimeException("Sound: Line Unavailable: " + e);
		} catch (OutOfMemoryError e) {
			throw new RuntimeException("Out of Memory: " + e);
		}
	}

	/**
	 * Plays the sound once.
	 */
	public void play() {
		myClip.setFramePosition(0); // Must always rewind!
		myClip.loop(0);
		myClip.start();
		if (!myClip.isActive())
			myClip.stop();

	}

	/**
	 * Plays the sound in a loop
	 */
	public void loop() {
		myClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * Stops the sound.
	 */
	public void stop() {
		myClip.stop();
	}
}
