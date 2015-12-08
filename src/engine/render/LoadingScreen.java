package engine.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.JFrame;
import engine.menu.Menu;
import fileSystem.ResourceLoader;

/**
 * Displays a loading screen between levels
 */
public class LoadingScreen extends JFrame {
	private static final long serialVersionUID = 6677800395436882671L;
	private Image background;
	public static boolean showCredits = true;

	/**
	 * Displays a loading screen
	 */
	public LoadingScreen() {
		ResourceLoader res = new ResourceLoader();

		setSize(Menu.MENU_WIDTH, Menu.MENU_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		Image credits = res.loadImage("credits.png");
		Image bg = res.loadImage("loading.png");

		if (showCredits) {
			setTitle("Credits");
			background = credits;
			showCredits = false;
		} else {
			setTitle("Loading");
			background = bg;
		}

		LoadingScreenComponent component = new LoadingScreenComponent();
		add(component);

		setVisible(true);
		repaint();
	}

	/**
	 * Displays a loading screen specific to the level to be displayed
	 * 
	 * @param index
	 *            the level that will be displayed
	 */
	public LoadingScreen(int index) {
		ResourceLoader res = new ResourceLoader();

		setSize(Menu.MENU_WIDTH, Menu.MENU_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		if(index==1)
			background = res.loadImage("loading.png");
		else
			background = res.loadImage("loading" + (index) + ".png");
		setTitle("Loading");

		LoadingScreenComponent component = new LoadingScreenComponent();
		add(component);

		setVisible(true);
		repaint();
	}

	private class LoadingScreenComponent extends JComponent {
		private static final long serialVersionUID = -1853505811343390770L;

		/**
		 * Paints the background image of the loading screen
		 */
		public LoadingScreenComponent() {
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, Menu.MENU_WIDTH, Menu.MENU_HEIGHT,
					this);

		}
	}
}
