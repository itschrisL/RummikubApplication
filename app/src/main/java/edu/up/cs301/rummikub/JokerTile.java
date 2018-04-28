package edu.up.cs301.rummikub;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;

/**
 * Class JokerTile
 *
 * Extends Tile class
 * Joker has same functionality as a regular tile but is able to have its
 * value and color changed according to the run or book jokerTile is a part of
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class JokerTile extends Tile implements Serializable {

    private int jokerVal;
    private int jokerCol;

    //whether the joker's value and color has been assigned
    private boolean assigned;

    private static final long serialVersionUID = 6737393762469851826L;

    /**
     * Constructor for JokerTile
     */
    public JokerTile(){
        //unassigned jokers are 0 and orange
        super(0, Tile.ORANGE);
        this.jokerVal = 0;
        this.jokerCol = colorArray[3];
        this.assigned = false;
    }

    /**
     * Copy Constructor for JokerTile Class
     *
     * @param copy the JokerTile to copy
     */
    public JokerTile(JokerTile copy){
        super(copy.getValue(), copy.getColor());
        this.jokerVal = copy.getJokerVal();
        this.jokerCol = copy.getJokerCol();
        this.assigned = copy.getAssigned();
    }

    /**
     * Draw JokerTile
     * overrides tile's drawTile method because joker looks different
     *
     * @param c the canvas on which to draw
     */
    @Override
    public void drawTile(Canvas c){
        //fills and outlines tile color
        Paint tileColor = new Paint ();
        tileColor.setColor(TILECOLOR);
        Paint tileOutline = new Paint ();
        tileOutline.setStyle (Paint.Style.STROKE);
        c.drawRect(this.getX(),this.getY(),this.getX()+WIDTH,
                this.getY()+Tile.getHeight(),tileColor);
        c.drawRect(this.getX(),this.getY(),this.getX()+WIDTH,
                this.getY()+Tile.getHeight(),tileOutline);

        // Set color values for drawing the text for joker tile
        Paint valColor = new Paint ();
        valColor.setColor(Tile.ORANGE);
        valColor.setTextSize((float)0.72*WIDTH);

        c.drawText("J", this.getX() + WIDTH /3,
                this.getY() + (Tile.getHeight() * 2) / 3,valColor);

        Paint jokerValColor = new Paint ();
        // If jokerTile is assigned then show its value and color
        if(assigned){
            jokerValColor.setTextSize((float)0.3*WIDTH);
            jokerValColor.setColor(this.getJokerCol());
            c.drawText("" + this.getJokerVal(), this.getX()+WIDTH/20,
                    this.getY()+(Tile.getHeight() - 15), jokerValColor);
            c.drawCircle(this.getX()+WIDTH-20,
                    this.getY()+Tile.getHeight()-20, 10, jokerValColor);
        }
        // Else just show tile
        else {
            jokerValColor.setColor(this.getColor());
            this.jokerVal = 0;
        }
    }

    /**
     * Setter method to set joker's value and color
     *
     * @param value - tile value of tile taking place of
     * @param color - tile color of tile taking place of
     */
    public void setJokerValues(int value, int color){
        this.jokerVal = value;
        this.jokerCol = color;
        this.assigned = true;
    }

    /**
     * returns a string representation of this tile
     * will be a character representing its color
     * followed immediately with its value
     *
     * e.g. The joker representing a Black 12 will be "(J,true,12,B)"
     * characters correspond to colors as such:
     * Red- "R"
     * Green- "G"
     * Blue- "U"
     * Black- "B"
     *
     * True - is assigned
     * False - not assigned
     *
     * @return this Tile as a string
     */
    @Override
    public String toString(){
        String colorChar= "";
        if(jokerCol == RED)        colorChar= "R";
        else if(jokerCol == GREEN) colorChar= "G";
        else if(jokerCol == BLUE)  colorChar= "U";
        else if(jokerCol == BLACK) colorChar= "B";
        else{
            return null;
        }

        return "(J," + assigned + "," + this.jokerVal + "," + colorChar + ")";
    }

    public int getValue(){
        return this.jokerVal;
    }

    public int getColor(){return this.jokerCol;}

    public int getJokerVal(){return this.jokerVal;}

    public int getJokerCol(){return this.jokerCol;}

    public boolean getAssigned(){return this.assigned;}

    /**
     * Setter method to set Joker assigned boolean
     * Also resets joker's value and color if false
     *
     * @param assigned
     *      - True - is assigned
     *      - False - not assigned
     */
    public void setJokerAssigned(boolean assigned){
        this.assigned = assigned;
        if(!assigned){
            this.jokerVal = 0;
            this.jokerCol = Tile.ORANGE;
        }
    }

    public void setJokerVal(int val){
        this.jokerVal = val;
    }

    public void setJokerCol(int col){
        this.jokerCol = col;
    }
}
