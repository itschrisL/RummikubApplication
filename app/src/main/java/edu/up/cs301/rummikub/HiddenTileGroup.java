package edu.up.cs301.rummikub;

import java.io.Serializable;

/**
 * class HiddenTileGroup
 *
 * This child-class of TileGroup represents only the
 * things about a tile group that all players should be able to see.
 * The tiles themselves are hidden (the array list tiles is null),
 * but the information about the size of the group is still included
 *
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 * @author Harry Thoma
 *
 */

public class HiddenTileGroup extends TileGroup implements Serializable {

    //the size of the hidden tile group
    private int groupSize;

    private static final long serialVersionUID = 5737393762469851826L;

    /**
     * constructor for a hidden tile group takes
     * a group which we will copy only some aspects of
     *
     * @param group the group to hide
     *              if the param group is null, create a hidden
     *              representation of an empty group
     */
    public HiddenTileGroup(TileGroup group){
        if(group == null || group.tiles == null){
            groupSize= 0;
        }
        else {
            groupSize = group.groupSize();
        }

        //in any case, set tiles to null
        this.tiles= null; //tiles is an inherited field
    }

    /**
     * @return -1 because this group is hidden
     */
    @Override
    public int groupPointValues(){
        return -1;
    }

    /**
     * @return the size of the hidden group
     */
    @Override
    public int groupSize(){
        return groupSize;
    }

    /**
     * @return a string representation of this hidden group
     */
    @Override
    public String toString(){
        return "??" + groupSize + "??";
    }
}
