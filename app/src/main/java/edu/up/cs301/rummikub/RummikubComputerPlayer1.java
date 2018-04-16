package edu.up.cs301.rummikub;

import java.util.ArrayList;

import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubPlayGroupAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;

/**
 * This is a smarter computer player
 * it will play tiles out of it's hand, but will not
 * rearrange the table
 *
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */

public class RummikubComputerPlayer1 extends RummikubComputerPlayer {

    /**
     * Constructor for objects of class CounterComputerPlayer1
     *
     * @param name the player's name
     */
    public RummikubComputerPlayer1(String name) {
        super(name);
    }

    /**
     * changes the playActions queue to reflect the actions
     * this player wants to make this move
     */
    @Override
    protected void findMove(){
        int[] indexesToPlay= findSetInHand();

        //if we found a set in our hand
        if(indexesToPlay != null){
            //we want to play it, then knock
            playActions.add(new RummikubPlayGroupAction(this,indexesToPlay));
            playActions.add(new RummikubKnockAction(this));

            return;
        }

        //now check if there is a single tile to play
        int[] playPair= findTileToPlay();

        //if we found a tile to play and we've melded

        if(playPair != null && state.hasMelded(playerNum)){
            playActions.add(new RummikubPlayTileAction(this,playPair[0]));

            //find the index on the table of the tile you just played
            int newGroupIndex= state.getTableTileGroups().size();

            playActions.add(new RummikubConnectAction(this,playPair[1],newGroupIndex));
            //then knock
            playActions.add(new RummikubKnockAction(this));

            return;
        }


        //if we get this far we need to draw
        playActions.add(new RummikubDrawAction(this));
    }

    /**
     * finds a set of playable tiles in this players hand
     * @return the array of indexes of the group the player wants to play
     *          null if no set exists
     */
    private int[] findSetInHand(){
        ArrayList<Tile> tiles= state.getPlayerHand(playerNum).getTileGroup();

        //go the hand, looking at each combination of three tiles
        for(int i= 0; i<tiles.size(); i++){
            for(int j= i+1; j<tiles.size(); j++){
                for(int k= j+1; k<tiles.size(); k++){
                    Tile t1= tiles.get(i);
                    Tile t2= tiles.get(j);
                    Tile t3= tiles.get(k);

                    TileGroup group= new TileGroup(t1,t2,t3);
                    if(isValidSet(group)){

                        return new int[]{i,j,k};
                    }
                }//k loop
            }//j loop
        }//i loop

        //if we got this far, we didn't find a valid set
        return null;
    }

    /**
     * finds out if the player could play this group
     * takes into account whether the player has melded
     * and if this is a sufficient meld
     * @param group the group to check
     * @return whether it is a valid play
     */
    private boolean isValidSet(TileGroup group){
        //if the group is not a valid set
        if(!TileSet.isValidSet(group)) return false;

        //if we've melded, any valid set is good to go
        if(state.hasMelded(playerNum)) return true;

        //if we are here, we haven't melded, so we need at least 30
        if(group.groupPointValues() < 30) return false;

        return true;
    }

    /**
     * finds a tile that can be added to a group
     * @return a 2-big array,
     *          the first index is the tile to play
     *          the second is the index of the group it can be added to
     *          null if no tile can be played
     */
    private int[] findTileToPlay(){
        TileGroup hand= state.getPlayerHand(playerNum);
        ArrayList<TileGroup> groups= state.getTableTileGroups();

        //go through each tile in our hand
        for(int tileIndex=0; tileIndex<hand.groupSize(); tileIndex++){
            Tile tile= hand.getTile(tileIndex);
            //go through each group on the table
            for(int groupIndex=0; groupIndex<groups.size(); groupIndex++){
                //see if the tile can be added to the group
                if(groups.get(groupIndex).canAddForSet(tile)){
                    return new int[]{tileIndex,groupIndex};
                }
            }
        }

        //if we get all the way here, no valid play was found
        return null;
    }
}
