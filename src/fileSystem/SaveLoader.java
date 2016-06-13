package fileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Saves and loads games
 */
public class SaveLoader implements Serializable {

	private static final long serialVersionUID = 8775439875762295413L;
	
	public static int MAX_SAVES = 5;
	private int levelIndex;

	/**
	 * Stores the current level
	 * 
	 * @param index
	 *            the current level
	 */
	public SaveLoader(int index) {
		levelIndex = index;
	}

	/**
	 * Returns the level index stored in the save
	 * 
	 * @return the level stored in the save
	 */
	public int getLevelIndex() {
		return levelIndex;
	}

	/**
	 * Sets the level index stored in the save
	 * 
	 * @param index
	 *            the level index stored in the save
	 */
	public void setLevelIndex(int index) {
		levelIndex = index;
	}

	/**
	 * Returns the level stored in the save as a String
	 * 
	 * @return the level stored in the save in String format
	 */
	public String toString() {
		return "Level " + levelIndex;
	}

	/**
	 * Loads the current list of saves from the file "saves"
	 * 
	 * @return an ArrayList of SaveLoaders containing the saves
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<SaveLoader> load() {
		File file = new File("saves");

		// load in saves from file
		ArrayList<SaveLoader> saves = new ArrayList<SaveLoader>();
		if (file.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(file));
				saves = (ArrayList<SaveLoader>) in.readObject();
				in.close();
				return saves;
			} catch (NullPointerException | ClassNotFoundException
					| IOException e) {
				e.printStackTrace();
			}
		}
		// if file not accessible, write a new file with empty saves
		return clearSaves();
	}

	/**
	 * Stores a save in the file "saves" at a specified index in the current
	 * list of saves
	 * 
	 * @param s
	 *            the save to store
	 * @param index
	 *            the index to store the save at
	 */
	public static void save(SaveLoader s, int index) {
		// load saves
		ArrayList<SaveLoader> saves = SaveLoader.load();
		// add save
		saves.set(index, s);
		ObjectOutputStream out;
		// rewrite saves to file
		try {
			out = new ObjectOutputStream(new FileOutputStream("saves"));
			out.writeObject(saves);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the current list of saves
	 * 
	 * @return the cleared list of saves
	 */
	public static ArrayList<SaveLoader> clearSaves() {
		ArrayList<SaveLoader> saves = new ArrayList<SaveLoader>();
		File file = new File("saves");
		try {
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("saves"));
			for (int i = 0; i < MAX_SAVES; i++) {
				SaveLoader sav = new SaveLoader(-1);
				saves.add(sav);
			}
			out.writeObject(saves);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saves;

	}
}
