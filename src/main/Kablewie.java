/**
 * @file Kablewie.java
 * @author Thomas Phelps
 * @date 4 December 2015
 *
 * Starts the game then creates and
 * calls the other components.
 */

package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import game.Board;
import game.GameController;
import game.Player;

public class Kablewie {

	private JFrame m_frame;
	private final int FRAME_WIDTH = 640;
	private final int FRAME_HEIGHT = 480;
	private final int MAX_X_COORD = 2;
	private final int MAX_Y_COORD = 2;
	private final int SCREEN_WIDTH_DIV = 2;
	private final int SCREEN_HEIGHT_DIV = 2;

	/**
	 * Initialisation method.
	 * 
	 * @param args a String Array of arguments passed from the command line.
	 */
	public static void main(String[] args) {
		// Create a Kablewie instance to escape static scope.
		new Kablewie();
	}

	/**
	 * Method for creating a JFrame for the mainMenu
	 * 
	 * @see MainMenu.java
	 */
	public Kablewie() {
		// Create the frame.
		m_frame = new JFrame("Kablewie");
		m_frame.setIconImage(new ImageIcon("images/Kablewie.png").getImage());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Set frame boundaries.
		m_frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		m_frame.setLocation(
				dim.width / MAX_X_COORD - m_frame.getSize().width / 
				SCREEN_WIDTH_DIV, dim.height / MAX_Y_COORD - 
				m_frame.getSize().height /SCREEN_HEIGHT_DIV);

		// Set window to close when exited.
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Show the frame.
		m_frame.setVisible(true);
		new MainMenu(m_frame, this);
	}

	/**
	 * Simple method for passing board and player information to the game
	 * controller
	 * 
	 * @param board
	 * @param player
	 * 
	 * @see GameController.java
	 */
	public void startGame(Board board, Player player, MainMenu menu) {
		m_frame.getContentPane().removeAll();
		new GameController(board, player, m_frame, menu);
	}

}
