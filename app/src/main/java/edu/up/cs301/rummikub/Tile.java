package edu.up.cs301.rummikub;

/**
 * Created on 3/26/2018.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * class Tile
 * Saves value and color of tile.
 * This class draws Tiles; includes all basic framework.
 * Tile's x and y coordinates on screen.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class Tile {

    //coordinates of tile according to top left corner of tile
    private int x;
    private int y;

    private int value; //value on tile
    private int color; //tile's color

    //sizing (width and height) of tile
    public static final int WIDTH = 110;
    public static final int HEIGHT = 120;

    //4 tile colors
    public static final int BLUE = Color.argb(255,0,0,255);
    public static final int RED = Color.argb(255,255,0,0);
    public static final int BLACK = Color.argb(255,0,0,0);
    public static final int GREEN = Color.argb(255,50,205,50);
    public static final int TILECOLOR= Color.argb(255,255,250,250);

    //purpose of array is to assign numerical value to each color
    public static final int[] colorArray = {BLUE, RED, BLACK, GREEN};

    /**
     * When Tile is created, the coordinates of the tile, its value,
     * and color should be specified.
     */
    public Tile (int tileX, int tileY, int value, int color) {
        this.x = tileX;
        this.y = tileY;
        this.value = value;
        this.color = color;
    }

    /**
     * Copy constructor for tiles
     * @param copyTile tile to copy
     */
    public Tile(Tile copyTile){
        this.x = copyTile.getX();
        this.y = copyTile.getY();
        this.value =  copyTile.getValue();
        this.color = copyTile.getColor();
    }

    /**
     * Getter method to get X location
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method to get Y location
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Getter method to get Tile value
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter method to get color val
     * @return
     */
    public int getColor() {
        return color;
    }

    /**
     * Setter Method for x location
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter mehtod for Y location
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * This method draws tiles using Canvas.
     */
    public void drawTile(Canvas c) {
        //fills and outlines tile color
        Paint tileColor = new Paint ();
        tileColor.setColor(TILECOLOR);
        Paint tileOutline = new Paint ();
        tileOutline.setStyle (Paint.Style.STROKE);
        c.drawRect(x,y,x+WIDTH,y+HEIGHT,tileColor);
        c.drawRect(x,y,x+WIDTH,y+HEIGHT,tileOutline);


        Paint valColor = new Paint ();
        valColor.setColor(color);
        valColor.setTextSize(80.0f);

        //for the numbers that consist of 2 chars (ex: 10,11,12,13)
        //moves the value printing more to the left
        if (value > 9) {
            c.drawText(""+value, x+WIDTH/12,y+(HEIGHT*2)/3,valColor);
        }
        else {
            c.drawText("" + value, x + WIDTH /3, y + (HEIGHT * 2) / 3, valColor);
        }
    }

    /**
     * returns a string representation of this tile
     * will be a character representing it's color
     * followed immediately with it's value
     *
     * e.g. a Black 12 will be "B12"
     * characters correspond to colors as such:
     * Red- "R"
     * Green- "G"
     * Blue- "U"
     * Black- "B"
     *
     * @return this Tile as a string
     */
    @Override
    public String toString(){
        String colorChar= "";

        if(color == RED)        colorChar= "R";
        else if(color == GREEN) colorChar= "G";
        else if(color == BLUE)  colorChar= "U";
        else if(color == BLACK) colorChar= "B";
        else{
            Log.i("tile",""+color);
            return null;
        }

        return colorChar+value;
    }
}
