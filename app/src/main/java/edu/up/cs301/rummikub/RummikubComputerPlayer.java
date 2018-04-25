package edu.up.cs301.rummikub;

import java.util.LinkedList;
import java.util.Random;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubRevertAction;

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

        //failsafe
        //if we ever try an illegal move,
        //somthing went wrong
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
            int score= findMove();

            //if we weren't able to find a play
            if(playActions.isEmpty()){
                //we draw
                playActions.add(new RummikubDrawAction(this));
            }
            //if we must meld, but we arn't going to with this play
            else if(!state.hasMelded(playerNum) && score < 30){
                //we must do nothing and draw
                playActions.clear();
                playActions.add(new RummikubDrawAction(this));
            }
            else{
                //if we find a good play, knock
                playActions.add(new RummikubKnockAction(this));
            }

        }
        //then, we want to make our play,
        //one action at a time
        randomSleep();
        game.sendAction(playActions.remove());
    }

    /**
     * updates the playActions queue to represetn the moves
     * this player wants to make
     *
     * @return the number of points we are going to play
     */
    protected int findMove(){
        return 0;
    }

    private void randomSleep() {
        Random random = new Random();
        // Randomly chooses a sleeping time between 1 and 4 seconds
        sleep(700);
        // /sleep(random.nextInt(3000)+1000);
    }
}
