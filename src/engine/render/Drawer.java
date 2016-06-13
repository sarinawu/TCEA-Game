package engine.render;

import fileSystem.ResourceLoader;
import game.gameObjects.moveableObjects.JumpingEnemy;
import game.gameObjects.stationaryObjects.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A static class that reads in map files, creates an image with all stationary
 * platforms. Other classes can use this class to get data on the map and the
 * objects within it.
 */
public class Drawer {
	public static final int Y_OFFSET = 5;
	private static Image staticPic, endGem, p;
	private static HashMap<StationaryPlatform, BufferedImage> preLoad;
	private static ArrayList<DisappearingPlatform> disappearingPlats;
	private static int tWidth, tHeight, numRows, numCols;
	private static EndObject end;
	private static ArrayList<JumpingEnemy> je;

	/**
	 * Takes the background image and draws stationary platforms and endgame
	 * objects.
	 */
	private static void updateStaticPic() {
		// introductory levels
		Graphics g = staticPic.getGraphics();

		// stationary platforms
		for (Entry<StationaryPlatform, BufferedImage> es : preLoad.entrySet()) {
			StationaryPlatform p = es.getKey();
			BufferedImage b = es.getValue();
			g.drawImage(b, p.getCoords()[0], p.getCoords()[1], (int) (tWidth * Screen.PLATFORM_SCALE),
					(int) (tHeight * Screen.PLATFORM_SCALE), null);
		}

		// endgame object
		if (end != null) {
			int[] c = end.getCoords();
			g.drawImage(endGem, c[0], c[1], (int) (tWidth * Screen.PLATFORM_SCALE),
					(int) (tHeight * Screen.PLATFORM_SCALE), null);
		}
	}

	/**
	 * Returns the background image with all necessary components from the
	 * method updateStaticPic()
	 * 
	 * @return the background image
	 */
	public static Image getStaticPic() {
		staticPic = new BufferedImage(4096, 600, BufferedImage.TYPE_INT_ARGB);
		staticPic.getGraphics().drawImage(p, 0, 0, null);
		updateStaticPic();
		return staticPic;
	}

	/**
	 * Gets the current EndObject
	 * 
	 * @return the current EndObject
	 */
	public static EndObject getEndObject() {
		return end;
	}

	/**
	 * Gets the StationaryPlatforms in the current level
	 * 
	 * @return the StationaryPlatforms in the current level
	 */
	public static Set<StationaryPlatform> getPlats() {
		return preLoad.keySet();
	}

	/**
	 * Gets the JumpingEnemies in the current level
	 * 
	 * @return the JumpingEnemies in the current level
	 */
	public static ArrayList<JumpingEnemy> getJumpingEnemies() {
		ArrayList<JumpingEnemy> temp = new ArrayList<JumpingEnemy>();
		for (JumpingEnemy t : je) {
			temp.add(t.clone());
		}
		return temp;
	}

	/**
	 * Gets the DisappearingPlatforms in the current level
	 * 
	 * @return the DisappearingPlatforms in the current level
	 */
	public static ArrayList<DisappearingPlatform> getDisappearingPlats() {
		return disappearingPlats;
	}

	/**
	 * Returns the height of the ground, or the coordinate of the top surface of
	 * the bottom row of tiles
	 * 
	 * @return the height of the ground
	 */
	public static int heightOfGround() {
		return (int) ((numRows - 1) * tHeight * Screen.PLATFORM_SCALE);
	}

	/**
	 * Returns the width of the map, in pixels, that is being used based on the
	 * .tmx file
	 * 
	 * @return the width of the map
	 */
	public static int widthOfMap() {
		return (int) ((numCols) * tWidth * Screen.PLATFORM_SCALE);
	}

	/**
	 * Given a .tmx file, this method will read in tile placements and enemy
	 * placements. Instantiates all class variables except for staticPic
	 * 
	 * @param fileName
	 *            the name of the .tmx file
	 */
	public static void readImageData(String fileName) {

		try {
			ResourceLoader res = new ResourceLoader();

			ArrayList<StationaryPlatform> plats = new ArrayList<StationaryPlatform>();
			disappearingPlats = new ArrayList<DisappearingPlatform>();

			// document
			DocumentBuilder d = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			// Document doc = d.parse(res.loadFile(fileName));
			Document doc = d.parse(res.loadFile(fileName));
			XPath path = XPathFactory.newInstance().newXPath();

			// values for map
			String tileSetName = path.evaluate("/map/tileset/image/@source", doc);
			tWidth = Integer.parseInt(path.evaluate("/map/tileset/@tilewidth", doc));
			tHeight = Integer.parseInt(path.evaluate("/map/tileset/@tileheight", doc));
			int row = Integer.parseInt(path.evaluate("/map/@height", doc));
			int col = Integer.parseInt(path.evaluate("/map/@width", doc));
			int spikeGid = -1, disappearGid = -1;
			int numSpecials = Integer.parseInt(path.evaluate("count(/map/tileset/tile)", doc));
			for (int i = 1; i <= numSpecials; i++) {
				String property = path.evaluate("/map/tileset/tile[" + i + "]/properties/property/@name", doc);
				int whichTile = Integer.parseInt(path.evaluate("/map/tileset/tile[" + i + "]/@id", doc)) + 1;
				if (property.equals("Spike"))
					spikeGid = whichTile;
				else if (property.equals("Disappear"))
					disappearGid = whichTile;
			}

			BufferedImage tileSet = ImageIO.read(res.loadFile(tileSetName));

			int index = 1;
			for (int c = 0; c < row; c++) {
				for (int r = 0; r < col; r++) {
					int gid = Integer.parseInt(path.evaluate("/map/layer[@name=\"Stationary\"]/data/tile[" + (index)
							+ "]/@gid", doc));
					index++;
					if (gid > 0) {
						if (gid == 49) {
							int gem = EndObject.GENERIC;
							if (fileName.contains("summer"))
								gem = EndObject.SUMMER;
							else if (fileName.contains("winter"))
								gem = EndObject.WINTER;
							else if (fileName.contains("autumn"))
								gem = EndObject.FALL;
							else if (fileName.contains("spring"))
								gem = EndObject.SPRING;
							end = new EndObject((int) (r * tHeight * Screen.PLATFORM_SCALE),
									(int) (c * tWidth * Screen.PLATFORM_SCALE) + Y_OFFSET,
									(int) (tWidth * Screen.PLATFORM_SCALE), (int) (tHeight * Screen.PLATFORM_SCALE),
									gem);
							endGem = res.loadImage("gem_" + gem + ".png");
						} else if (gid == disappearGid) {
							// disappearingplatforms take a picture
							int[] coords = Drawer.getGidCoords(70, 70, gid);
							disappearingPlats.add(new DisappearingPlatform((int) (r * tHeight * Screen.PLATFORM_SCALE),
									(int) (c * tWidth * Screen.PLATFORM_SCALE) + Y_OFFSET,
									(int) (tWidth * Screen.PLATFORM_SCALE), (int) (tHeight * Screen.PLATFORM_SCALE),
									gid, tileSet.getSubimage(coords[0], coords[1], 70, 70)));
						} else
							plats.add(new StationaryPlatform((int) (r * tHeight * Screen.PLATFORM_SCALE), (int) (c
									* tWidth * Screen.PLATFORM_SCALE)
									+ Y_OFFSET, (int) (tWidth * Screen.PLATFORM_SCALE),
									(int) (tHeight * Screen.PLATFORM_SCALE), gid, gid == spikeGid));
					}
				}
			}

			numRows = row;
			numCols = col;

			switch (end.getGem()) {
			case EndObject.SUMMER:
				p = res.loadImage("summerbgtiled.png");
				break;
			case EndObject.WINTER:
				p = res.loadImage("winterbgtiled.png");
				break;
			case EndObject.FALL:
				p = res.loadImage("autumnbgtiled2.png");
				break;
			case EndObject.SPRING:
				p = res.loadImage("springbgtiled.png");
				break;
			default:
				p = res.loadImage("full-background.png");
			}

			je = new ArrayList<JumpingEnemy>();
			int count = Integer.parseInt(path.evaluate("count(/map/objectgroup/object)", doc));
			// System.out.println(count);
			for (int o = 1; o <= count; o++) {
				int enemyX = (int) (Double.parseDouble(path.evaluate("/map/objectgroup/object[" + o + "]/@x", doc)) * Screen.PLATFORM_SCALE);
				int enemyY = (int) (Double.parseDouble(path.evaluate("/map/objectgroup/object[" + o + "]/@y", doc)) * Screen.PLATFORM_SCALE);
				je.add(new JumpingEnemy(enemyX, enemyY, Screen.SLIME_WIDTH, Screen.SLIME_HEIGHT));

			}

			while (tileSet == null)
				;
			preLoad = new HashMap<StationaryPlatform, BufferedImage>();

			for (StationaryPlatform plat : plats) {
				int[] c = Drawer.getGidCoords(70, 70, plat.getGid());
				preLoad.put(plat, tileSet.getSubimage(c[0], c[1], 70, 70));
			}

		} catch (XPathExpressionException | SAXException | ParserConfigurationException | IOException e) {
			System.out.println("Something went wrong while parsing file: " + fileName);
			e.printStackTrace();
		}

	}

	/**
	 * Gets the coordinates of a tile in the tileSet, based on the gid. All
	 * tileSets are in a 7x7 grid.
	 * 
	 * @param tWidth
	 *            the width of the tile
	 * @param tHeight
	 *            the height of the tile
	 * @param gid
	 *            the gid of the tile
	 * @return an array containing the top left coordinates of the tile in the
	 *         tileSet.
	 */
	public static int[] getGidCoords(int tWidth, int tHeight, int gid) {
		gid -= 1;
		int r = gid / 7;
		int c = gid % 7;
		return new int[] { c * tHeight, r * tWidth };
	}
}
