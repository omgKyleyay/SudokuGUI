/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;
import java.util.HashMap;
import java.util.Map;
import static sudoku_11.pkg24.Sudoku_1124.grid;

/**
 *
 * @author Kyle
 */
/*
 * A simple class that holds a main() method to unit test the 
 * the method in BruteForce that test if a Sudoku grid is valid
 */
public class TestIfValidGrid implements Runnable {
        
   int r = 0;

    
    public void Validate(int row){
        this.r = row;
    }
    
    public void run(){
        try{
            Map <Integer, Integer> map = new HashMap<>();
            for (int j= 0; j < 9 ; j++){
                Integer count = map.get(grid[r][j]);
                if (count == null){
                    map.put(grid[r][j], 1);
                }
                else {
                    map.put(grid[r][j], map.get(board[r][j] +1));
                    System.out.println("You messed up A-A-RON");
                    return;
                }
        }
        System.out.println("You're good buddy");
        return;
        }
        catch(Exception e){
            
        }
    }
	/*
	 * The main() method
	 */
	public static void main(String[] args) {
		// Build the Grid
		Grid grid = new Grid(9);
		
		// Load it with an valid problem
		System.out.println("Test with a valid problem");
		Problems.setProblem1(grid);
		// call the method that validates asking it to display the grids
		boolean status = BruteForce.isValidSudoku(grid, true);
		System.out.println("Methods returns equals: " + status);
                System.out.println();
        
		// Load it with an invalid problem

		// call the method that validates asking it to display the grids
	}
}




