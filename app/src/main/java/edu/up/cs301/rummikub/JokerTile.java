package edu.up.cs301.rummikub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import edu.up.cs301.rummikub.Tile;

/**
 * Created on 4/12/2018.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class JokerTile extends Tile {

    public int jokerVal;
    public int jokerCol;

    /**
     * Copy Constructor for JokerTile
     * @param tileX
     * @param tileY
     * @param value
     * @param color
     */
    public JokerTile(int tileX, int tileY, int val, int color){
        super(tileX, tileY, 30, color);
        this.jokerVal = 30;
        this.jokerCol = color;
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
     * Draw JokerTileClass
     */
    @Override
    public void drawTile(Canvas c){
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

        c.drawText("J", this.getX()+WIDTH/12,this.getY()+(HEIGHT*2)/3,valColor);
    }



}
