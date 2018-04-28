package edu.up.cs301.rummikub;

import java.util.Stack;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.action.*;

/**
 *class Rummikub LocalGame
 *
 * Control the game, responds to action from all players
 * Game Overlord
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubLocalGame extends LocalGame {

    //reference to the game state
    //public to test j-unit
    public RummikubState state;

    //stack of previous states
    private Stack<RummikubState> prevState= new Stack<RummikubState>();

    private Object syncObject = new Object();

    /**
     * External Citation
     * Date: 3/28/2018
     * Problem: wanted stack for previous states
     * Source: java.util.Stack documentation
     * Solution: use java.util.Stack
     */

    /**
     * starts the game
     *
     * @param players in game
     */
    @Override
    public void start(GamePlayer[] players){
        super.start(players);
        this.state= new RummikubState( players.length );
    }

    /**
     * @param p player to send the state to
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        int playerId= getPlayerIdx(p);
        //waits for all actions to be done the sends the info
        synchronized (syncObject){
            p.sendInfo(new RummikubState(state,playerId));
        }
    }

    /**
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return true if it's the players turn
     *          false if it isn't players turn
     */
    @Override
    protected boolean canMove(int playerIdx) {
        return state.isPlayerTurn(playerIdx);
    }

    /**
     * @return name of winner
     *          null if game is not over
     */
    @Override
    protected String checkIfGameOver() {
        //checks to see if its the last round of the game
        if( state.getRound() != 0) return null;

        int winner= state.getWinner();
        //if no winner
        if(winner == -1) return null;

        return playerNames[winner] + " won";
    }

    /**
     * checks which action was received and calls appropriate method
     *  associated with action
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return true if action was legal
     *          false if action was illegal
     */
    @Override
    public boolean makeMove(GameAction action) {

        //actions are completely handled before state in sent
        synchronized (syncObject) {
            int temp = getPlayerIdx(action.getPlayer());
            if(state.isPlayerTurn(temp)){
                if (action instanceof RummikubPlayTileAction) {
                    return playTileAction((RummikubPlayTileAction) action);
                }
                if (action instanceof RummikubSelectTileGroupAction) {
                    return selectTileGroupAction((RummikubSelectTileGroupAction) action);
                }
                if (action instanceof RummikubFreeJokerAction) {
                    return freeJokerAction ((RummikubFreeJokerAction) action);
                }
                if (action instanceof RummikubConnectAction) {
                    return connectAction((RummikubConnectAction) action);
                }
                if (action instanceof RummikubSplitAction) {
                    return splitAction((RummikubSplitAction) action);
                }
                if (action instanceof RummikubDrawAction) {
                    return drawAction((RummikubDrawAction) action);
                }
                if (action instanceof RummikubKnockAction) {
                    return knockAction((RummikubKnockAction) action);
                }
                if (action instanceof RummikubUndoAction) {
                    return undoAction((RummikubUndoAction) action);
                }
                if (action instanceof RummikubRevertAction) {
                    return revertAction((RummikubRevertAction) action);
                }
                if(action instanceof RummikubPlayGroupAction){
                    return playTileGroupAction((RummikubPlayGroupAction)action);
                }
                if(action instanceof RummikubReturnTileAction){
                    return returnTileAction((RummikubReturnTileAction) action );
                }
            }
        }
        //if we got this far, noting happened
        return false;
    }

    /**
     * attempts to play a tile
     *
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean playTileAction(RummikubPlayTileAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by playing a tile
        boolean stateChanged=
                state.canPlayTile(playerId,action.getTileIndex());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * Attempts to play TileGroup
     *
     * @param action sent by player
     * @return whether action was performed
     */
    private boolean playTileGroupAction(RummikubPlayGroupAction action){
        // Set variables
        int playerId = getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by playing a tile
        boolean stateChanged = state.canPlayTileGroup(playerId, action.getTiles());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to select a tile group
     *
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean selectTileGroupAction(RummikubSelectTileGroupAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSelectGroup(playerId,action.getGroup());

        return stateChanged;
    }

    /**
     * attempts to free a joker
     *
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean freeJokerAction (RummikubFreeJokerAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state,push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canFreeJoker (playerId,action.
                        getGroupContainsJoker(),action.getGroupToSwap());

        //if the state did not change,
        // we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to connect two tile groups
     *
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean connectAction(RummikubConnectAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canConnect(playerId,action.getGroup1(),action.getGroup2());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to split a tile group
     *
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean splitAction(RummikubSplitAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged= false;
        if(action instanceof RummikubComputerSplitAction){
            stateChanged= state.canSimpleSplit(
                            playerId,action.getGroup(), action.getTile());
        }
        else{
            stateChanged= state.canSplit(
                    playerId,action.getGroup(), action.getTile());
        }

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to end player's turn by drawing
     *
     * @param action the action sent by player
     * @return whether the action was performed
     */
    private boolean drawAction(RummikubDrawAction action){
        int playerId= getPlayerIdx(action.getPlayer());
        //the player's turn is done
        boolean turnEnded = true;
        //if we try to draw and there are no more tiles in drawPile,
        //will throw an exception
        try {
            turnEnded = state.canDraw(playerId);
        }
        //if exception was thrown, round is over
        catch(RuntimeException rte){
            //sends each player round info to know round is over
            for( int i = 0; i < state.getNumPlayers(); i++ ){
                players[i].sendInfo(new EndRoundInfo());
            }
        }

        //if the turn ended
        if(turnEnded){
            //we want to no longer be able to undo
            prevState.clear();
        }

        return turnEnded;
    }

    /**
     * attempts to return a tile to player's hand
     *
     * @param action sent by player
     * @return whether action was performed
     */
    private boolean returnTileAction( RummikubReturnTileAction action){
        int playerId = getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canReturnTile(playerId, action.getGroupIndex());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to end player's turn by knocking
     *
     * @param action the action sent by player
     * @return whether the action was performed
     */
    private boolean knockAction(RummikubKnockAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        boolean turnEnded= true;
        //try to knock
        try {
            turnEnded = state.canKnock(playerId);
        }
        //if anyone has no tiles
        catch(RuntimeException rte) {
            //tell each player that round is over
            for (int i = 0; i < state.getNumPlayers(); i++) {
                players[i].sendInfo(new EndRoundInfo());
            }
        }

        //if the turn ended
        if(turnEnded){
            //we want to no longer be able to undo
            prevState.clear();
        }

        return turnEnded;
    }

    /**
     * attempts to undo most recent play
     *
     * attempts to undo the last move
     * @return whether the action was performed
     */
    private boolean undoAction(RummikubUndoAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //must be your turn to undo
        if(!state.isPlayerTurn(playerId)) return false;

        //cannot pop an empty stack
        if(prevState.isEmpty()) return false;

        //set the state to the previous state
        state= prevState.pop();
        return true;
    }

    /**
     * attempts to revert to original state
     *
     * @param action the action sent by the player
     * @return whether the action was performed
     */
    private boolean revertAction(RummikubRevertAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //must be your turn to undo
        if(!state.isPlayerTurn(playerId)) return false;

        //cannot pop an empty stack
        if(prevState.isEmpty()) return false;

        //testing yielded that index 0 contained the first pushed object
        //set the state to the state at the bottom of the stack
        state= prevState.get(0);

        //we went back to the begining so there is nothing now to undo
        prevState.clear();

        return true;
    }
}
