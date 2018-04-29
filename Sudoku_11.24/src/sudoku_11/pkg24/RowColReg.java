/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

/*
 * Contains an array of N Sudoku Cells that represents a Row, a Column or a Region
 * The whole idea is to have a common class to describe one of the 3 components
 * Common methods can check for unicity of a number in a Row, Column, Region whitout
 * having to know if the array of cells passed as parameter are effectively a Row,
 * a Column or a Region.
 * 
 * It is in that class that you should check bor twins, 3 of a kinds, ....
 */
public class RowColReg {

	// The array of Sudoku cells
	private Cell[] cell;
	
	/*
	 * Constructor
	 */
	public RowColReg(Cell[] array) {
		cell = array;
	}
	
	/*
	 * If the RowColRegion contains the value
	 */
	public boolean contains(int n) {
		for(int i = 0; i < cell.length; i++) {
			if(cell[i].getValue() == n)
				return true;
		}
		return false;
	}
	/*
	 * Returns if the value pass as parameter is valid for that RowColReg
	 */
	public boolean isAvailable(int n) {
		// always permit to set the cell to 0
		if(n == 0)
			return true;
		return !contains(n);
	}
	/*
	 * Mostly for debug purpose a representation of the whole array
	 */
	public String toString() {
		String str = "[" + cell[0].getIdStr();
		for(int i = 1; i < cell.length; i++)
			str += " " + cell[i].getIdStr();
		return str + "]";
	}
	
	/*
	 * For the GUIs to receive the cells
	 */
	Cell[] getCells() {
		return cell;
	}
}