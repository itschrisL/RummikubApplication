package edu.up.cs301.rummikub;

import java.util.ArrayList;

import edu.up.cs301.game.GameComputerPlayer;
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
            makeMove();
        }
    }

    private void makeMove(){
        int[] indexesToPlay= findSetInHand();

        if(indexesToPlay == null){
            if(state.canKnock(playerNum)){
                game.sendAction(new RummikubKnockAction(this));
            }
            else {
                game.sendAction(new RummikubDrawAction(this));
            }
        }
        else{
            game.sendAction(new RummikubPlayGroupAction(this,indexesToPlay));
        }
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
}
