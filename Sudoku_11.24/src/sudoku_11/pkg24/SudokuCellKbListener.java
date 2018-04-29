/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

import java.awt.event.KeyEvent;

/*
 * An interface that will implement the GUI components that want to be
 * informed when a key is pressed on the GUI
 */
public interface SudokuCellKbListener {

	// the cell will call this method when a character will be pressed 
	// when they have focus
	public void keyPressed(Cell cell, int x, int y, KeyEvent e);
}
