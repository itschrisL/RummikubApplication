package edu.up.cs301.rummikub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;

import edu.up.cs301.rummikub.Tile;

/**
 * Created on 4/12/2018.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class JokerTile extends Tile implements Serializable {

    public int jokerVal;
    public int jokerCol;
    public boolean assigned;

    /**
     * Copy Constructor for JokerTile
     * @param tileX
     * @param tileY
     * @param color
     */
    public JokerTile(int tileX, int tileY, int val, int color){
        super(tileX, tileY, 0, color);
        this.jokerVal = 0;
        this.jokerCol = colorArray[3];
        this.assigned = false;
    }

    /**
     * Copy Constructor for JokerTile Class
     * @param copy
     */
    public JokerTile(JokerTile copy){
        super(copy.getX(), copy.getY(), copy.getValue(), copy.getColor());
    }

    /**
     * Returns point value for joker
     * @return
     */
    public int getPointValue(){
        return 30;
    }

    /**
     * Draw JokerTile
     */
    public void drawJokerTile(Canvas c){
        //fills and outlines tile color
        Paint tileColor = new Paint ();
        tileColor.setColor(TILECOLOR);
        Paint tileOutline = new Paint ();
        tileOutline.setStyle (Paint.Style.STROKE);
        c.drawRect(this.getX(),this.getY(),this.getX()+WIDTH,this.getY()+HEIGHT,tileColor);
        c.drawRect(this.getX(),this.getY(),this.getX()+WIDTH,this.getY()+HEIGHT,tileOutline);


        Paint valColor = new Paint ();
        valColor.setColor(this.getColor());
        valColor.setTextSize((float)0.72*WIDTH);

        Paint jokerValColor = new Paint ();
        jokerValColor.setColor(this.getColor());
        jokerValColor.setTextSize((float)0.30*WIDTH);

        c.drawText("J", this.getX()+WIDTH/12,this.getY()+(HEIGHT*2)/3,valColor);

        c.drawText("" + jokerVal, this.getX()+WIDTH/12,this.getY()+(HEIGHT*2)/3,valColor);
        c.drawCircle(this.getX(), this.getY(), 20, jokerValColor);
    }

    @Override
    public String toString(){
        String colorChar= "";
        if(jokerCol == RED)        colorChar= "R";
        else if(jokerCol == GREEN) colorChar= "G";
        else if(jokerCol == BLUE)  colorChar= "U";
        else if(jokerCol == BLACK) colorChar= "B";
        else{
            Log.i("tile",""+jokerCol);
            return null;
        }

        return "J," + assigned + "," + this.jokerVal + "," + colorChar;
    }

}
