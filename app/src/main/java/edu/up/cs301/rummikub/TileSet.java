package edu.up.cs301.rummikub;

/**
 * class TileSet
 *determines whether tile set is run or book
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * class TileSet
 * Subclass of TileGroup
 * These are valid groups of tiles on the table.
 * Minimum of 3 tiles.
 * A TileSet may be either a book or a run of tiles
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class TileSet extends TileGroup implements Serializable {

    private static final long serialVersionUID = 3737393762469851826L;

    private boolean isRun; //for sets: true = run, false = book

    /**
     * Constructor for TileSet
     */
    public TileSet(){
        super();
    }

    /**
     * constructor to make set out of a given group
     *
     * @param group the group to turn into a set
     */
    public TileSet(TileGroup group){
        super();
        if(isRun(group)){
            isRun= true;
        }
        else if(isBook(group)){
            isRun= false;
        }
        else{
            Log.i("TileSet","Invalid Set");
            System.exit(-1);
        }
        //this.tiles = new ArrayList<Tile>();
        for(Tile t : group.tiles){
            if(t instanceof JokerTile){
                this.add(new JokerTile((JokerTile)t));
            }
            else {
                this.add(new Tile(t));
            }
            //this.add(t);
        }

        if (isRun) numericalOrder();
    }

    /**
     * Copy constructor for tileSets
     *
     * @param copyTileSet tileSet to copy
     */
    public TileSet (TileSet copyTileSet){
        super (copyTileSet);
        this.isRun = copyTileSet.isRun;
    }

    /**
     * Check if group is a valid set
     *
     * @param group the group to check
     * @return whether group is a valid set
     */
    public static boolean isValidSet(TileGroup group){
        return (isRun(group) || isBook(group));
    }

    /**
     * Determines whether the group passed in is a run or not
     *
     * @param group the group to check
     * @return whether it is a run
     */
    private static boolean isRun(TileGroup group){
        if(group == null) return false;
        if(group.tiles.size() < 3 || group.tiles.size() > 13){ return false;}

        //make a copy
        group= new TileGroup(group);
        Tile[] tileAr= new Tile[group.tiles.size()];
        group.tiles.toArray(tileAr);

        ArrayList<Tile> tempArrayList = new ArrayList<Tile>();
        int tileColor = 0;
        int jokerIndex;

        for(Tile T : group.tiles){
            if(!(T instanceof JokerTile)){
                tileColor = T.getColor();
            }
        }
        if(containsJoker(group)){
            Log.i("Run","Contains Joker");
            for(int i=0;i<tileAr.length-1;i++) {
                if(tileAr[i] instanceof JokerTile){
                    if(i + 1 < tileAr.length){
                        ((JokerTile)tileAr[i]).setJokerValues(tileAr[i+1].getValue()-1, tileColor);
                    }
                    else{
                        ((JokerTile)tileAr[i]).setJokerValues(tileAr[i-1].getValue()+1, tileColor);
                    }
                    if(((JokerTile)tileAr[i]).getJokerVal() > 13){
                        return false;
                    }
                }
                else {
                    if (tileAr[i].getColor() != tileColor) return false;
                    if(tileAr[i+1] instanceof JokerTile){
                        if (tileAr[i].getValue() + 1 != ((JokerTile)tileAr[i+1]).getJokerVal()){
                            return false;
                        }
                    }
                    else {
                        if (tileAr[i].getValue() + 1 != tileAr[i+1].getValue()) {
                            return false;
                        }
                    }
                }
            }
        }
        else {
            //bubble sort the list
            for(int j= tileAr.length -1 ;j>=0;j--){
                for (int i = 0; i < j; i++) {
                    if (tileAr[i].getValue() > tileAr[i + 1].getValue()) {
                        Tile temp = tileAr[i];
                        tileAr[i] = tileAr[i + 1];
                        tileAr[i + 1] = temp;
                    }
                }
            }
            //walk array and make sure they are in natural order
            //and all same color
            tileColor = tileAr[0].getColor();
            for(int i=1;i<tileAr.length;i++){
                if(tileAr[i].getColor() != tileColor) return false;
                if(tileAr[i-1].getValue()+1 != tileAr[i].getValue()) return false;
            }
        }

        /*
        // If Run contains joker, more calculations needed.
        if(containsJoker(group)){
            // Get the color for this Run
            for(Tile T : group.tiles){
                if(!(T instanceof JokerTile)){
                    tileColor = T.getColor();
                    break;
                }
            }
            // If Joker is only tile in array of tiles then keep its value 30
            // And return false for being not a valid set.
            if(group.tiles.size() == 1 && group.tiles.get(0) instanceof JokerTile){
                ((JokerTile) group.tiles.get(0)).jokerVal = 30;
                return false;
            }
            // TODO For now the joker is always added to end of set, need to eventually be able to add to front
            for(int t = 0; t < group.tiles.size(); t++){
                if(group.tiles.get(t) instanceof JokerTile){
                    ((JokerTile) group.tiles.get(t)).jokerVal = group.tiles.get(t+1).getValue();
                    ((JokerTile) group.tiles.get(t)).jokerCol = tileColor;
                }
            }
        }
        else {
            //bubble sort the list
            for(int j= tileAr.length -1 ;j>=0;j--){
                for (int i = 0; i < j; i++) {
                    if (tileAr[i].getValue() > tileAr[i + 1].getValue()) {
                        Tile temp = tileAr[i];
                        tileAr[i] = tileAr[i + 1];
                        tileAr[i + 1] = temp;
                    }
                }
            }
            //walk array and make sure they are in natural order
            //and all same color
            tileColor = tileAr[0].getColor();
            for(int i=1;i<tileAr.length;i++){
                if(tileAr[i].getColor() != tileColor && tileAr[i].getValue() != 30) return false;
                if(tileAr[i-1].getValue()+1 != tileAr[i].getValue()) return false;
            }
        }



        if(containsJoker(group)){
            for(int t = 0; t < tileAr.length; t++){
                if(tileAr[t].getValue()+1 == tileAr[t].getValue()){
                    tempArrayList.add(tileAr[t]);
                }
                else {
                    tempArrayList.add(tileAr[group.tiles.size()-1]);
                }
            }
        }


        //walk array and make sure they are in natural order
        //and all same color
        for(int i=1;i<tileAr.length;i++){
            if(containsJoker(group)){
                if(tileAr[i].getColor() != tileColor && tileAr[i].getValue() != 30) return false;
                if(tileAr[i-1].getValue()+1 != tileAr[i].getValue() && tileAr[i].getValue() != 30) return false;
            }
            else {
                if(tileAr[i].getColor() != tileColor && tileAr[i].getValue() != 30) return false;
                if(tileAr[i-1].getValue()+1 != tileAr[i].getValue()) return false;
            }
        }
        */

        return true;
    }

    /**
     * Determines whether group passed in is book or not
     *
     * @param group the group to check
     * @return whether it is a book
     */
    private static boolean isBook(TileGroup group){
        if(group == null) return false;
        if(group.tiles.size() < 3 || group.tiles.size() > 4) return false;

        //whether we have seen each color
        boolean seenRed= false;
        boolean seenGreen= false;
        boolean seenBlack= false;
        boolean seenBlue= false;
        int bookVal = 0;
        for (Tile t: group.tiles){
            if(!(t instanceof JokerTile)){
                bookVal = t.getValue();
                break;
            }
        }
        for(Tile t : group.tiles){
            // Check if tile is joker
            if(!(t instanceof JokerTile)){
                if(t.getValue() != bookVal){
                    return false;
                }
                int tileColor= t.getColor();
                if(tileColor == Tile.RED){
                    if(seenRed){
                        return false;
                    }
                    seenRed= true;
                }
                else if(tileColor == Tile.GREEN){
                    if(seenGreen){
                        return false;
                    }
                    seenGreen= true;
                }
                else if(tileColor == Tile.BLACK){
                    if(seenBlack){
                        return false;
                    }
                    seenBlack= true;
                }
                else if(tileColor == Tile.BLUE){
                    if(seenBlue){
                        return false;
                    }
                    seenBlue= true;
                }
            }
            else {
                int color;
                if(!seenBlack){
                    color = Tile.BLACK;
                }
                else if (!seenBlue){
                    color = Tile.BLUE;
                }
                else if(!seenGreen){
                    color = Tile.GREEN;
                }
                else if(!seenRed){
                    color = Tile.RED;
                }
                // Return false if all colors are used in book.  Shouldn't happen however.
                else {
                    return false;
                }
                ((JokerTile) t).setJokerValues(bookVal, color);
            }

        }
        return true;
    }

    /**
     * Checks if joker is in this TileGroup
     * @param group - given tile group
     * @return - true if it is and false if not
     */
    public static boolean containsJoker(TileGroup group){
        for(Tile t : group.tiles){
            if(t.getValue() == 0 || t instanceof JokerTile){
                return true;
            }
        }
        return false;
    }

    /**
     * this tile set as a string
     * it will be the tilegroup string
     * immediately followed by a "_"
     * immediately followed by "Book" for a book
     * or "Run" for a run
     *
     * @return a string representation of this tilegroup
     */
    @Override
    public String toString(){
        String groupString= super.toString();

        String typeString= "";
        if(isRun) typeString= "Run";
        else typeString= "Book";

        return groupString + "_" + typeString;
    }
}
