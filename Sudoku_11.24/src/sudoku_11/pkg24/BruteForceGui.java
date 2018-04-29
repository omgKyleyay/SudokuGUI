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
import javax.swing.*;
import java.awt.*;
/*
 * A Demo of the Brute Force solving with a GUI
 * Two GidGuiOne will be created with the same problem
 * One Brute force will be used to fix the problem in order the other in reverse 
 * We will see how the cells are updated
 * 
 */
public class BruteForceGui {
	
	/*
	 * To test the Grid
	 */
	public static void main(String[] atgs) {
		// create a JFrame to hold the Grid
		JFrame frame = new JFrame("Demo with two different solvers running a different way");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Build a Grid of size 9
		Grid grid1 = new Grid(9);

		// load the grid with predefined problem
		Problems.setProblem2(grid1);

		// build a GridGuiOne for each Grid object
		Sudoku_1124 ggo1 = new Sudoku_1124(grid1);

				
		// add the Grid to the Frame
		frame.setLayout(new GridLayout(1,2, 20, 20));
		frame.getContentPane().setBackground(Color.YELLOW);
		frame.add(ggo1);
                
                BruteForce bf1 = new BruteForce(grid1);

		// start the solving
		bf1.solveWithGui(false);

		// make it visible
		frame.setVisible(true);
		frame.pack();
		



	}
}



