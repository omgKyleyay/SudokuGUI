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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
/*
 * Contains methods to solve by brute force a Sudoku Grid
 * It implements an ActionListener for whn run in GUI mode
 */
public class BruteForce implements ActionListener {

	// the Grid I will have to work on
	private Grid grid;
	// the ArrayList containing the cells that have to be fixed
	private ArrayList<Point> al;
	// the number of cells to fix (the ArrayList size)
	private int nbTodo;
	// the index we are at
	private int index;
	// the SwingTimer use when do in GUI mode
	private Timer timer;
	
	/*
	 * Constructor
	 */
	public BruteForce(Grid grid) {
		this.grid = grid;
	}
	
	/*
	 * To solve the Grid in console mode will throw an exception if the problem is not feasible
	 */
	public void solve() {
		solve(false);			// call ny default no reverse
	}
	// With the reverse flag
	public void solve(boolean reverse) {
		// The ArrayList of cells to fill
		al = grid.getCellsToFill();
		// get it's size once
		nbTodo = al.size();
		// if nothing to do
		if(nbTodo == 0) 
			return;
		// if as to be reversed call the method doing it
		if(reverse)
			al = reverse(al);
		// index we will search
		index = 0;
		// for the console method we just loop until we are done
		while(index < nbTodo) {
			// common method
			performFill();
		}
		return;
	}
	/*
	 * To solve the Grid with GUI will throw an exception if the problem is not feasible
	 */
	public void solveWithGui(boolean reverse) {
		// The ArrayList of cells to fill
		al = grid.getCellsToFill();
		// get it's size once
		nbTodo = al.size();
		// if nothing to do
		if(nbTodo == 0) 
			return;
		// if as to be reversed call the method doing it
		if(reverse)
			al = reverse(al);
		// index we will search
		index = 0;
		// create the Timer that will be invoked 10 times a second
		timer = new Timer(100, this);
		timer.start();
		return;
	}
	/*
	 * The common method used by both the Console mode and the GUI mode
	 * The console mode method solve() just call it in a while
	 * the GUI mode calls it through a SwingTimer
	 */
	private void performFill() {
		// process next cell if index = -1 it means the solution is not feasible
		// it is in invalid Sudoku problem (you havent fill the cells with the GUI)
		if(index < 0)
			throw new IllegalStateException("Trying to solve an invalid Sudoku problem");
		Point p = al.get(index);
		// get its possible values
		int[] guess = grid.getAllAvailableValues(p.x, p.y);
		// if no possible values fall back to previous cell at next call
		if(guess.length == 0) {
			--index;				// use previous one
			return;
		}
		// get its value (will be 0 at first trial)
		int value = grid.getValue(p);
		// if it is first trial use the first one in the array
		if(value == 0) {
			grid.setCellValue(p.x, p.y, guess[0]);
			++index;				// go check next one at next call
			return;
		}
		// test if the value used is the last of the list
		if(value == guess[guess.length - 1]) {
			// reset this cell value to 0 to start all the process again
			grid.setCellValue(p.x, p.y, 0);
			// and go back to the previous one
			--index;
			return;
		}
		// OK we will have to try the next available one
		for(int lastUsed = 0; lastUsed < guess.length; ++lastUsed) {
		   // when we found the last one we have tried
		   if(value == guess[lastUsed]) {
			   // use  +1 to check the next one
			   grid.setCellValue(p.x, p.y, guess[lastUsed+1]);
			   ++index;
			   break;   // no need to continue the loop
		   }
		}	
	}

	/*
	 * Called by the SwingTimer when in GUI mode to update
	 * the search and the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// if we are done
		if(index == nbTodo) {
			timer.stop();
			return;
		}
		// not done we have to update one batch
		performFill();
	}

	/*
	 * Reverse and ArrayList of Point to test the solution in reverse 
	 * this is used to find if the the solution is unique
	 */
	private ArrayList<Point> reverse(ArrayList<Point> order) {
		// create new ArrayList
		ArrayList<Point> al = new ArrayList<Point>();
		int size = order.size();
		// loop in reverse
		for(int i = size-1; i >= 0; i--)
			al.add(order.get(i));
		// return it
		return al;
	}
	
	/* 
	 * A static method to test if a Sudoku grid is a valid Sudoku grid
	 * To perform this test we apply the normal brute force and the
	 * reversed but force to the Grid. 
	 * If the 2 solved problems are the same we can conclude that there
	 * is only one solution
	 * A print flag is used if you want to print the 2 solved grid
	 */
	public static boolean isValidSudoku(Grid origGrid) {
	   return isValidSudoku(origGrid, false);
	}
	public static boolean isValidSudoku(Grid origGrid, boolean printFlag) {
		// build two copies of the original Grid because the 
		// BruteForce process fills the grid to find the solution
		Grid[] grid = new Grid[2];
		boolean[] reverseFlag = {false, true};
		// build the 2 solutions
		for(int i = 0; i < 2; i++) {
			// build a copy of the Grid
			grid[i] = new Grid(origGrid);
			// create a BruteForce object out of that grid
			BruteForce bf = new BruteForce(grid[i]);
			// call the solving method one reverse one not
			bf.solve(reverseFlag[i]);
		}
		// if printFlag is on we print the grids
		if(printFlag) {
			System.out.println("The original Grid");
			origGrid.printGrid();
			System.out.println("The solved Grid");
			grid[0].printGrid();
			System.out.println("The solved Grid in reverse");
			grid[1].printGrid();
		}
		// return if the 2 grids are the same
		return grid[0].equals(grid[1]);
	}
	/*
	 * To Unit test the whole thing
	 */
	public static void main(String[] args) {
		// Build a Grid
		Grid grid = new Grid(9);
		// load it with one of our prepared model
		Problems.setProblem3(grid);
		
		grid.printGrid();
		
		BruteForce bf = new BruteForce(grid);
		bf.solve(false);
		grid.printGrid();
	}

}
