package fileSystem;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Loads resources from the source folder "res"
 */
public class ResourceLoader {
	/**
	 * Loads an image from a given file name
	 * 
	 * @param filename
	 *            the file to load the image from
	 * @return the loaded image
	 */
	public Image loadImage(String filename) {
		URL url = getClass().getResource(filename);
		if (url == null)
			url = getClass().getResource("/" + filename);
		return (new ImageIcon(url)).getImage();
	}

	/**
	 * Loads an InputStream from a given file name
	 * 
	 * @param filename
	 *            the file to load the InputStream from
	 * @return the loaded InputStream
	 */
	public InputStream loadFile(String filename) {
		InputStream stream = this.getClass().getClassLoader()
				.getResourceAsStream(filename);
		if (stream == null)
			stream = this.getClass().getClassLoader()
					.getResourceAsStream("/" + filename);
		return stream;
	}

	/**
	 * Loads a URL from a given file name
	 * 
	 * @param filename
	 *            the file to load the URL from
	 * @return the loaded URL
	 */
	public URL loadURL(String filename) {
		URL url = getClass().getResource(filename);
		if (url == null)
			url = getClass().getResource("/" + filename);
		return url;
	}

}
