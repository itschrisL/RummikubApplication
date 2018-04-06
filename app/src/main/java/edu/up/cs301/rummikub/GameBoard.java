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
    private Bitmap background;

    //the tile groups currently on the table


    private int columnPadding = Tile.WIDTH + 75; // Padding between each tile group
    private int rowPadding = Tile.HEIGHT + 50; // Padding between Rows of tile groups
    private int wallPadding = 50;  // Padding from
    private int cellingPadding = 50;

    public ArrayList<TileGroup> tileGroups = null;

    public TileGroup selectedGroup = null;

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

    /**
     * onDraw overrides View's onDraw method, and draws current board.
     */
    @Override
    public void onDraw(Canvas c) {
        drawBackground(c);
        drawGroups(c);
        outlineSelectedGroup(c);
    }

    public void drawGroups(Canvas c){
        // Set instance variables
        int columnCount = 0;
        int rowCount = 0;
        int surfaceWidth = c.getWidth();
        int surfaceHeight = c.getHeight();

        // Condition if tile group has nothing in it, just return.
        if(tileGroups == null || tileGroups.size() == 0) return;

        // Iterates through each group on board
        for(TileGroup group : tileGroups){

            // Calculate starting x location for group
            int xNum = (wallPadding + (columnCount* columnPadding));

            // If xNum is greater then surface width start a new row
            if(xNum > surfaceWidth){
                columnCount = 0;  // Reset column count
                rowCount++; // add to row count
                xNum = (wallPadding + (columnCount* columnPadding));
            }

            // Calculate starting y location for group
            int yNum = cellingPadding + (rowPadding*rowCount);

            // Iterates through each tile in group and draws them.
            for(int t = 0; t < group.groupSize(); t++){
                Tile tile = group.getTile(t);
                tile.setX(xNum + (t*Tile.WIDTH));
                tile.setY(yNum);
                tile.drawTile(c);
            }
            columnCount++; // add to column count
        }
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
         */
        background =
                Bitmap.createScaledBitmap(background,getWidth(),getHeight(),false);
        c.drawBitmap(background,0,0,null);
    }

    public void setTileGroups(ArrayList<TileGroup> tileGroups) {
        this.tileGroups = tileGroups;
    }

    public void setSelectedGroup(TileGroup group){
        this.selectedGroup= group;
    }
}
