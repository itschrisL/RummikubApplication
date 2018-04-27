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
 * extends View in order to draw the board.
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class GameBoard extends View {

    //green felt background of playing table
    private Bitmap background;

    // Padding between each tile group
    private int columnPadding = Tile.WIDTH + 75;

    // Padding between Rows of tile groups
    private int rowPadding = Tile.getHeight() + 50;
    private int wallPadding = 50;

    //the tile groups currently on the table
    private ArrayList<TileGroup> tileGroups = null;

    //tileGroup selected on table
    private TileGroup selectedGroup = null;

    //whether invalid groups should be highlighted
    private boolean highlightInvalid= false;

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
     *initialize allows to draw; green felt table bitmap created only once
     */
    private void initialize() {
        this.setWillNotDraw(false);

        background =
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.gameboard_background);
    }

    /**
     * onDraw overrides View's onDraw method, and draws current board.
     */
    @Override
    public void onDraw(Canvas c) {
        drawBackground(c);
        drawGroups(c);
        outlineSelectedGroup(c);

        if(highlightInvalid) outlineInvalidGroups(c);
    }

    /**
     * alternative drawGroups method
     * this version gets around the problem of tileGroups overlapping
     * 
     * @param c the canvas on which to draw
     */
    private void drawGroups(Canvas c){
        // Condition if tile group has nothing in it, just return.
        if(tileGroups == null || tileGroups.size() == 0) return;

        int topPadding= 50; //padding between rows of groups
        int sidePadding= 50; //padding between columns of groups

        int surfaceWidth = c.getWidth();
        int surfaceHeight = c.getHeight();

        //the current x and y to draw the tile
        int currX= wallPadding;
        int currY= wallPadding;

        //go through each group
        for(TileGroup group : tileGroups){

            int groupWidth= group.groupSize()*Tile.WIDTH;
            //if this tile group will draw off screen
            if(currX + groupWidth > surfaceWidth - wallPadding){
                //drop down to the next line
                currX= wallPadding;
                currY+= Tile.getHeight() + topPadding;
            }

            ArrayList<Tile> tiles= group.getTileGroup();
            //go through each tile in the group
            for(Tile tile : tiles){
                tile.setX(currX);
                tile.setY(currY);

                //draw the next tile one tile away
                currX+= Tile.WIDTH;
            }

            //add padding between the next group
            currX+= sidePadding;
        }

        //go through each group and draw
        for(TileGroup group : tileGroups){
            ArrayList<Tile> tiles= group.getTileGroup();


            //If Group is just a Joker, reset its values
            if(tiles.size() == 1){
                if(tiles.get(0) instanceof JokerTile){
                    ((JokerTile) tiles.get(0)).setJokerAssigned(false);
                }
            }

            //go through each tile in the group
            for(Tile tile : tiles){
                tile.drawTile(c);
            }
        }
    }

    /**
     * outlines the selected group
     * @param c the canvas on which to draw
     */
    private void outlineSelectedGroup(Canvas c) {
        if(selectedGroup == null) return;

        //outline the selected group yellow
        outlineGroup(c,selectedGroup,0xffffff00);
    }

    /**
     * outlines invalid groups
     * @param c the canvas on which to draw
     */
    private void outlineInvalidGroups(Canvas c){
        for(TileGroup group : tileGroups){
            //if this group is not a set
            if(!(TileSet.isValidSet(group))){
                //outline it red
                outlineGroup(c,group,0xffff0000);
            }
        }
    }

    /**
     * outlines a group
     *
     * @param c the canvas on which to draw
     * @param group the group to outline
     * @param color the color to outline the group with
     */
    private void outlineGroup(Canvas c, TileGroup group, int color){
        Paint outline= new Paint();
        outline.setColor(color);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(10);

        float left= group.getTile(0).getX();
        float top= group.getTile(0).getY();
        float right= left + group.groupSize()*Tile.WIDTH;
        float bottom= top + Tile.getHeight();

        c.drawRect(left,top,right,bottom,outline);
    }

    private void outlineTile(Canvas c, Tile tile, int color){
        Paint outline= new Paint();
        outline.setColor(color);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(10);

        float left= tile.getX();
        float top= tile.getY();
        float right= left + Tile.WIDTH;
        float bottom= top + Tile.getHeight();

        c.drawRect(left,top,right,bottom,outline);
    }

    /**
     * paints the background
     *
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
                Bitmap.createScaledBitmap
                        (background,getWidth(),getHeight(),false);
        c.drawBitmap(background,0,0,null);
    }

    public void setTileGroups(ArrayList<TileGroup> tileGroups) {
        this.tileGroups = tileGroups;
    }

    public void setSelectedGroup(TileGroup group){
        this.selectedGroup= group;
    }

    //todo rename to setOutlineInvalidGroups ?
    public void outlineInvalidGroups(boolean outline){
        this.highlightInvalid= outline;
    }
}
