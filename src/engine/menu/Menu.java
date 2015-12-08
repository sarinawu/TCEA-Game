package engine.menu;

import Main.Main;
import fileSystem.ResourceLoader;
import fileSystem.SaveLoader;
import game.GameEngine;
import game.Runner;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Creates the Menu of the game, which shows the levels of the game,
 * instructions, options, save, load, and quit actions.
 */
public class Menu extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 6223947377671003851L;
	public static final int MENU_WIDTH = 800;
	public static final int MENU_HEIGHT = 600;
	private Image background;
	public Menu thisMenu;
	public Runner runner;
	public GameEngine gameEngine;
	private String textUp;
	private String textLeft;
	private String textRight;
	private String textRestart;
	private String textPause;
	private boolean waitingKeyUp = false;
	private boolean waitingKeyLeft = false;
	private boolean waitingKeyRight = false;
	private boolean waitingKeyRestart = false;
	private boolean waitingKeyPause = false;
	private ArrayList<JButton> saves;
	private ArrayList<String> saveNames;
	private Map<String, JComponent> components = new TreeMap<String, JComponent>();
	private JComponent currentComponent;
	/**
	 * Default constructor of the Menu, which creates the background,
	 * instantiates the gameEngine and Runner, and creates all the components
	 * used within the Menu as well as sound files to be played.
	 */
	public Menu() {
		// background
		ResourceLoader res = new ResourceLoader();
		background = res.loadImage("colored_castle.png");
		// create a Runner & gameEngine for setting keyBindings
		runner = new Runner();
		gameEngine = runner.getGameEngine();
		// get the component that is displayed; initially is the mainMenu
		saves = new ArrayList<JButton>();
		saveNames = new ArrayList<String>();
		JComponent mainMenu = new MainMenuComponent(this);
		components.put("main", mainMenu);
		components.put("instructions", new InstructionsMenuComponent(this));
		components.put("seasons", new SeasonsMenuComponent(this));
		components.put("options", new OptionsMenuComponent(this));
		components.put("saves", new SavePanel(this));
		components.put("story", new StoryComponent(this));

		// dealing with currentComponent
		currentComponent = mainMenu;
		add(currentComponent);
		// for keybindings
		addKeyListener(this);
		textUp = KeyEvent.getKeyText(gameEngine.getKeyBindingUp());
		textLeft = KeyEvent.getKeyText(gameEngine.getKeyBindingLeft());
		textRight = KeyEvent.getKeyText(gameEngine.getKeyBindingRight());
		textRestart = KeyEvent.getKeyText(gameEngine.getKeyBindingRestart());
		// other stuff
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setResizable(false);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		thisMenu = this;
		Main.mainSound.loop();
	}

	/**
	 * Constructor of the Menu, which creates the background, instantiates the
	 * gameEngine and Runner, and creates all the components used within the
	 * Menu as well as sound files to be played. Takes in number for the
	 * maxLevel.
	 * 
	 * @param i
	 *            the given levelMax number
	 */
	public Menu(int i) {
		// background
		ResourceLoader res = new ResourceLoader();
		background = res.loadImage("colored_castle.png");
		// create a Runner & gameEngine for setting keyBindings
		runner = new Runner(i);
		gameEngine = runner.getGameEngine();
		// get the component that is displayed; initially is the mainMenu
		saves = new ArrayList<JButton>();
		saveNames = new ArrayList<String>();
		JComponent mainMenu = new MainMenuComponent(this);
		components.put("main", mainMenu);
		components.put("instructions", new InstructionsMenuComponent(this));
		components.put("seasons", new SeasonsMenuComponent(this));
		components.put("options", new OptionsMenuComponent(this));
		components.put("saves", new SavePanel(this));
		components.put("story", new StoryComponent(this));

		// dealing with currentComponent
		currentComponent = mainMenu;
		add(currentComponent);
		// for keybindings
		addKeyListener(this);
		textUp = KeyEvent.getKeyText(gameEngine.getKeyBindingUp());
		textLeft = KeyEvent.getKeyText(gameEngine.getKeyBindingLeft());
		textRight = KeyEvent.getKeyText(gameEngine.getKeyBindingRight());
		textRestart = KeyEvent.getKeyText(gameEngine.getKeyBindingRestart());
		// other stuff
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setResizable(false);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		thisMenu = this;
		Main.mainSound.loop();

	}

	/**
	 * Updates any buttons keys changed in the Options Menu
	 */
	public void updateButtons() {
		((OptionsMenuComponent) components.get("options")).updateButtons();
	}

	/**
	 * Repaints the Menu
	 */
	public void repaintMenu() {
		this.repaint();
	}

	/**
	 * Creates the MainMenu with buttons for instructions, options, save, load,
	 * clear files, and quit.
	 */
	private class MainMenuComponent extends JComponent {
		private static final long serialVersionUID = 1097985956843961859L;

		/**
		 * Creates the MainMenu with buttons for instructions, options, save,
		 * load, clear files, and quit.
		 * 
		 * @param al
		 *            Actionlistener for the buttons
		 */
		public MainMenuComponent(ActionListener al) {
			JLabel words = new JLabel("snail tale");
			JButton play = new JButton("play");
			ArrayList<JButton> buttons = new ArrayList<JButton>();
			buttons.add(new JButton("load from file"));
			buttons.add(new JButton("clear saves"));
			buttons.add(new JButton("instructions"));
			buttons.add(new JButton("options"));
			buttons.add(new JButton("quit"));

			// listeners
			words.setFont(new Font("Courier New", Font.PLAIN, 40));
			play.addActionListener(al);
			play.setFont(new Font("Courier New", Font.PLAIN, 60));

			// layouts
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			words.setAlignmentX(CENTER_ALIGNMENT);
			play.setAlignmentX(Component.CENTER_ALIGNMENT);

			// adding buttons
			add(words);
			add(Box.createRigidArea(new Dimension(5, 100)));
			add(play);
			add(Box.createRigidArea(new Dimension(5, 70)));

			for (JButton b : buttons) {
				b.addActionListener(al);
				b.setFont(new Font("Courier New", Font.PLAIN, 30));
				b.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(b);
			}
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);

		}
	}

	/**
	 * Creates the save menu for Menu
	 */
	private class SavePanel extends JPanel {
		private static final long serialVersionUID = 1250536334820105210L;

		/**
		 * Creates the save menu for Menu
		 * 
		 * @param al
		 *            Actionlistener for buttons in SavePanel
		 */
		public SavePanel(ActionListener al) {
			saves.clear();
			saveNames.clear();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JLabel saveTitle = new JLabel("saves");
			saveTitle.setFont(new Font("Courier New", Font.PLAIN, 40));
			saveTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(saveTitle);
			add(Box.createRigidArea(new Dimension(5, 80)));
			for (int i = 1; i <= SaveLoader.load().size(); i++) {
				SaveLoader save = SaveLoader.load().get(i - 1);
				JButton b = new JButton("");
				if (save.getLevelIndex() >= 0)
					b.setText("save " + i + " | level: " + save.getLevelIndex());
				else
					b.setText("save " + i + " | empty");
				saveNames.add(b.getText());
				b.setFont(new Font("Courier New", Font.PLAIN, 30));
				b.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(b);
				add(Box.createRigidArea(new Dimension(5, 20)));
				b.addActionListener(al);
				saves.add(b);
			}
			add(Box.createRigidArea(new Dimension(5, 65)));
			JButton menuButton = new JButton("return to main menu");
			menuButton.setFont(new Font("Courier New", Font.PLAIN, 40));
			menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			menuButton.addActionListener(al);
			add(menuButton);
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);

		}
	}

	/**
	 * Creates the Instructions menu for Menu
	 */
	private class InstructionsMenuComponent extends JComponent {
		private static final long serialVersionUID = 7054304696504895034L;

		/**
		 * Creates the Instructions menu for Menu
		 * 
		 * @param al
		 *            Actionlistener for buttons in InstructionsMenuComponent
		 */
		public InstructionsMenuComponent(ActionListener al) {

			JLabel words1 = new JLabel("you are a snail.");
			ArrayList<JLabel> words = new ArrayList<JLabel>();
			words.add(new JLabel("your current home is too hot and dry."));
			words.add(new JLabel("find a new home using your"));
			words.add(new JLabel("arrow keys and some common sense."));
			words.add(new JLabel(" "));
			words.add(new JLabel("hint: you can restart and pause."));
			words.add(new JLabel("go to options to change keybindings."));
			JButton returnToMain = new JButton("return to main menu");
			// set fonts
			words1.setFont(new Font("Courier New", Font.BOLD, 60));
			returnToMain.setFont(new Font("Courier New", Font.PLAIN, 40));
			// listeners
			returnToMain.addActionListener(al);
			// layouts
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			words1.setAlignmentX(Component.CENTER_ALIGNMENT);
			returnToMain.setAlignmentX(Component.CENTER_ALIGNMENT);
			// adding words
			add(Box.createRigidArea(new Dimension(5, 20)));
			add(words1);
			add(Box.createRigidArea(new Dimension(5, 10)));
			for (JLabel label : words) {
				label.setFont(new Font("Courier New", Font.PLAIN, 30));
				label.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(label);
			}
			add(Box.createRigidArea(new Dimension(5, 210)));
			add(returnToMain);
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			// handling background
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);
		}

	}

	/**
	 * Allows changing of key bindings on actions within the game.
	 */
	private class OptionsMenuComponent extends JComponent {
		private static final long serialVersionUID = 508977251137407893L;
		private JPanel jpanel;
		ArrayList<JComponent> myComponents = new ArrayList<JComponent>();
		ArrayList<JButton> myButtons = new ArrayList<JButton>();
		ArrayList<JLabel> words = new ArrayList<JLabel>();
		ActionListener actionListener;
		JLabel options;
		JLabel warning = new JLabel("");
		JButton returnToMain;

		/**
		 * Constructor for OptionsMenuComponent. Allows changing of key bindings
		 * on actions within the game. Will emit an error sound if same key is
		 * binded on 2 or more actions.
		 * 
		 * @param al
		 *            Actionlistener for buttons in OptionsMenuComponent
		 */
		public OptionsMenuComponent(ActionListener al) {
			// instantiate
			actionListener = al;
			options = new JLabel("options");
			words.add(new JLabel("click on a movement, then"));
			words.add(new JLabel("press desired key on your keyboard"));
			words.add(new JLabel("to bind key to movement"));
			returnToMain = new JButton("return to main menu");
			// adding to the jpanel

			jpanel = new JPanel();
			jpanel.setLayout(new GridLayout(2, 5, 5, 5));
			// manipulating things within jpanel
			// being able to change keybindings
			myComponents.add(new JLabel("move left:", SwingConstants.CENTER));
			myComponents.add(new JLabel("jump:", SwingConstants.CENTER));
			myComponents.add(new JLabel("move right:", SwingConstants.CENTER));
			myComponents.add(new JLabel("restart:", SwingConstants.CENTER));
			myComponents.add(new JLabel("pause:", SwingConstants.CENTER));
			for (JComponent c : myComponents) {
				c.setFont(new Font("Courier New", Font.PLAIN, 20));
				jpanel.add(c);
			}
			updateButtons();
			jpanel.setMaximumSize(new Dimension(700, 150));
			jpanel.setBackground(new Color(212, 237, 246));
			// fonts
			options.setFont(new Font("Courier New", Font.PLAIN, 40));
			returnToMain.setFont(new Font("Courier New", Font.PLAIN, 40));
			// listeners
			returnToMain.addActionListener(actionListener);
			this.addKeyListener(thisMenu);
			// layouts
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			options.setAlignmentX(Component.CENTER_ALIGNMENT);
			returnToMain.setAlignmentX(Component.CENTER_ALIGNMENT);

			// adding words
			add(options);
			add(Box.createRigidArea(new Dimension(5, 20)));
			add(jpanel);
			add(Box.createRigidArea(new Dimension(5, 195)));
			for (JLabel w : words) {
				w.setFont(new Font("Courier New", Font.PLAIN, 30));
				w.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(w);
			}
			add(returnToMain);
			pack();
			setVisible(true);
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			// handling background
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);
		}

		/**
		 * Updates the key binding on actions in the game and changes it in
		 * buttons on OptionsMenuComponent
		 */
		public void updateButtons() {
			for (JButton b : myButtons) {
				jpanel.remove(b);
			}
			myButtons.clear();
			textLeft = KeyEvent.getKeyText(gameEngine.getKeyBindingLeft());
			textUp = KeyEvent.getKeyText(gameEngine.getKeyBindingUp());
			textRight = KeyEvent.getKeyText(gameEngine.getKeyBindingRight());
			textRestart = KeyEvent
					.getKeyText(gameEngine.getKeyBindingRestart());
			textPause = KeyEvent.getKeyText(gameEngine.getKeyBindingPause());
			myButtons.add(new JButton(textLeft)); // 0
			myButtons.add(new JButton(textUp)); // 1
			myButtons.add(new JButton(textRight)); // 2
			myButtons.add(new JButton(textRestart)); // 3
			myButtons.add(new JButton(textPause)); // 4
			for (JButton b : myButtons) {
				b.setFont(new Font("Courier New", Font.PLAIN, 20));
				jpanel.add(b);
				b.addActionListener(actionListener);
			}
			this.revalidate();
			this.repaint();
		}

		/**
		 * Gets the ArrayList of buttons
		 * 
		 * @return the ArrayList of buttons
		 */
		public ArrayList<JButton> getBindingButtons() {
			return myButtons;
		}

		/**
		 * Shows when multiple actions are using the same key
		 * 
		 * @param b
		 *            whether multiple actions are bound to the same key
		 */
		public void displayMultipleBindText(boolean b) {
			removeAll();
			setSize(MENU_WIDTH, MENU_HEIGHT);
			if (b) {
				warning = new JLabel(
						"warning: multiple actions bound to same key!");
				warning.setFont(new Font("Courier New", Font.PLAIN, 20));
				warning.setForeground(Color.RED);
				warning.setAlignmentX(Component.CENTER_ALIGNMENT);
			}

			add(options);
			add(Box.createRigidArea(new Dimension(5, 20)));
			add(jpanel);
			if (b) {
				add(warning);
			}
			if (b)
				add(Box.createRigidArea(new Dimension(5, 172)));
			else
				add(Box.createRigidArea(new Dimension(5, 195)));
			for (JLabel w : words) {
				add(w);
			}
			add(returnToMain);
			this.revalidate();
			this.repaint();
		}
	}

	/**
	 * Shows the levels of the game. Doesn't allow levels to be clicked if the
	 * level has not been played yet.
	 */
	private class SeasonsMenuComponent extends JComponent {
		private static final long serialVersionUID = 4653396635291976250L;

		/**
		 * Shows the levels of the game. Doesn't allow levels to be clicked if
		 * the level has not been played yet.
		 * 
		 * @param al
		 *            the ActionListener for the buttons
		 */
		public SeasonsMenuComponent(ActionListener al) {
			JLabel words = new JLabel("select a season");
			ArrayList<JButton> buttons = new ArrayList<JButton>();
			JButton returnToMain = new JButton("return to main menu");
			for (int i = 1; i <= 12; i++) {
				buttons.add(new JButton("Level " + i));
			}

			if (runner.getLevelMAX() > 0) {
				for (int i = runner.getLevelMAX(); i < 12; i++) {
					buttons.get(i).setEnabled(false);
				}
			}

			words.setFont(new Font("Courier New", Font.PLAIN, 40));
			returnToMain.setFont(new Font("Courier New", Font.PLAIN, 40));
			returnToMain.addActionListener(al);
			for (int i = 0; i < buttons.size(); i++) {
				JButton b = buttons.get(i);
				b.setFont(new Font("Courier New", Font.PLAIN, 20));
				b.addActionListener(al);
				switch (i % 3) {
				case 0:
					b.setAlignmentX(Component.RIGHT_ALIGNMENT);
					// b.setAlignmentY(Component.BOTTOM_ALIGNMENT);
					break;
				case 1:
					b.setAlignmentX(Component.CENTER_ALIGNMENT);
					// b.setAlignmentY(Component.BOTTOM_ALIGNMENT);
					break;
				case 2:
					b.setAlignmentX(Component.LEFT_ALIGNMENT);
					// b.setAlignmentY(Component.BOTTOM_ALIGNMENT);
					break;
				}
				if (i < 3)
					b.setBackground(new Color(225, 225, 99));
				else if (i < 6)
					b.setBackground(new Color(218, 43, 0));
				else if (i < 9)
					b.setBackground(new Color(186, 228, 233));
				else
					b.setBackground(new Color(20, 153, 9));
			}

			// layouts
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			words.setAlignmentX(Component.CENTER_ALIGNMENT);
			returnToMain.setAlignmentX(Component.CENTER_ALIGNMENT);
			returnToMain.setAlignmentY(Component.BOTTOM_ALIGNMENT);

			// adding buttons/labels
			add(words);
			for (JButton b : buttons) {
				add(Box.createRigidArea(new Dimension(5, 5)));
				add(b);
			}

			add(Box.createRigidArea(new Dimension(5, 5)));
			add(returnToMain);
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);
		}
	}

	/**
	 * Shows the background story of our game
	 */
	private class StoryComponent extends JComponent {
		private static final long serialVersionUID = -5456380463730626452L;

		/**
		 * Shows the background story of our game
		 * 
		 * @param al
		 *            the ActionListener for the button
		 */
		public StoryComponent(ActionListener al) {
			JButton startStory = new JButton("begin game?");

			startStory.setFont(new Font("Courier New", Font.PLAIN, 40));

			// listeners
			startStory.addActionListener(al);

			// layouts
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			startStory.setAlignmentX(Component.CENTER_ALIGNMENT);

			// adding words
			add(Box.createRigidArea(new Dimension(5, 510)));
			add(startStory);
		}

		/**
		 * Paints the background image
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		@Override
		protected void paintComponent(Graphics g) {
			// handling background
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(background, 0, 0, MENU_WIDTH, MENU_HEIGHT,
					Color.WHITE, null);
		}

	}

	/**
	 * Tells every single button in this giant class what to do
	 * 
	 * @param e
	 *            the ActionEvent generated by a button press
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ResourceLoader res = new ResourceLoader();
		// remove the currently displayed component
		remove(currentComponent);

		// find the button that was pressed
		String buttonPressed = e.getActionCommand();
		for (int i = 0; i < saveNames.size(); i++) {
			if (saveNames.get(i).equals(buttonPressed)) {
				int level = SaveLoader.load().get(i).getLevelIndex();
				if (level >= 0) {
					this.setVisible(false);
					Main.mainSound.stop();
					runner.showLevel(level - 1);
					this.dispose();
				}
			}
		}
		if (buttonPressed.matches("Level.*")) {
			if (buttonPressed.equals("Level 1")) {
				Main.mainSound.stop();
				currentComponent = components.get("story");
				background = res.loadImage("beginStory.png");
				setTitle("Story");
			} else {
				this.setVisible(false);
				Main.mainSound.stop();
				runner.showLevel(Integer.parseInt(buttonPressed.split(" ")[1]) - 1);
				this.dispose();
			}
		} else
			switch (buttonPressed) {
			case "load from file":
				setTitle("Load");
				currentComponent = components.get("saves");
				break;
			case "clear saves":
				Object[] opt = { "Ok", "Cancel" };
				int o = JOptionPane.showOptionDialog(null,
						"Are you sure you want to clear all saves?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
						null, opt, opt[0]);
				if (o == 0) {
					SaveLoader.clearSaves();
					components.put("saves", new SavePanel(this));
					revalidate();
					repaint();
				}
				break;
			case "play":
				currentComponent = components.get("seasons");
				setTitle("Levels");
				background = res.loadImage("colored_talltrees.png");
				break;
			case "instructions": // switches to a component that displays
									// instructions
				currentComponent = components.get("instructions");
				background = res.loadImage("summerbg.png");
				setTitle("Instructions");
				break;
			case "return to main menu": // switch back to mainMenu
				boolean stuck = hasMultipleBinds();
				if (stuck) {
					JOptionPane
							.showMessageDialog(null,
									"You have multiple actions bound to the same key.");
				} else {
					currentComponent = components.get("main");
					background = res.loadImage("colored_castle.png");
					setTitle("Main Menu");
				}
				break;
			case "return to seasons": // switch back to mainMenu
				boolean stuck2 = hasMultipleBinds();
				if (stuck2) {
					JOptionPane
							.showMessageDialog(null,
									"You have multiple actions bound to the same key.");
				} else {
					currentComponent = components.get("seasons");
					background = res.loadImage("colored_talltrees.png");
					setTitle("Main Menu");
				}
				break;
			case "options": // switch to optionsMenu
				currentComponent = components.get("options");
				setTitle("Options");
				background = res.loadImage("colored_forest.png");
				break;
			case "begin game?" + "":
				this.setVisible(false);
				runner.setGameFrameVisible(true);
				runner.startGameEngineTimer();
				break;
			case "quit": // quit the game
				Object[] options = { "Quit", "Cancel" };
				int option = JOptionPane.showOptionDialog(null,
						"Are you sure you want to quit?", "Warning",

						JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,

						null, options, options[0]);
				if (option == 0) {
					Main.mainSound.stop();
					setVisible(false);
					dispose();
				}
				break;
			default: // for KeyBindings
			{
				ArrayList<JButton> binds = ((OptionsMenuComponent) components.get("options"))
						.getBindingButtons();
				if (binds.get(0).equals(e.getSource())) { // left
					waitingKeyUp = waitingKeyRight = waitingKeyRestart = false;
					waitingKeyLeft = true;
				} else if (binds.get(1).equals(e.getSource())) { // up
					waitingKeyLeft = waitingKeyRight = waitingKeyRestart = false;
					waitingKeyUp = true;
				} else if (binds.get(2).equals(e.getSource())) { // right
					waitingKeyLeft = waitingKeyUp = waitingKeyRestart = false;
					waitingKeyRight = true;
				} else if (binds.get(3).equals(e.getSource())) { // restart
					waitingKeyLeft = waitingKeyUp = waitingKeyRight = false;
					waitingKeyRestart = true;
				} else if (binds.get(4).equals(e.getSource())) { // pause
					waitingKeyLeft = waitingKeyRight = waitingKeyUp = waitingKeyRestart = false;
					waitingKeyPause = true;
				}
			}
				break;
			}
		add(currentComponent);
		// refresh the jframe
		revalidate();
		repaint();
		currentComponent.repaint();
	}

	/**
	 * Unused
	 * 
	 * @param e
	 *            unused
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Changes the key bindings
	 * 
	 * @param e
	 *            the KeyEvent generated by a key press
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (waitingKeyUp) {
			gameEngine.setKeyBindingUp(e.getKeyCode());
		} else if (waitingKeyLeft) {
			gameEngine.setKeyBindingLeft(e.getKeyCode());
		} else if (waitingKeyRight) {
			gameEngine.setKeyBindingRight(e.getKeyCode());
		} else if (waitingKeyRestart) {
			gameEngine.setKeyBindingRestart(e.getKeyCode());
		} else if (waitingKeyPause) {
			gameEngine.setKeyBindingPause(e.getKeyCode());
		}
		waitingKeyUp = waitingKeyLeft = waitingKeyRight = waitingKeyRestart = waitingKeyPause = false;
		// update the buttons
		((OptionsMenuComponent) components.get("options")).updateButtons();
		// check for multiple bindings
		((OptionsMenuComponent) components.get("options"))
				.displayMultipleBindText(hasMultipleBinds());
	}

	/**
	 * Returns whether a key is binded to multiple options
	 * 
	 * @return whether a key is binded multiple times
	 */
	private boolean hasMultipleBinds() {
		int u = gameEngine.getKeyBindingUp();
		int l = gameEngine.getKeyBindingLeft();
		int r = gameEngine.getKeyBindingRight();
		int res = gameEngine.getKeyBindingRestart();
		int p = gameEngine.getKeyBindingPause();
		boolean returning = (u == l || u == r || u == res || u == p || l == r
				|| l == res || l == p || r == res || r == p || res == p);
		return returning;
	}

	/**
	 * Unused
	 * 
	 * @param e
	 *            unused
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

}
