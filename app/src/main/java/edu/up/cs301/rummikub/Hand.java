package edu.up.cs301.rummikub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import edu.up.cs301.game.R;

/**
 * class Hand
 *
 * This class produces the player's tiles that are in their hand (un-played)
 * and the rack that the tiles lay on top of.
 * This class extends View in order to draw the board.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class Hand extends View {

    //background of player's tile rack
    Bitmap rackBackground;

    //the tiles in the hand
    TileGroup tiles;

    public Hand(Context context) {
        super(context);
        initilize();
    }

    public Hand(Context context, AttributeSet attrs) {
        super(context, attrs);
        initilize();
    }

    public Hand(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initilize();
    }

    /**
     *initilize allows to draw; rack bitmap created only once
     */
    private void initilize() {
        this.setWillNotDraw(false);

        rackBackground =
                BitmapFactory.decodeResource(getResources(), R.drawable.rack_background);

    }

    @Override
    /**
     * onDraw paints tiles, referencing Tile class.
     */
    public void onDraw(Canvas c) {
        //first paints the player's tile rack
        drawRack(c);

        //random used to randomly select one of four colors
       /* Random random = new Random();
        int randColor;
        int randNum; //variable that represents a random num to be printed onto tile

        //prints first row of random colored tiles in player's hand
        for (int i=0; i<7; i++) {
            randColor = random.nextInt(4);
            new Tile( 0+Tile.WIDTH*i,10,i+1, Tile.colorArray[randColor]).drawTile(c);
        }
        //prints second row of random colored tiles
        for (int i=0; i<7; i++) {
            randColor = random.nextInt(4);
            randNum = random.nextInt(13) + 1;
            new Tile( 0+Tile.WIDTH*i,200,randNum, Tile.colorArray[randColor]).drawTile(c);
        }*/
        if(tiles == null) return;
        ArrayList<Tile> handTiles= tiles.getTileGroup();

        for(Tile tile : handTiles){
            tile.drawTile(c);
        }
    }

    /**
     * drawRack creates bitmap of brown-textured "rack" where player's tiles are displayed
     * and draws it according to coordinates
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
                Bitmap.createScaledBitmap(rackBackground,getWidth(),getHeight(),false);
        c.drawBitmap(rackBackground,0,0,null);
    }

    public void setTiles(TileGroup tiles) {
        this.tiles = tiles;
    }
}