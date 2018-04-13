package edu.up.cs301.rummikub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;
import edu.up.cs301.rummikub.action.*;

/**
 * The computer player.
 * This player draws a tile every turn
 *
 * @author Harry Thoma
 */
public class RummikubComputerPlayer extends GameComputerPlayer {

    //the copy of the state
    private RummikubState state= null;

    //Queue of actions the player wants to play this turn
    private LinkedList<GameAction> playActions= new LinkedList<GameAction>();

    /**
     * Constructor for objects of class CounterComputerPlayer1
     *
     * @param name
     * 		the player's name
     */
    public RummikubComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
    }

    /**
     * callback method--game's state has changed
     *
     * @param info
     * 		the information (presumably containing the game's state)
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        if(info instanceof RummikubState){
            this.state= (RummikubState)info;
        }

        //if we are not yet hooked up to a game, ignore
        if(game == null){
            return;
        }

        if(state.isPlayerTurn(playerNum)){
            //if we have not figured out our play, do so
            if(playActions.isEmpty()){
                findMove();
            }
            //then, we want to make our play,
            //one action at a time
            randomSleep();
            game.sendAction(playActions.remove());
        }
    }

    /**
     * changes the playActions queue to reflect the actions
     * this player wants to make this move
     */
    private void findMove(){
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

        //if we found a tile to play
        if(playPair != null){
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
                    if(TileSet.isValidSet(group)){
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

    private void randomSleep() {
        Random random = new Random();
        // Randomly choses a sleeping time between 1 and 4 seconds
        sleep(random.nextInt(3000)+1000);
    }
}
