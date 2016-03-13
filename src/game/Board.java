/**
 * @file Board.java
 * @author Ethan Davies
 * @date 7 December 2015
 *
 * Contain the Information of the current Board
 * and various helper methods for manipulating
 * the board.
 */

package game;

import java.awt.*;
import java.util.*;

import java.lang.Math;

public class Board {

	private int m_rows;
	private int m_columns;
	private int m_mineCount;
	private boolean m_gameWon = false;
	private boolean m_gameLost = false;
	private ArrayList<ArrayList<Tile>> m_board;
	private Revealer m_reveal;

	/**
	 * Constructor
	 * 
	 * @param bRows
	 *            an int for the number of rows
	 * @param bColumns
	 *            an int for the number of columns
	 * @param numMines
	 *            an int for number of mines
	 */
	public Board(int bRows, int bColumns, int numMines) {
		// Set Class variables
		this.m_mineCount = numMines;
		this.m_rows = bRows;
		this.m_columns = bColumns;

		// Create a revealed tile for the sake of method calls.
		m_reveal = new Revealer(false, false, false);

		// Setup the board.
		setBoardDimensions();
		placeMines();
	}

	public void setBoard(ArrayList<ArrayList<Tile>> board) {
		m_board = board;
	}
	
	/**
	 * @return the value of m_gameLost which is true if the game is lost
	 */
	public boolean getm_GameLost() {
		return m_gameLost;
	}

	/**
	 * @return the board which has all the tile
	 */
	public ArrayList<ArrayList<Tile>> getm_Board() {
		return m_board;
	}

	/**
	 * @return the value of m_gameWon which is true if the game is won
	 */
	public boolean getm_GameWon() {
		return m_gameWon;
	}

	public int getm_Rows() {
		return m_rows;
	}

	public int getm_Columns() {
		return m_columns;
	}

	public int getm_MineCount() {
		return m_mineCount;
	}
	public void setm_MineCount(int mineCount) {
		m_mineCount=mineCount;
	}
	public void setTile(Tile tile, int x, int y) {
		m_board.get(y).set(x, tile);
	}

	/**
	 * Sets the dimensions of the board
	 */
	private void setBoardDimensions() {
		// Create board
		m_board = new ArrayList<ArrayList<Tile>>();

		// Sets the dimensions HxW of the board that is to be created
		for (int i = 0; i < m_rows; i++) {
			m_board.add(new ArrayList<Tile>());

			for (int q = 0; q < m_columns; q++) {
				m_board.get(i).add(new Secret(false, true, false));
			}
		}
	}

	/**
	 * Place the mines onto the board.
	 */
	private void placeMines() {
		// This places the mines in random areas on the board(In the array)
		Random rnd = new Random();
		int mineCount = this.m_mineCount;
		while (mineCount > 0) {
			int row = rnd.nextInt(m_board.size());
			int column = rnd.nextInt(m_board.get(row).size());

			if (m_board.get(row).get(column).m_isMine) {

			} else {
				m_board.get(row).get(column).setTileType(true, true);
				mineCount--;
			}
		}
	}

	/**
	 * This method is used to reveal tiles on the board after a users turn, and
	 * then takes action based on what tile is revealed.
	 * 
	 * @param x
	 *            an int which is the X position of the click
	 * @param y
	 *            an int which is the Y position of the click
	 */
	public void revealTile(int x, int y) {
		// Work out the positions in the Array of the mouse click
		int xPos = (int) Math.floor(x / Tile.WIDTH);
		int yPos = (int) Math.floor(y / Tile.HEIGHT);

		if (inLimit(yPos, xPos) && !(m_board.get(yPos).get(xPos).m_isDefused))
		{

			if (m_board.get(yPos).get(xPos).m_isHidden && 
					!(m_board.get(yPos).get(xPos).m_isMine)) {

				m_reveal.revealPosition(m_board, yPos, xPos);
				haveWon();

			} else if (m_board.get(yPos).get(xPos).m_isMine) {

				this.m_gameWon = false;
				this.m_gameLost = true;
				m_board.get(yPos).remove(xPos); // create a mine tile
				m_board.get(yPos).add(xPos, new Mine(true, false, false,
						"images/mineX.jpg"));
				for (int i = 0; i < m_board.size(); ++i) {
					for (int j = 0; j < m_board.get(0).size(); ++j) {
						if (m_board.get(i).get(j).m_isMine &&
								!(i == yPos && j == xPos)) {
							m_board.get(i).remove(j); // create a mine tile
							m_board.get(i).add(j, new Mine(true, false, false,
									"images/mine.png"));
						}
					}
				}
			}
		}
	}

	/**
	 * This method is used to defuse tiles on the board after a users turn,
	 * storing whether the tile was a mine or not.
	 * 
	 * @param x
	 *            an int which is the X position of the click
	 * @param y
	 *            an int which is the Y position of the click
	 */
	public void defusedTile(int x, int y) {
		// Work out the positions in the Array of the mouse click
		int xPos = (int) Math.floor(x / Tile.WIDTH);
		int yPos = (int) Math.floor(y / Tile.HEIGHT);

		if (inLimit(yPos, xPos)) {

			boolean isMine = m_board.get(yPos).get(xPos).m_isMine;
			boolean isDefused = m_board.get(yPos).get(xPos).m_isDefused;
			boolean isHidden = m_board.get(yPos).get(xPos).m_isHidden;

			if (isHidden) {
				if (!(isDefused)) {
					m_board.get(yPos).remove(xPos);
					m_board.get(yPos).add(xPos, new Flag(isMine,
							true, true));
					haveWon();
				} else {
					m_board.get(yPos).remove(xPos);
					m_board.get(yPos).add(xPos, new Secret(isMine,
							true, false));
				}
			}
		}
	}

	/**
	 * it check if x,y are in the range of the board or not
	 * 
	 * @param x
	 *            an int for the row
	 * @param y
	 *            an int for the column
	 * 
	 * @return true if x, y are in range
	 */
	private boolean inLimit(int x, int y) {
		return !(x >= m_board.size() || y >= m_board.get(0).size() 
				|| x < 0 || y < 0);
	}

	/**
	 * Calls render on each tile on the board.
	 * 
	 * @param g
	 *            a Graphics object for rendering the board.
	 */
	public void render(Graphics g) {
		// This will be responsible for creating the graphics of the board
		for (int y = 0; y < m_board.size(); y++) {
			for (int x = 0; x < m_board.get(y).size(); x++) {
				m_board.get(y).get(x).render(g, x, y);
			}
		}
	}

	/**
	 * Renders the games UI
	 * 
	 * @param g
	 *            a Graphics object for rendering the UI
	 * @param player
	 *            a Player object with the info to be displayed
	 * @param timePassed
	 *            a String with the time that has passed.
	 */
	public void renderInfo(Graphics g, Player player, String timePassed) {
		Font timeNewRoman = new Font("Time new roman", Font.BOLD, 12);

		// Positioning Values.
		int x = 1;
		int y = 10;

		g.setFont(timeNewRoman);
		g.setColor(Color.RED);
		g.drawString("Name : " + player.getUsername(), x, y);

		x = x + 200;

		if (timePassed == null) {
			g.drawString("Time : 00:00:00", x, y);
		} else {
			g.drawString("Time :" + timePassed, x, y);
		}

		x = 1;
		y = 27;
		g.setColor(Color.BLUE);
		g.drawString("Defused Mine : " + getDefusedTile(), x, y);

		x = x + 200;
		g.drawString("Mines Present : " + m_mineCount, x, y);

		y = 48;
		x = 1;
		g.drawString("Hidden Square : " + getHiddenTile(), x, y);

		x = x + 200;
		g.drawString("Revealed Square : " + getRevealedTile(), x, y);
	}

	/**
	 * checks if the game have been won or not
	 */
	private void haveWon() {
		if (getRevealedTile() + getDefusedTile() ==
				m_board.size() * m_board.size()) {
			m_gameLost = false;
			m_gameWon = true;
		}
	}

	/**
	 * @return a int with the number of revealed tiles
	 */
	private int getRevealedTile() {
		int revealedTile = 0;

		for (int i = 0; i < m_board.size(); ++i) {
			for (int j = 0; j < m_board.get(0).size(); ++j) {
				if (!(m_board.get(i).get(j).m_isHidden)) {
					++revealedTile;
				}
			}
		}

		return revealedTile;
	}

	/**
	 * @returna int with the number of hidden tiles
	 */
	private int getHiddenTile() {
		int hiddenTile = 0;

		for (int i = 0; i < m_board.size(); ++i) {
			for (int j = 0; j < m_board.get(0).size(); ++j) {
				if (m_board.get(i).get(j).m_isHidden) {
					++hiddenTile;
				}
			}
		}

		return hiddenTile;
	}

	/**
	 * @return a int with the number of defused tiles
	 */
	private int getDefusedTile() {
		int defusedTile = 0;

		for (int i = 0; i < m_board.size(); ++i) {
			for (int j = 0; j < m_board.get(0).size(); ++j) {
				if (m_board.get(i).get(j).m_isDefused &&
						m_board.get(i).get(j).m_isMine) {
					++defusedTile;
				}
			}
		}

		return defusedTile;
	}

	/**
	 * reset the game so that it can be played again
	 */
	public void reset() {
		m_board.clear();
		setBoardDimensions();
		placeMines();
		m_gameLost = false;
		m_gameWon = false;
	}

	/**
	 * Reveals the mines without ending the game. Used for testing purposes
	 */
	public void toggleMines(boolean minesShown) {
		for (int y = 0; y < m_board.size(); y++) {
			for (int x = 0; x < m_board.get(y).size(); x++) {
				if (m_board.get(y).get(x).isMine()) {
					m_board.get(y).remove(x);
					if (!minesShown) {
						m_board.get(y).add(x, new Mine(true, true, false,
								"images/mine.png"));
					} else {
						m_board.get(y).add(x, new Secret(true, true, false));
					}

				}
			}
		}
	}
}
