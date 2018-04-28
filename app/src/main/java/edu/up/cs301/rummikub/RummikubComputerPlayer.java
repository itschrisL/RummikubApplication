package edu.up.cs301.rummikub;

import android.util.Log;

import java.util.LinkedList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubRevertAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;

/**
 * Class RummikubComputerPlayer
 *
 * This is the base class which controls how each type of computer plays.
 * It finds all it's actions and plays them one by one.
 *
 * If the findMove() method is not overridden, or if this object is itself
 * instantiated, it will draw a tile every move
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
     * External Citation
     * Date: 4/12/2018
     * Problem: wanted queue for actions
     * Source: java.util.LinkedList documentation
     * Solution: use java.util.LinkedList
     *          with add and remove methods making
     *          the list function as a queue
     */

    /**
     * Constructor for objects of class CounterComputerPlayer
     *
     * @param name the player's name
     */
    public RummikubComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
    }

    /**
     * callback method--game's state has changed
     * or other info has been received
     *
     * @param info
     * 		the information (presumably containing the game's state)
     * 	    or an illegal move info
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

        //fail-safe
        //if we ever try an illegal move,
        //something went wrong
        if(info instanceof IllegalMoveInfo){
            //so we must revert and draw
            playActions.clear();
            playActions.add(new RummikubRevertAction(this));
            playActions.add(new RummikubDrawAction(this));
        }

        if(state.isPlayerTurn(playerNum)){
            makePlay();
        }
    }

    /**
     * sets up and makes the play that this player wants to do
     */
    private void makePlay(){
        //if we have not figured out our play, do so
        if(playActions.isEmpty()){
            //this critical section adds actions to the playActions queue
            synchronized (playActions) {
                //how many points the player will play with this move
                int score = findMove();

                //if we weren't able to find a play
                if (playActions.isEmpty()) {
                    //we draw
                    playActions.add(new RummikubDrawAction(this));
                }
                //if we must meld, but we aren't going to with this play
                else if (!state.hasMelded(playerNum) && score < 30) {
                    //we must do nothing and draw
                    playActions.clear();
                    playActions.add(new RummikubDrawAction(this));
                } else {
                    //if we find a good play, knock
                    playActions.add(new RummikubKnockAction(this));
                }
            }
        }

        //then, we want to make our play,
        //one action at a time

        //sleep so the human can see each change
        randomSleep();

        //this critical section removes and sends actions
        //from the playActions queue
        synchronized (playActions) {
            game.sendAction(playActions.remove());
        }
    }

    /**
     * this computer player is dumb, so it does nothing here
     *
     * @return the number of points we are going to play
     */
    protected int findMove(){
        return 0;
    }

    /**
     * sleeps somewhere between 1 and 2 seconds
     */
    private void randomSleep() {
        Random random = new Random();
        sleep(random.nextInt(1000)+1000);
    }
}
