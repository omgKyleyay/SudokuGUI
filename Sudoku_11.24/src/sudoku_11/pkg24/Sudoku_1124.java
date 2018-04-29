/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;
import java.awt.*;        // Uses AWT's Layout Managers
import java.awt.event.*;  // Uses AWT's Event Handlers
import javax.swing.*;     // Uses Swing's Container/Components
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Kyle
 */
public class Sudoku_1124 extends JPanel {
/*
 * A Basic JPanel to display the cells
 */


private static final long serialVersionUID = 1L;

	// grid saved 
	public static Grid grid;
	
	// Constructor receives an instance of class Grid as parameter
	Sudoku_1124(Grid grid) {
		this.grid = grid;
		// get the regions from the Grid
		RowColReg[] region = grid.getRegion();
		// get the size to get the number of row and column in each region
		// and the total of regions per row/column
		int size = (int) Math.sqrt((double) region.length);
		
		// this (the main panel) will contain size X size regions
		setLayout(new GridLayout(size, size));
		// now we will build the other panels (one for each region)
		for(int i = 0; i < region.length; i++) {
			JPanel p = new JPanel(new GridLayout(size, size));
			p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			// get the cells from that region
			Cell[] cell = region[i].getCells();
			// add the cell to the grid
			for(int j = 0; j < cell.length; j++) {
				p.add(cell[j]);
			}
			// then add this panel of a region to the big grid
			add(p);
		}
	}
	
	/*
	 * Used by the SudokuGenerator to get the Grid in GUI mode
	 */
	public Grid getGrid() {
		return grid;
	}
        
        
	/*
	 * To test the Grid
	 */
	public static void main(String[] atgs) {
		// create a JFrame to hold the Grid
		JFrame frame = new JFrame("First GUI demo");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Build a Grid of size 9
		Grid grid = new Grid(9);
		// load the grid with predefined problem
		Problems.setProblem1(grid);

		// build a GridGuiOne from that Grid object
		Sudoku_1124 ggo = new Sudoku_1124(grid);
		// add the Grid to the Frame
		frame.add(ggo);
		// make it visible
		frame.setVisible(true);
		frame.pack();
	}
}



    

