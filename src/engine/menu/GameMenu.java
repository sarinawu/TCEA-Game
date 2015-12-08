package engine.menu;

import fileSystem.ResourceLoader;
import fileSystem.SaveLoader;
import game.GameEngine;
import game.Runner;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Displays a menu while in-game with saving, loading, and quitting options
 */
public class GameMenu extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = -8847851934170090429L;
	public static final int MENU_WIDTH = 800;
	public static final int MENU_HEIGHT = 600;
	private Image background;
	private ArrayList<JButton> inGameButtons = new ArrayList<JButton>();
	private ArrayList<JButton> saves = new ArrayList<JButton>();
	private ArrayList<String> saveNames = new ArrayList<String>();
	private GameMenuPanel panel;
	private SavePanel savePanel;
	private JPanel currentPanel;
	private Runner runner;
	private GameEngine gameEngine;
	private boolean saving;
	private boolean loading;

	/**
	 * Constructs an in-game menu to wait in the background while the game runs
	 * 
	 * @param r
	 *            the Runner currently in control of the game
	 * @param g
	 *            the GameEngine currently in control of the game
	 */
	public GameMenu(Runner r, GameEngine g) {
		ResourceLoader res = new ResourceLoader();
		saving = false;
		loading = false;

		panel = new GameMenuPanel();
		savePanel = new SavePanel();
		currentPanel = panel;
		runner = r;
		gameEngine = g;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// background
		background = res.loadImage("colored_castle.png");

		// layout
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// title
		JLabel words = new JLabel("paused");
		words.setFont(new Font("Courier New", Font.PLAIN, 40));
		words.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(words);
		addKeyListener(this);

		// buttons
		inGameButtons.add(new JButton("save to file"));
		inGameButtons.add(new JButton("load from file"));
		inGameButtons.add(new JButton("clear saves"));
		inGameButtons.add(new JButton("exit to menu"));
		inGameButtons.add(new JButton("exit game"));
		inGameButtons.add(new JButton("resume game"));

		updateSavePanel();

		panel.add(Box.createRigidArea(new Dimension(5, 130)));
		for (JButton b : inGameButtons) {
			b.setFont(new Font("Courier New", Font.PLAIN, 20));
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(b);
			panel.add(Box.createRigidArea(new Dimension(5, 20)));
			b.addActionListener(this);
		}
		// other stuff
		add(currentPanel);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setResizable(false);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle("Game Menu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(false);
		panel.repaint();
		savePanel.repaint();

	}

	/**
	 * Displays the in-game menu
	 */
	public void setGameMenuVisible(boolean b) {
		setVisible(b);
	}

	/**
	 * Repaints the in-game menu
	 */
	public void repaintGameMenu() {
		this.repaint();
	}

	/**
	 * Registers input from buttons
	 * 
	 * @param e
	 *            the input from buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// find the button that was pressed
		String buttonPressed = e.getActionCommand();
		remove(currentPanel);
		for (int i = 0; i < saveNames.size(); i++) {
			if (saveNames.get(i).equals(buttonPressed)) {
				if (saving) {
					if (SaveLoader.load().get(i).getLevelIndex() >= 0) {
						Object[] options = { "Ok", "Cancel" };
						int option = JOptionPane.showOptionDialog(null,
								"Are you sure you want to overwrite this save?", "Warning", JOptionPane.YES_NO_OPTION,
								JOptionPane.ERROR_MESSAGE, null, options, options[0]);
						if (option == 0) {
							SaveLoader.save(new SaveLoader(runner.getLevelIndex()), i);
							updateSavePanel();
							currentPanel = savePanel;
							revalidate();
							repaint();
						}
					} else {
						SaveLoader.save(new SaveLoader(runner.getLevelIndex()), i);
						updateSavePanel();
						currentPanel = savePanel;
						revalidate();
						repaint();
					}
				} else {
					int level = SaveLoader.load().get(i).getLevelIndex();
					if (level >= 0) {
						currentPanel = panel;
						saving = false;
						loading = false;
						revalidate();
						repaint();
						runner.switchToGameFrame();
						runner.showLevel(level - 1);
						gameEngine.pause(false);
					}
				}
			}
		}
		switch (buttonPressed) {
		case "save to file":
			setTitle("Save");
			currentPanel = savePanel;
			saving = true;
			loading = false;
			break;
		case "load from file":
			setTitle("Load");
			currentPanel = savePanel;
			saving = false;
			loading = true;
			break;
		case "clear saves":
			Object[] opt = { "Ok", "Cancel" };
			int o = JOptionPane.showOptionDialog(null, "Are you sure you want to clear all saves?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, opt, opt[0]);
			if (o == 0) {
				SaveLoader.clearSaves();
				updateSavePanel();
				revalidate();
				repaint();
			}
			break;
		case "exit to menu":
			int[] buttons = gameEngine.getBindings();
			dispose();
			Menu menu = new Menu(runner.getLevelMAX());
			menu.gameEngine.setKeyBindingLeft(buttons[0]);
			menu.gameEngine.setKeyBindingUp(buttons[1]);
			menu.gameEngine.setKeyBindingRight(buttons[2]);
			menu.gameEngine.setKeyBindingRestart(buttons[3]);
			menu.gameEngine.setKeyBindingPause(buttons[4]);
			menu.updateButtons();
			break;
		case "exit game":
			Object[] options = { "Quit", "Cancel" };
			int option = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
			if (option == 0) {
				setVisible(false);
				runner.exit();
				dispose();
			}
			break;
		case "resume game":
			runner.switchToGameFrame();
			gameEngine.pause(false);
			break;
		case "back to game menu":
			currentPanel = panel;
			saving = false;
			loading = false;
			break;
		}
		// refresh the jframe
		add(currentPanel);
		revalidate();
		repaint();
	}

	/**
	 * Updates the panel that displays saves
	 */
	public void updateSavePanel() {
		saves.clear();
		saveNames.clear();
		savePanel = new SavePanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));
		JLabel saveTitle = new JLabel("saves");
		saveTitle.setFont(new Font("Courier New", Font.PLAIN, 40));
		saveTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		savePanel.add(saveTitle);
		savePanel.add(Box.createRigidArea(new Dimension(5, 100)));
		for (int i = 1; i <= SaveLoader.load().size(); i++) {
			SaveLoader save = SaveLoader.load().get(i - 1);
			JButton b = new JButton("");
			if (save.getLevelIndex() >= 0)
				b.setText("Save " + i + " | Level: " + save.getLevelIndex());
			else
				b.setText("Save " + i + " | Empty");
			saveNames.add(b.getText());
			b.setFont(new Font("Courier New", Font.PLAIN, 20));
			b.setAlignmentX(Component.CENTER_ALIGNMENT);
			savePanel.add(b);
			savePanel.add(Box.createRigidArea(new Dimension(5, 25)));
			b.addActionListener(this);
			saves.add(b);
		}
		savePanel.add(Box.createRigidArea(new Dimension(5, 70)));
		JButton menuButton = new JButton("back to game menu");
		menuButton.setFont(new Font("Courier New", Font.PLAIN, 20));
		menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuButton.addActionListener(this);
		savePanel.add(menuButton);

	}

	private class SavePanel extends JPanel {
		private static final long serialVersionUID = -8100919530229142358L;

		/**
		 * Constructs a panel that displays saves
		 */
		public SavePanel() {
			super();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT, Color.WHITE, null);

		}
	}

	private class GameMenuPanel extends JPanel {
		private static final long serialVersionUID = 4854100751266363168L;

		/**
		 * Paints the background image of the in-game menu
		 */
		public GameMenuPanel() {
			super();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT, Color.WHITE, null);

		}
	}

	/**
	 * Registers keys pressed and exits the menu when the pause key is pressed
	 * 
	 * @param e
	 *            the key pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == gameEngine.getKeyBindingPause()) {
			if (saving || loading) {
				remove(currentPanel);
				currentPanel = panel;
				saving = false;
				loading = false;
				add(currentPanel);
				repaint();
				revalidate();
			} else {
				runner.switchToGameFrame();
				gameEngine.pause(false);
			}
		}
	}

	/**
	 * Registers keys released
	 * 
	 * @param e
	 *            the key released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Registers keys typed
	 * 
	 * @param e
	 *            the key typed
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
