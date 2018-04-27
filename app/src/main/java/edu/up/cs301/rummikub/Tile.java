package edu.up.cs301.rummikub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;

/**
 * class Tile
 *
 * Saves value and color of tile.
 * This class draws Tiles; includes all basic framework.
 * Tile's x and y coordinates on screen.
 *
 * Created on 3/26/2018.
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class Tile implements Serializable {

    private static final long serialVersionUID = 1737393762469851826L;

    //coordinates of tile according to top left corner of tile
    private int x;
    private int y;

    private int value; //value on tile
    private int color; //tile's color

    //sizing (width and height) of tile
    public static final int WIDTH = 110;
    private static int height;

    //4 tile colors
    public static final int BLUE = Color.argb(255,0,0,255);
    public static final int RED = Color.argb(255,255,0,0);
    public static final int BLACK = Color.argb(255,0,0,0);
    public static final int GREEN = Color.argb(255,50,205,50);
    public static final int ORANGE = Color.argb(255, 255, 128, 0);
    public static final int TILECOLOR = Color.argb(255,255,250,250);

    //purpose of array is to assign numerical value to each color
    public static final int[] colorArray = {BLUE, RED, BLACK, GREEN, ORANGE};

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

        Paint paint= new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     *
     * @param x coord to check
     * @param y coord to check
     * @return whether x,y is within this tile
     */
    public boolean hit(float x, float y){

        //if x or y is outside any boundary,
        //we didn't hit this tile
        if(x < this.x) return false;
        if(x > this.x+WIDTH) return false;
        if(y < this.y) return false;
        if(y > this.y+this.height) return false;

        //if we got this far, x and y were not
        //outside any boundary
        //so we hit this tile
        return true;
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
     * Method to set value of Tile
     * @param v - new value
     */
    public void setValue(int v){
        this.value = v;
    }

    public static int getHeight() { return height; }

    public static void setHeight(int index) { height = index; }

    /**
     * This method draws tiles using Canvas.
     * @param c the canvas on which to draw
     */
    public void drawTile(Canvas c) {

        //fills and outlines tile color
        Paint tileColor = new Paint ();
        tileColor.setColor(TILECOLOR);
        Paint tileOutline = new Paint ();
        tileOutline.setStyle (Paint.Style.STROKE);
        c.drawRect(x,y,x+WIDTH,y+height,tileColor);
        c.drawRect(x,y,x+WIDTH,y+height,tileOutline);

        float letterSize =(float).72*WIDTH;
        Paint valColor = new Paint ();
        valColor.setColor(color);
        valColor.setTextSize(letterSize);
        valColor.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = valColor.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        int letterPadding = (((int)(height-letterSize)));

        //for the numbers that consist of 2 chars (ex: 10,11,12,13)
        //moves the value printing more to the left

        c.drawText("" + value, x + WIDTH /2, y + height - letterPadding, valColor);

    }
    /**
     * External Citation
     *
     * in method above text size isn't the same at the height in pixels of the character
     * Date: 4-18-18
     * Problem: We needed the height of the text to put the character in the right place
     * Solution: Found on stack overflow and example that shows a method fontmetrics
     * took the lines and put them in our code
     * Source:https://stackoverflow.com/questions/14277058/get-the-text-height-
     * including-the-font-size-and-set-that-height
     *
     */

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
