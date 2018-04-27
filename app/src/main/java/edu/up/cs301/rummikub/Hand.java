package edu.up.cs301.rummikub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.R;

/**
 * class Hand
 *
 * This class produces the player's tiles that are in their hand (un-played)
 * and the rack that the tiles lay on top of.
 * extends View in order to draw the board.
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class Hand extends View implements Serializable, View.OnTouchListener {

    private static final long serialVersionUID = 4737393762469851826L;

    //background of player's tile rack
    Bitmap rackBackground;

    //the height to start the drawing, makes the hand scrollable
    private int startHeight;
    //sets height of tile in case needs to change
    private boolean setTileHeight;

    //the tiles in the hand
    TileGroup tiles;

    private int wallPadding = 20; //space between the tile and rack border
    private int rowPadding = 30; //space between the rows of tiles
    private int tilePadding = 10; //space between each tile

    public Hand(Context context) {
        super(context);
        initialize();
    }

    public Hand(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public Hand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * initialize allows to draw; rack bitmap created only once
     */
    private void initialize() {
        this.setWillNotDraw(false);
        this.setTileHeight = false;
        this.startHeight= 0;
        rackBackground =
                BitmapFactory.decodeResource
                        (getResources(), R.drawable.rack_background);
    }

    @Override
    /**
     * onDraw paints tiles, referencing Tile class.
     */
    public void onDraw(Canvas c) {
        if( setTileHeight == false ){
            int height = ((this.getHeight()-100)/3);
            Tile.setHeight(height);
            setTileHeight = true;
        }
        //first paints the player's tile rack
        drawRack(c);

        if (tiles == null) return;

        //draw tiles on rack
        drawTiles(c);
    }

    /**
     * draws the tiles
     *
     * @param c the canvas on which to draw
     */
    private void drawTiles(Canvas c) {

        ArrayList<Tile> tileList = tiles.getTileGroup();

        //first tile drawn
        int currX = wallPadding;
        int currY = wallPadding + startHeight;
        int handWidth= getWidth()-100;

        for (int i = 0; i < tiles.groupSize(); i++) {
            //set tile's x and y
            tileList.get(i).setX(currX);
            tileList.get(i).setY(currY);
            currX = currX + Tile.WIDTH + tilePadding;

            //changes x-coord
            if (currX + wallPadding > handWidth) {
                currX = wallPadding;
                currY += Tile.getHeight() + rowPadding;
            }
        }

        //draws each tile according to its x,y coords
        for (Tile tile : tileList) {
            if(tile instanceof JokerTile){
                ((JokerTile) tile).setJokerAssigned(false);
            }
            tile.drawTile(c);
        }
    }

    /**
     * drawRack creates bitmap of brown-textured "rack" where player's
     * tiles are displayed and draws it according to coordinates
     **/
    public void drawRack(Canvas c) {
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

        rackBackground =
                Bitmap.createScaledBitmap
                        (rackBackground, getWidth(), getHeight(), false);
        c.drawBitmap(rackBackground, 0, 0, null);
    }

    /**
     * get scroll button touches
     * @param view the button that was clicked
     */
    public boolean onTouch(View view, MotionEvent event) {

        //we don't care about any other button
        if(view.getId() != R.id.ButtonScroll) return false;

        //if we don't have any tiles, dont worry about scrolls
        if(tiles == null || tiles.groupSize() == 0) return false;

        //find top and bottom heights of tiles
        int top= tiles.getTile(0).getY();

        Tile bottomTile= tiles.getTile(tiles.groupSize()-1);
        int bottom= bottomTile.getY() + bottomTile.getHeight();

        int maxScroll= 13;

        //calculate scroll
        float delta= 2*event.getY()*maxScroll/view.getHeight() - maxScroll;

        //if we want to scroll down, but shouldn't
        if(delta > 0 && top >= wallPadding) return false;
        //if we want to scroll up, but shouldn't
        if(delta < 0 && bottom < getHeight() - wallPadding) return false;

        startHeight+= delta;

        //redraw the hand
        invalidate();
        return true;
    }

    public void setTiles(TileGroup tiles) {
        this.tiles = tiles;
    }

}