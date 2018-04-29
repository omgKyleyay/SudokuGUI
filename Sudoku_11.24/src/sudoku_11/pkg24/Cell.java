/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * A Sudoku cell as it might be used in a GUI make it a JLabel
 * by extending the JLabel class
 */
/*
 * A Sudoku cell as it might be used in a GUI make it a JLabel
 * by extending the JLabel class
 */
public class Cell extends JLabel implements FocusListener, MouseListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	// the font use for displaying the label
	private static Font labelFont, boldFont;
	// the background for the cell that hasfocus
	private static Color lightYellow = new Color(255, 255, 125);
	
	// at first invocation create our fonts
	static {
		JLabel l = new JLabel();          	// build a temp label to get standard font
		Font font = l.getFont();			// get the Font
		// make a font 2 times the original size (which will be considered bold)
		boldFont = font.deriveFont(font.getStyle(), (int) (font.getSize2D() * 2.0));
		// a smaller font for the cell not part of the original problem
		labelFont = boldFont.deriveFont(boldFont.getStyle() ^ Font.BOLD);	
	}
	// the String representation of the point 
	private String str;
	// the value of the cell
	private int value;
	// if the value is part of the original problem
	private boolean originalProblem;
	// the cell position
	private Point point;
	
	// an ArrayList to register the SudokuCellKbListener that want to be informed when a
	// a the keyboard is press
	private ArrayList<SudokuCellKbListener> listenerList;
	
	/*
	 * Constructor
	 */
	public Cell(int x, int y) {
		super("     ");
		point = new Point(x, y);
		str = String.format("(%02d,%02d)", x, y);
		// have the number centered
		setHorizontalAlignment(SwingConstants.CENTER);
		// make it opaque to it will have a background
		setOpaque(true);
		setBackground(Color.WHITE);
		// give it a border
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// the font to use
		setFont(labelFont);
		// the arrayList for the listener of the keyboard
		listenerList = new ArrayList<SudokuCellKbListener>();
	}
	
	/*
	 * Returns the cell value
	 */
	public int getValue() {
		return value;
	}
	/*
	 * Returns cell position (mostly for debug purpose)
	 */
	public Point getPosition() {
		return point;
	}
	
	/*
	 * Reset a cell 
	 */
	private void reset(int val, boolean original) {
		value = val;
		// if value == 0 we put originalProblem to false and set the label at " "
		if(value == 0) {
			originalProblem = false;
			setText("     ");
			return;
		}
		originalProblem = original;
		setText("  " + Integer.toString(value, Character.MAX_RADIX).toUpperCase() + "  ");	
		// determine the font based on the type
		if(originalProblem)
			setFont(boldFont);
		else
			setFont(labelFont);
	}
	/*
	 * Set the value for an original problem
	 */
	public void setOriginalProblemValue(int value) {
		reset(value, true);
	}
	/*
	 * Set the value for a user guess or solver
	 */
	public void setValue(int value) {
		reset(value, false);
	}
	// getter for the type of value
	public boolean isOriginalProblem() {
		return originalProblem;
	}
	/*
	 * The method that overloads toString() that return the value 
	 */
	public String toString() {
		// for text display so we also pass the 0
		return Integer.toString(value, Character.MAX_RADIX).toUpperCase();	
	}
	/*
	 * To return the unique Id as a String
	 */
	public String getIdStr() {
		return str;
	}
	
	/*
	 * Test if 2 Cells are equals (does not check the position 
	 * neither if originalProblem or not)
	 */
	public boolean equals(Cell c) {
		return value == c.value;
	}
	/*
	 * The two method invoked when the cell gain or loose focus
	 */
	@Override
	public void focusGained(FocusEvent e) {
		setBackground(lightYellow);
	}
	@Override
	public void focusLost(FocusEvent e) {
		setBackground(Color.WHITE);
	}

	/*
	 * The mouse listener to gain focus when a cell is cliked
	 * only pressed is used
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		requestFocusInWindow();				// give focus to that cell
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	/*
	 * to register to have the keyboard event relayed
	 */
	public void registerCellKbListener(SudokuCellKbListener listener) {
		// at the first call, when the arrayList is empty, register this as keyLlistener
		// to react to keyBoard key pressed and make the cells focusable
		if(listenerList.size() == 0) {
			// labels by default are not focusable make it focusable
			setFocusable(true);
			// and add them a focus listener so we can change the background color
			addFocusListener(this);
			// amouse listener to be able to select the cell by clicking over it with the mouse
			addMouseListener(this);
			addKeyListener(this);
			// disable the fact that a TAB will bring me to next JLabel
			setFocusTraversalKeysEnabled(false);
		}
		// add this listener to our list
		listenerList.add(listener);
	}
	/*
	 * The KeyListener methods
	 */
	@Override
	public void keyPressed(KeyEvent ev) {
		// pass through our list to prevent all the possible listener
		for(SudokuCellKbListener listener : listenerList) {
			listener.keyPressed(this, point.x , point.y, ev);
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
