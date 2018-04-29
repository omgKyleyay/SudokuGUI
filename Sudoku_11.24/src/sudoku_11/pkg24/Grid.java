/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
/*
 * A class that implements a Sudoku grid 9 X 9
 * array contains an array of 9 x 9 cells
 * row, column and region are arrays[9][9] that reference the SAME cells that are in array
 */
public class Grid implements SudokuCellKbListener {

	// The dimension of the grid
	private int size;
	// The array of Cell
	private Cell[][] cell;
	// To access by column by row and by region
	private RowColReg[] column, row, region;
	// The region of each cell
	private int[][] regionId;

	/*
	 * Constructor that receives the size of the Grid as parameter
	 */
	public Grid(int size) {
		// validate the suported size
		switch(size) {
			case 4:
			case 9:
			case 16:
			case 25:
				break;
			default:
				throw new IllegalStateException("Gid size available values are 4, 9, 16, 25");
			
		}
		
		this.size = size;
		// create the master grid and fill it
		cell = new Cell[size][size];
		regionId = new int[size][size];
		for(int col = 0; col < size; col++) {
			for(int row = 0; row < size; row++) {
				cell[row][col] = new Cell(row, col);
			}
		}

		// the rows are easy we just use the rows of the grid as Java 2 dimensionnal arrays
		// are just and aray of arrays
		row = new RowColReg[size];
		for(int i = 0; i < size; i++)
			row[i] = new RowColReg(cell[i]);

		// for columns it is more complicated we have to select each element as they do not
		// follow each other in memory
		column = new RowColReg[size];
		for(int i = 0; i < size; i++) {
			Cell[] c = new Cell[size];
			for(int j = 0; j < size; j++) {
				c[j] = cell[j][i];  // all the rows of that column
			}
			column[i] = new RowColReg(c);
		}

		// now the regions the most complicated ones
		region = new RowColReg[size];
		// this will be the index of the region[] we are building
		int k = 0;
		// the number of rows (and columns) for the number of region
		int nb = (int) Math.sqrt((double) size);
		// the cells of the region
		Cell[] c;
		// lets fill them
		for(int i = 0; i < nb; i++) {
			// first row ow that region
			int firstRow = i * nb;
			for(int j = 0; j < nb; j++) {
				// first col of that region
				int firstCol = j * nb;
				// the cells of that region
				c = new Cell[size];
				// index in that array of Cells
				int idx = 0;
				for(int rRow = 0; rRow < nb; rRow++) {
					for(int rCol = 0; rCol < nb; rCol++) {
						c[idx++] = cell[firstRow+rRow][firstCol+rCol];
					}
				}
				// build the RowColReg object from that array
				region[k] = new RowColReg(c);
				// build the regionId for these cells (could have been integrated in the
				// previous loop but complicated for nothing lots clearer, from a code
				// point of vue, here)
				for(int cellId = 0; cellId < c.length; cellId++) {
					Point p = c[cellId].getPosition();
					regionId[p.x][p.y] = k;
				}
				// next region
				k++;
			}
		}
	}

    Grid(Grid origGrid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	/*
	 * Constructor to clone a Grid
	 */

	/*
	 * returns the all the available value for a cell
	 */
	public int[] getAllAvailableValues(int x, int y) {
		// build a BitSet from 0 to size+1 (0 won't be used)
		BitSet bs = new BitSet(size + 1);
		for(int i = 1; i <= size; i++) {
			// if i is available for that cell
			if(isAvailable(x, y, i))
				bs.set(i);
		}
		// plus obviously its actual value
		int value = getValue(x, y);
		if(value > 0)
		   bs.set(value);
		// make an array of int out of the BitSet
		return buildArray(bs);
	}
	
	/*
	 * returns the the available value for a cell
	 * which has not been set as an original value or set by the user
	 */
	public int[] getAvailableValues(Point p) {
		int x = p.x;
		int y = p.y;
		// if set as original or by the user return an empty array
		if(cell[x][y].isOriginalProblem())
			return new int[0];
		// build a BitSet from 0 to size+1 (0 won't be used)
		BitSet bs = new BitSet(size + 1);
		for(int i = 1; i <= size; i++) {
			// if i is available for that cell
			if(isAvailable(x, y, i))
				bs.set(i);
		}
		// plus obviously its actual value if it not 0
		int value = getValue(x, y);
		if(value > 0)
		   bs.set(value);
		// make an array of int out of the BitSet
		return buildArray(bs);
	}
	
	/*
	 * Makes an array of int from bit set to ON in the BitSet received as parameter
	 */
	private int[] buildArray(BitSet bs) {
		// get the number of bit set to on
		int nb = bs.cardinality();
		// if none no neeed to loop
		if(nb == 0)
			return new int[0];
		// build the array
		int[] array = new int[nb];
		// index in the new array
		int k = 0;
		for(int i = 1; i <= size; i++) {
			// if the bit is set
			if(bs.get(i))
				array[k++] = i;	// add it to the array
		}
		return array;
	}
	/*
	 * Returns if the value N can be set in x,y 
	 */
	public boolean isAvailable(int x, int y, int n) {
		if(!row[x].isAvailable(n))
			return false;
		if(!column[y].isAvailable(n))
			return false;
		if(!getRegion(x, y).isAvailable(n))
		    return false;
		return true;
	}

	/*
	 * to throw an exception is value not valid
	 */
	private void validate(int x, int y, int value) {
        // reset to 0 always ok
		if(value == 0)
			return;
		// if value already the one there
		if(value == getValue(x, y))
			return;
		// if greater than size or not available 
		if(value > size || !isAvailable(x, y, value))
			throw new IllegalStateException("Trying to set " + value + " at x: " + x + " y: " + y);		
	}
	/*
	 * Set the value for an original problem
	 */
	public void setOriginalProblemValue(int x, int y, int value) {
//		validate(x, y, value);
		cell[x][y].setOriginalProblemValue(value);
	}
	/*
	 * Set the value for a user guess
	 */
	public void setCellValue(int x, int y, int value) {
//		validate(x, y, value);
		cell[x][y].setValue(value);
	}
	// getter for the type of value
	public boolean isOriginalProblem(int x, int y) {
		return cell[x][y].isOriginalProblem();
	}

	// to get the column, the row of the region of a cell
	// for the row and column it is quite evident for the region it is trickier
//	// we standardize the methods by making one for each type of RowColReg
	public RowColReg getRow(int x, int y) {
		return row[x];
	}
	public RowColReg getColumn(int x, int y) {
		return column[y];
	}
	public RowColReg getRegion(int x, int y) {
		return region[regionId[x][y]];
	}
	// return the size of the Grid required by the SudokuGenerator
	public int getSize() {
		return cell.length;
	}
	// returns the cells for the GUIs that want to put their own Listener on them
	public Cell[][] getCells() {
		return cell;
	}


	/*
	 * To get a cell value
	 */
	int getValue(int x, int y) {
		return cell[x][y].getValue();
	}
	int getValue(Point p) {
		return getValue(p.x, p.y);
	}
        
        	// clear all the cells when we want to create a new grid
	public void clearCell() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				cell[i][j].setValue(0);
			}
		}
	}
	/*
	 * For the brute force return the list of the cells to fill
	 */
	public ArrayList<Point> getCellsToFill() {
		// ArrayList of cell coordinates stored as Point that will be returned
		ArrayList<Point> al = new ArrayList<Point>();
		// scan all cells
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				// if its value is 0
				if(cell[i][j].getValue() == 0)
					al.add(cell[i][j].getPosition());  // add its Point object
			}
		}
		return al;
	}
	/*
	 * To print the grid
	 */
	public void printGrid() {
		// where to separate region
		int mod = (int) Math.sqrt((double) size);
		// the line of +-----+-----+------- to separate regions in vertical display
		StringBuilder sb = new StringBuilder(size * 5);
		sb.append(' ');
		for(int i = 0; i < mod; i++) {
			sb.append("+-");
			for(int j = 0; j < mod * 2; j++) {
				sb.append('-');
			}
		}
		sb.append('+');
		String line = sb.toString();
		
		// for every row
		for(int i = 0; i < size; i++) {
			if(i % mod == 0)
				System.out.println(line);
			// for every column
			for(int j = 0; j < size; j++) {
				// if module sqrt of the grid
				if(j % mod == 0)
					System.out.print(" |");
			    System.out.print(" " + cell[i][j]);		
			}
			// the final "|" at the end of the row
			System.out.println(" |");
		}
		// the final +------+-----------
		System.out.println(line);
	}
	
	/*
	 * For the GUIs to get the region
	 */
	public RowColReg[] getRegion() {
		return region;
	}

	/*
	 * To ask the Grid to Register as a SudokuCellKbListener
	 */
	public void registerForKeyboardEvent() {
		// pass through all our cells
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				// inform them to call me back
				cell[i][j].registerCellKbListener(this);
			}
		}
	}
	/*
	 * When the Grid register itself as a SudoduCellKbListener to the cells
	 * the cell will call back this method with the key pressed
	 */
	public void keyPressed(Cell cell, int x, int y, KeyEvent e) {
		// test first for cursor movement
		int code = e.getKeyCode();
		switch(code) {
		    // check for cursor movement or keybad or tab
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_LEFT:
			case KeyEvent.VK_KP_RIGHT:
			case KeyEvent.VK_KP_UP:
			case KeyEvent.VK_KP_DOWN:
			case KeyEvent.VK_TAB:
				changeFocus(code, x, y);
				return;
			default:
				break;
		}
		
		// if the cell is an original problem cell we cannot change it
		if(cell.isOriginalProblem())
			return;
		// other checks if it is not a digit or a character exit
		char c = Character.toLowerCase(e.getKeyChar());
		// get the value from conversion at base 36 which is the largest
		try {
			int value = Integer.parseInt("" + c, Character.MAX_RADIX);
			// if >= the size of our grid, ignore it
			if(value >= size)
				return;
			// so set the value of the cell received as parameter to that value
			// if it is legal
			if(isAvailable(x, y, value)) {
				cell.setValue(value);
				// pass to next cell so set the value of code to right cursor
				changeFocus(KeyEvent.VK_RIGHT, x, y);
			}
		}
		catch(Exception ex) {
			return;					// just ignore it
		}
	}
	
	/*
	 * Use to perform change of Cell depending of the keyTyped
	 * when cursor movement
	 */
	private void changeFocus(int code, int x, int y) {
		// direction where I will move
		switch(code) {
		    // check for cursor movement or keybad or tab
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				y--;
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
			case KeyEvent.VK_TAB:
				y++;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				x--;
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				x++;
				break;
			default:
				return;
		}
		// ok check for wrap around
		// if we went to musch to left
		if(x < 0)
			x = size - 1;
		if(x >= size)
			x = 0;
		if(y < 0)
			y = size - 1;
		if(y >= size)
			y = 0;
		// give the focus to the adjacent cell
		cell[x][y].requestFocus();
	}
	
	/*
	 * Test if 2 Grid are equals
	 */
	public boolean equals(Grid g) {
		// test the size first
		if(size != g.size)
			return false;
		// compare all cells
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				// if any cell not equal return false
				if(!(cell[i][j].equals(g.cell[i][j])))
					return false;
			}
		}
		return true;
	}
	/*
	 * To unit test the whole thing
	 */
	public static void main(String[] args) {
		int size = 9;
		Grid g = new Grid(size);
		// displaying all the cell uniqueId created for debug purposes
		System.out.println("The whole grid");
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(" " + g.cell[i][j].getIdStr());
			}
			System.out.println();
		}
		// the rows
		System.out.println("The " + size + " rows");
		for(int i = 0; i < size; i++)
			System.out.println("Row " + i + ": " + g.row[i]);
		// the columns
		System.out.println("The " + size + " columns");
		for(int i = 0; i < size; i++)
			System.out.println("Col " + i + ": " + g.column[i]);
		// the regions
		System.out.println("The " + size + " regions");
		for(int i = 0; i < size; i++)
			System.out.println("Region " + i + ": " + g.region[i]);
		// test the get region method, for column and row it is quite evident
		// but for region we have to test
		RowColReg rcr = g.getRegion(7, 7);
		System.out.println("Region where [7,7] resides : " + rcr);
		rcr = g.getRegion(1, 1);
		System.out.println("Region where [1,1] resides : " + rcr);
	}

}


