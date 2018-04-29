/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

/**
 *
 * @author Kyle
 */
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/*
 * This class is used to generate a Sudoku grid
 * It's main() method is there to test both in console mode both in GUI mode
 * the whole process
 */
public class SudokuGenerator implements ActionListener {

	// The Grid that will be generated
	private Grid grid;
	// If Gui mode or not
	private boolean useGui;
	// the Grid cells 
	private int size;
	// an arrayList to pickup cells to fill
	private ArrayList<Point> al;
	// the random generator
	private Random ran;
	// the Swing timer for GUI mode
	private javax.swing.Timer timer;
	// a counter if we get stuck
	private int stuckCounter;
	// how much we will fall back when stuck
	private int fallBack;
	// number of iteration performed
	private int nbIter;
	// where we are
	private int cellId;
	
	/*
	 * Constructor with or without GUI
	 */
	public SudokuGenerator(Grid grid) {
		this.grid = grid;
		useGui = false;
		init();
	}
	public SudokuGenerator(Sudoku_1124 gridPanel) {
		grid = gridPanel.getGrid();
		useGui = true;
		init();
	}
	private void init() {
		// save size
		size = grid.getSize();
		// create the arrayList
		al = new ArrayList<Point>();
		// fill it with the cell to process
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				al.add(new Point(i,j));
			}
		}
		ran = new Random();
	}
	/*
	 * Getter to return a new the Grid
	 */
	public void resetGrid() {
		// clear the Grid
		grid.clearCell();
		// cell to process
		cellId = 0;
		// set to 0 our stuck counter
		stuckCounter = 0;
		fallBack = 5;
		nbIter = 0;
		// start the process depending if GUI or not
		if(useGui) {
			timer = new javax.swing.Timer(250, this);
			timer.start();
		}
		else {				// console mode
			resetConsole();
		}
	}
	
	/*
	 * reset in console mode
	 * we just loop until the ArrayList is empty
	 */
	private void resetConsole() {
		// loop until all done
		while(cellId != size * size) {
			// ok try to put a value in a cell
			testNextOne();
		}
	}
	/*
	 * The method called by the Swing timer
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// test if we are done
		if(cellId == size * size) 
			timer.stop();
		else
			testNextOne();
	}
	
	/*
	 * Will try to find a value for a cell
	 */
	private void testNextOne() {
		// if the number of iteration id to big we are really not on a good path
		// so reset the whole thing
		if(nbIter > size * size * 3) {
			// put all the cells back to 0
			for(int i = 0; i < cellId; i++) {
				Point p = al.get(i);
				grid.setCellValue(p.x, p.y, 0);
			}
			// reset number of iteration and other counters
			nbIter = 0;
			stuckCounter = 0;
			fallBack = 5;
		}
		// number of iteration done
		++nbIter;
		// test if we seem stuck
		if(stuckCounter > 5) {
			for(int i = 0; i < fallBack; i++) {
				Point p = al.get(cellId--);
				grid.setCellValue(p.x, p.y, 0);
			}
			// reset stuck counter and increment fall back for next time
			stuckCounter = 0;
			++fallBack;
			// but not too much
			if(fallBack > 10)
				fallBack = 5;
		}
		// select randomly a cell to fill
		Point p = al.get(cellId);
	    // get the possible values for that cell
		int[] val = grid.getAllAvailableValues(p.x, p.y);
		// if no available value
		if(val.length == 0) {
			// put back go back
			--cellId;
			// set its value to precceding one to zero
			Point previous = al.get(cellId);
			grid.setCellValue(previous.x, previous.y, 0);
			// increment our stuck counter
			++stuckCounter;
			// we will try to fix at next call
			return;
		}
		// ok we will try to put a value
		grid.setCellValue(p.x, p.y, val[ran.nextInt(val.length)]);
		// validate that by doing that we wont get stuck
		for(int i = cellId + 1; i < size * size; i++) {
			Point tp = al.get(i);
			val = grid.getAllAvailableValues(tp.x, tp.y);
			// oups if 0 for one cell not a good ides
			if(val.length == 0) {
				// put the original cell back in the cell to process
				grid.setCellValue(p.x, p.y, 0);
				// fall back two cells before to cancel the cellId++ after the loop
				cellId -= 2;
				++stuckCounter;
				break;					// no need to continue
			}
		}
		// process we next cell, if it failed 
		cellId++;
	}
	
	/*
	 * To unit test the whole thing
	 */
	public static void main(String[] args) {
		// create a Grid
		Grid grid = new Grid(16);
		// create a Sudoku generator for that object in console mode
		SudokuGenerator sg = new SudokuGenerator(grid);
		// reset the grid
		sg.resetGrid();

		
		// now make a GUI version
		grid = new Grid(9);
		// build a Panel for it
		Sudoku_1124 panel = new Sudoku_1124(grid);
		// create a JFrame to display it
		JFrame frame = new JFrame("Sudoku generator");
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		// the packed frame will only be good to display "" blank labels when
		// number will pe put it wont resize so we will have to make it a little bit bigger
		Dimension d = frame.getSize();
		d.height *= 7;
		d.width *= 7;
		d.height /= 6;
		d.width /= 6;
		frame.setSize(d);
		// create a SudukuGenerator GUI mode
		sg = new SudokuGenerator(panel);
		sg.resetGrid();
		try {
		Thread.sleep(1000);
		}
		catch(Exception e) {}
		
	}
}


