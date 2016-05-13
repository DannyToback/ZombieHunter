/* *****************************************
 * CSCI205 - Software Engineering and Design
 * Fall 2015
 *
 * Name: Ryan Greene, Jack Napor, Danny Toback, & Richard Huffman
 * Date: Nov 13, 2015
 * Time: 7:36:33 AM
 *
 * Project: csci205FinalProject
 * Package: Game
 * File: ZombieSurvivalMain
 * Description: The program's main method, where all of the general run time
 * decisions happen
 *
 * ****************************************
 */
package Game;

import java.io.IOException;

/**
 *
 * @author rtg008
 */
public class ZombieMain {

    public static void main(String[] args) throws IOException {
        //Board board = new Board(3, 1);
        //System.out.println(board);

        ZombieView theView = new ZombieView();
        ZombieController theController = new ZombieController(theView);
        theView.setVisible(true);

    }
}
