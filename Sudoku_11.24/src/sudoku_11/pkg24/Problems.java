/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Kyle
 */
/**
 * A class that contains, for test purpose, the setters for different
 * Sudoku problems
 */
public class Problems {

	/*
	 * private method called by the others to load a problem
	 */
    
    	private static void load(Grid grid, int[][] val) {
        // clear grid because if it already contains something
        // the setOriginalProblemValue might reject to set a cell to a value already there
            for(int i = 0; i < val.length; i++) {
                for(int j = 0; j < val[i].length; j++) {
                        grid.setCellValue(i, j, 0);	
                }
            }
//		load with new problem            
                for(int i = 0; i < val.length; i++) {
                    for(int j = 0; j < val[i].length; j++) {
                            grid.setCellValue(i, j, val[i][j]);	
                    }
                }
        }
        
	private static void load1(Grid grid, int[][] val) {
		// almost always an invalid grid

		for(int i = 0; i < val.length; i++) {
			for(int j = 0; j < val[i].length; j++) {
                            Random rn = new Random();
                            int range = 9 - 1 + 1;
                            int randomNum =  rn.nextInt(range) + 1;
                                    grid.setOriginalProblemValue(i, j, randomNum);	
			}
		}
                Validate(grid);


	}
	// preregistered problem 1	
	public static void setProblem1(Grid grid) {
		int[][] val = {
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		// load it with  0
		load1(grid, val);
	}
        
        public static void setProblem2(Grid grid) {
        int[][] val = {
                        {5, 1, 2, 3, 4, 6, 7, 8, 9},
                        {3, 4, 6, 7, 8, 9, 1, 2, 5},
                        {7, 8, 9, 1, 2, 5, 3, 4, 6},
                        {1, 2, 3, 4, 5, 7, 6, 9, 8},
                        {4, 5, 7, 6, 9, 8, 2, 1, 3},
                        {6, 9, 8, 2, 1, 3, 4, 5, 7},
                        {2, 3, 5, 8, 6, 1, 9, 7, 4},
                        {8, 6, 1, 9, 7, 4, 5, 3, 2},
                        {9, 7, 4, 5, 3, 2, 8, 6, 1}
        };
        // loat it with my values
        load(grid, val);
	}
        
        public static void setProblem3(Grid grid) {
                int[][] val = {
                                {5, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
        // loat it with my predefined values
        load(grid, val);
	}


	public static void main(String[] args) {
		// create a Grid
		Grid grid = new Grid(9);
		// fill and print it
		setProblem1(grid);
		grid.printGrid();
		// fill and print it


	}
}

