package edu.up.cs301.rummikub;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * class TileGroup
 *
 * Any grouping of tiles with a minimum of 1 tile
 * Used to save tiles on table and in player's hands
 * (example: Tiles in player's racks)
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class TileGroup implements Serializable {

    private static final long serialVersionUID = 2737393762469851826L;

    //an array list of tile objects
    protected ArrayList<Tile> tiles;

    /**
     * TileGroup Initial Constructor
     */
    public TileGroup(){
        this.tiles = new ArrayList<Tile>();
    }

    /**
     * Constructor that takes in an Array of given Tiles
     *
     * @param tileList the array of tiles
     */
    public TileGroup(Tile ... tileList){
        this.tiles = new ArrayList<Tile>();
        //adds tiles to list
        for(Tile T : tileList){
            tiles.add(T);
        }
    }

    /**
     * Copy constructor for tileGroups
     *
     * @param copyTileGroup tileGroup to copy
     */
    public TileGroup (TileGroup copyTileGroup){
        tiles = new ArrayList<Tile>();

        //copies each tile in tileGroup
        for (Tile t : copyTileGroup.tiles) {
            if(t instanceof JokerTile){
                tiles.add(new JokerTile((JokerTile)t));
            }
            else {
                tiles.add(new Tile(t));
            }
        }
    }

    /**
     * Method to add a Tile to ArrayList tiles
     *
     * @param addTile tile to add
     */
    public void add(Tile addTile){
        tiles.add(addTile);
    }

    /**
     * adds all tiles in addGroup to this group
     *
     * @param addGroup the group to add to this group
     */
    public void merge(TileGroup addGroup){
        for(Tile t : addGroup.tiles){
            this.add(t);
        }
    }
    
    /**
     * Method to remove Tile from TileGroup tiles
     *
     * @param remove tile to remove
     */
    public void remove(Tile remove){
        this.tiles.remove(remove);
    }

    /**
     * Helper Method to return size of TileGroup
     *
     * @return how many tiles in group
     */
    public int groupSize(){
        return this.tiles.size();
    }

    /**
     * Add all the values of tiles in a TileGroup
     *
     * @return total point value of tiles
     */
    public int groupPointValues(){
        int rtnVal = 0;
        for(Tile t : tiles){
            if(!(t instanceof JokerTile)){
                rtnVal = rtnVal + t.getValue();
            }
        }
        return rtnVal;
    }

    /**
     * Add all the values of tiles in a TileGroup
     * same as above method except only used at the end of the round
     *
     * @return total point values in each player's hand
     */
    public int roundGroupPointValues(){
        int rtnVal = 0;
        for(Tile t : tiles){
            //if tile is a joker at 30 points
            //for any other tile at its face value
            if( t.getValue() == 0){
                rtnVal = rtnVal + 30;
            }
            else{
                rtnVal = rtnVal + t.getValue();
            }
        }
        return rtnVal;
    }

    /**
     * arranges this tile group into a random order
     */
    public void randomize(){
        Random rand= new Random();

        //go through the array list and choose a random position to swap with
        for(int i=0;i<tiles.size();i++){
            int randPos= rand.nextInt(tiles.size());
            //now swap them
            Tile temp= tiles.get(i);
            tiles.set(i,tiles.get(randPos));
            tiles.set(randPos,temp);
        }

        /**
         * External Citation
         * Date: March 3 2018
         * Source: Java ArrayList doc
         * Problem: how to set specific index in array list
         * Solution: use set(int,object) method
         */
    }

    /**
     * removes the top tile from the group and returns it
     *
     * @return the top tile in the group
     *         null if empty group
     */
    public Tile draw(){
        //if drawPile is empty
        if(tiles.size() == 0) return null;

        Tile topTile= getTile(tiles.size()-1);
        remove(topTile);

        return topTile;
    }

    /**
     * Helper method to find if a TileGroup has a given tile
     *
     * @param findTile tile to look for
     * @return whether found tile in tileGroup
     */
    public boolean contains(Tile findTile){
        return tiles.contains(findTile);
    }

    /**
     * @param x coord to check
     * @param y coord to check
     * @return the index of the tile that was hit by the point (x,y)
     *          -1 if no tile was hit
     */
    public int hitTile(float x, float y){
        for(int i=0; i < tiles.size() ; i++){
            //if we hit this tile
            if(tiles.get(i).hit(x,y)){
                return i;
            }
        }

        //if we got this far, no tile was hit
        return -1;
    }

    /**
     * determines whether the argument tile can be added
     * and this group would then be a set
     *
     * @param tile the tile we are trying to add
     * @return whether the tile could be added and make this group a set
     */
    public boolean canAddForSet(Tile tile){
        //make a copy of this group
        TileGroup group = new TileGroup(this);
        //add the given tile
        group.add(tile);

        //return whether it is still a set
        return TileSet.isValidSet(group);
    }

    /**
     * Checks if joker is in this TileGroup
     *
     * @return - true if it is and false if not
     */
    public boolean containsJoker(){
        for(Tile t : this.tiles){
            if(t.getValue() == 0 || t instanceof JokerTile){
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts a group of tiles by value
     */
    public void numericalOrder () {
        //empty arraylist
        ArrayList <Tile> temp= new ArrayList<Tile>();
        findJokerValues();

        if(tiles.size() > 1){
            //goes through each tile
            while (!tiles.isEmpty()) {
                Tile currTile= tiles.get(0); //first tile in arrayList
                // Check remainder of tiles in group
                // to see if any are bigger then current tile
                for (int i= 1; i< tiles.size(); i++) {
                    if (tiles.get(i).getValue() < currTile.getValue()) {
                        currTile= tiles.get(i);
                    }
                }
                // add current tile to temp array that will be returned
                temp.add(currTile);
                // removes it from tiles, verify it is in right place
                tiles.remove(currTile);
            }
        }

        tiles= temp; //replaces with sorted tiles arrayList
    }

    /**
     * Goes through a run and sets the joker value
     */
    public void findJokerValues(){
        if(!(this.containsJoker())) return;

        // Assign tileColor
        int tileColor = 0;
        for(Tile T : this.tiles){
            if(!(T instanceof JokerTile)){
                tileColor = T.getColor(); // Set Color for Run
            }
        }
        // Assign values to jokers
        for(int j = 0; j < this.tiles.size(); j++){
            // Check if tile is joker
            if(this.tiles.get(j) instanceof JokerTile){
                // Assign joker value based on if there is a tile left or right
                if(j + 1 < this.tiles.size()){
                    ((JokerTile)this.tiles.get(j)).setJokerValues(
                            this.tiles.get(j+1).getValue()-1, tileColor);
                }
                else{
                    ((JokerTile)this.tiles.get(j)).setJokerValues(
                            this.tiles.get(j-1).getValue()+1, tileColor);
                }
            }
        }
    }

    /**
     * Method to find each color used in a book
     * and return a boolean statement for it
     *
     * @return boolean array of colors not used in book yet
     */
    public boolean[] findColorsInGroup(){
        //Set up return variable.  Array of boolean statements
        //represent if a color has been used in a set.
        //                      BLUE,   RED,  BLACK, GREEN
        boolean[] rtnBoolean = {false, false, false, false};

        // iterates though tiles in set.
        for(Tile tile : this.tiles){
            if(!(tile instanceof JokerTile)){
                if(tile.getColor() == Tile.BLUE){
                    rtnBoolean[0] = true;
                }
                if(tile.getColor() == Tile.RED){
                    rtnBoolean[1] = true;
                }
                if(tile.getColor() == Tile.BLACK){
                    rtnBoolean[2] = true;
                }
                if(tile.getColor() == Tile.GREEN){
                    rtnBoolean[3] = true;
                }
            }
        }
        return rtnBoolean;
    }

    /**
     * returns this tile group as a string
     * it will be each tile followed by a comma
     *
     * @return a string representation of this tile group
     */
    @Override
    public String toString(){
        //if empty, return empty string
        if(tiles.size() == 0) return "";

        String groupString= "";

        for(Tile t : tiles){
            groupString+= t.toString();
            groupString+= ',';
        }

        //remove last comma
        groupString= groupString.substring(0,groupString.length()-1);

        return groupString;
    }

    public Tile getTile(int index){
        return this.tiles.get(index);
    }

    public ArrayList getTileGroup(){
        return this.tiles;
    }
}
