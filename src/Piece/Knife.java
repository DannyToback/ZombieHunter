/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Piece;

import java.io.IOException;

/**
 *
 * @author DannyToback
 */
public class Knife extends Piece {
    private int orientation;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    /**
     *
     * @param x
     * @param y
     * @param orientation
     * @throws IOException
     */
    public Knife(int x, int y, int orientation) throws IOException {
        super("knife", x, y);
        this.orientation = orientation;
        //this.img = ImageIO.read(new File("images/horzKnife.png"));
    }

    public void setOrientation(int newDir) {
        this.orientation = newDir;
    }

    public int getOrientation() {
        return this.orientation;
    }

}
