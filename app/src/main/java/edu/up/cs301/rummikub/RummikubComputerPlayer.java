package edu.up.cs301.rummikub;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;
import edu.up.cs301.rummikub.action.RummikubDrawAction;

/**
 * The computer player.
 * This player draws a tile every turn
 *
 * @author Harry Thoma
 */
public class RummikubComputerPlayer extends GameComputerPlayer {

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
        //if we are not yet hooked up to a game, ignore
        if(game == null){
            return;
        }

        //if this is not a state, ignore
        if(!(info instanceof RummikubState)){
            return;
        }

        RummikubState state= (RummikubState)info;

        //if it is not our turn, ignore
        if(!state.isPlayerTurn(playerNum)){
            return;
        }

        game.sendAction(new RummikubDrawAction(this));
    }
}
