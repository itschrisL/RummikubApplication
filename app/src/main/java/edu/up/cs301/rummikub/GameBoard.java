package edu.up.cs301.rummikub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import edu.up.cs301.game.R;

/**
 * class GameBoard
 *
 * This class produces the green felt background that acts as the playing
 * table; draws the current state of playing table.
 *This class extends View in order to draw the board.
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class GameBoard extends View {

    //green felt background of playing table
    Bitmap background;

    //the tile groups currently on the table
    ArrayList<TileGroup> tileGroups= null;

    TileGroup selectedGroup= null;

    public GameBoard(Context context) {
        super(context);
        initialize();
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     *initilize allows to draw; green felt table bitmap created only once
     */
    private void initialize() {
        this.setWillNotDraw(false);

        background =
                BitmapFactory.decodeResource(getResources(), R.drawable.gameboard_background);
    }

    @Override
    /**
     * onDraw overrides View's onDraw method, and draws current board.
     */
    public void onDraw(Canvas c) {
        drawBackground(c);
        drawGroups(c);
        outlineSelectedGroup(c);
    }

    /**
     * outlines the selected group
     * @param c the canvas on which to draw
     */
    private void outlineSelectedGroup(Canvas c) {
        if(selectedGroup == null) return;

        Paint outline= new Paint();
        //set color to yellow
        outline.setColor(0xffffff00);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(10);

        float left= selectedGroup.getLeft();
        float top= selectedGroup.getTop();
        float right= selectedGroup.getRight();
        float bottom= selectedGroup.getBottom();

        c.drawRect(left,top,right,bottom,outline);
    }

    /**
     * paints the backgroud
     * @param c the canvas on which to draw
     */
    private void drawBackground(Canvas c){
        /**
         * External Citation
         * Date: 5 February 2018
         * Problem: Couldn't figure out how to scale bitmap
         * Resource: https://developer.android.com/reference/android/
         *      graphics/Bitmap.html#createScaledBitmap(android.graphics.
         *      Bitmap,%20int,%20int,%20boolean)
         * Solution: We looked up a method that rescales the bitmap according
         *      to the size we want it to be
         **/
        background =
                Bitmap.createScaledBitmap(background,getWidth(),getHeight(),false);
        c.drawBitmap(background,0,0,null);
    }

    /**
     * draws the tiles in each group
     * @param c the canvas on which to draw
     */
    private void drawGroups(Canvas c){
        if(tileGroups == null) return;
        for(TileGroup group : tileGroups){
            ArrayList<Tile> tiles= group.getTileGroup();

            for(Tile tile : tiles){
                tile.drawTile(c);
            }
        }
    }

    public void setTileGroups(ArrayList<TileGroup> tileGroups) {
        this.tileGroups = tileGroups;
    }

    public void setSelectedGroup(TileGroup group){
        this.selectedGroup= group;
    }
}
