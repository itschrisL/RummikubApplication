package edu.up.cs301.rummikub;

import android.util.Log;

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
     *
     * @return the number of points we are going to play
     */
    @Override
    protected int findMove() {
        //make a copy of the state
        RummikubState stateCopy= new RummikubState(state,playerNum);

        int currentPlayPoints= 0;

        boolean hasMelded = stateCopy.hasMelded(playerNum);

        //we will break out of the loop when we don't find a set
        while (true) {
            Log.i("Smart CP", "First Loop");
            int[] indexesToPlay = findSetInHand(stateCopy);

            //if we didn't find a set in hand
            if (indexesToPlay == null) {
                //break out of the loop
                break;
            }

            //how much is this play worth?

            int groupScore= 0;

            TileGroup hand= stateCopy.getPlayerHand(playerNum);
            for(int i=0; i<indexesToPlay.length; i++){
                Tile tile= hand.getTile(indexesToPlay[i]);
                if(!(tile instanceof JokerTile)){
                    groupScore+= tile.getValue();
                }
            }

            //change our copy of the state
            stateCopy.canPlayTileGroup(playerNum,indexesToPlay);

            //add this score to the current score
            currentPlayPoints+= groupScore;

            //add this action to the play actions queue
            playActions.add(new RummikubPlayGroupAction(this, indexesToPlay));
        }

        //if we havn't melded, we are done
        if(!hasMelded) return currentPlayPoints;

        //this loop will find single tiles to add to the table
        //we will break when we can no longer find one
        while(true) {
            Log.i("Smart CP", "Second Loop");
            //now check if there is a single tile to play
            int[] playPair = findTileToPlay(stateCopy);

            //if we found a tile to play and we've melded

            if (playPair == null) {
                break;
            }

            //get the score of this tile
            int tileScore= 0;
            Tile tile= stateCopy.getPlayerHand(playerNum).getTile(playPair[0]);
            if(!(tile instanceof JokerTile)) tileScore= tile.getValue();

            //make changes to our copy of the state
            stateCopy.canPlayTile(playerNum,playPair[0]);

            //find the index on the table of the tile we just played
            //it is the last index on the table
            int newGroupIndex = stateCopy.getTableTileGroups().size() - 1;

            stateCopy.canConnect(playerNum,playPair[1],newGroupIndex);

            //add the score that will result from this play
            currentPlayPoints+= tileScore;

            //now add the necessary actions to the action queue
            playActions.add(new RummikubPlayTileAction(this, playPair[0]));

            playActions.add(new RummikubConnectAction(this, playPair[1], newGroupIndex));
        }

        return currentPlayPoints;
    }

    /**
     * finds a set of playable tiles in this players hand
     * @param state the state we are working with
     * @return the array of indexes of the group the player wants to play
     *          null if no set exists
     */
    private int[] findSetInHand(RummikubState state){
        ArrayList<Tile> tiles= state.getPlayerHand(playerNum).getTileGroup();

        //go the hand, looking at each combination of three tiles
        for(int i= 0; i<tiles.size(); i++){
            Tile t1= tiles.get(i);

            for(int j= i+1; j<tiles.size(); j++){
                Tile t2= tiles.get(j);

                for(int k= j+1; k<tiles.size(); k++){
                    Tile t3= tiles.get(k);

                    TileGroup group= new TileGroup(t3,t2,t1);
                    if(TileSet.isValidSet(group)){

                        //return indexes
                        return new int[]{i,j,k};
                    }
                }//k loop
            }//j loop
        }//i loop

        //if we got this far, we didn't find a valid set
        return null;
    }

    /**
     * finds a tile that can be added to a group
     * @param state the state we are working with
     * @return a 2-big array,
     *          the first index is the tile to play
     *          the second is the index of the group it can be added to
     *          null if no tile can be played
     */
    private int[] findTileToPlay(RummikubState state){
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
