package edu.up.cs301.rummikub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubPlayGroupAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;

/**
 * The computer player.
 * This player draws a tile every turn
 *
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */
public class RummikubComputerPlayer extends GameComputerPlayer {

    //the copy of the state
    protected RummikubState state= null;

    //Queue of actions the player wants to play this turn
    protected LinkedList<GameAction> playActions= new LinkedList<GameAction>();

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
     * updates the playActions queue to represetn the moves
     * this player wants to make
     */
    protected void findMove(){
        //this player draws every time
        playActions.add(new RummikubDrawAction(this));
    }

    private void randomSleep() {
        Random random = new Random();
        // Randomly chooses a sleeping time between 1 and 4 seconds
        sleep(random.nextInt(3000)+1000);
    }
}
