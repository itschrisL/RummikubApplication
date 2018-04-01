package edu.up.cs301.rummikub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class GameBoard extends View {

    //green felt background of playing table
    Bitmap background;

    //the tile groups currently on the table
    ArrayList<TileGroup> tileGroups;

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
        //Tile tile;

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

        /*//draws tiles on board
        int i;
        //draws a run of 3 yellow tiles
        for (i=0; i<3; i++) {
            tile = new Tile (i*Tile.WIDTH+100, 50, i+1, Tile.BLACK);
            tile.drawTile(c);
        }
        //draws a group of 4 colored tiles value=4
        for (i=0; i<4; i++) {
            tile = new Tile (i*Tile.WIDTH+700,200,4,Tile.colorArray[i]);
            tile.drawTile(c);
        }
        //draws a group of 3 colored tiles value=10
        for (i=0; i<3; i++) {
            tile = new Tile (i*Tile.WIDTH+500,400,10,Tile.colorArray[i+1]);
            tile.drawTile(c);
        }*/

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
}
