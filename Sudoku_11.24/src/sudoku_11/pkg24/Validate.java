/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku_11.pkg24;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author stKy1219Lo7351
 */
public class Validate implements Runnable {
        int r = 0;
    
    public Validate(int row){
        this.r = row;
    }
    
    public void run(){
        try{
            Map <Integer, Integer> map = new HashMap<>();
            for (int j= 0; j < 9 ; j++){
                Integer count = map.get(board[r][j]);
                if (count == null){
                    map.put(board[r][j], 1);
                }
                else {
                    map.put(board[r][j], map.get(board[r][j] +1));
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

}
