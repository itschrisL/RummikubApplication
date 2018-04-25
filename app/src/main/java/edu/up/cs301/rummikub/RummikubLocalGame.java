package edu.up.cs301.rummikub;

import android.widget.Toast;

import java.util.Stack;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.action.*;

/**
 *class Rummikub LocalGame
 *
 * Includes actions for Rummikub
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubLocalGame extends LocalGame {

    RummikubState state;

    Stack<RummikubState> prevState= new Stack<RummikubState>();

    private Object syncObject = new Object();

    public RummikubLocalGame(){

    }

    @Override
    public void start(GamePlayer[] players){
        super.start(players);
        this.state= new RummikubState( players.length );

    }

    /**
     * External Citation
     * Problem: wanted stack for previous states
     * Source: java.util.Stack documentation
     * Solution: use java.util.Stack
     */

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        int playerId= getPlayerIdx(p);
        synchronized (syncObject){
            p.sendInfo(new RummikubState(state,playerId));
        }
    }

    @Override
    protected boolean canMove(int playerIdx) {

        return state.isPlayerTurn(playerIdx);
    }

    @Override
    protected String checkIfGameOver() {

        //checks to see if its the last round of the game
        if( state.getRound() != 0) return null;

        int winner= state.getWinner();

        //if no winner
        if(winner == -1) return null;

        return playerNames[winner] + " won";

    }

    @Override
    public boolean makeMove(GameAction action) {

        synchronized (syncObject) {
            if (action instanceof RummikubPlayTileAction) {
                return playTileAction((RummikubPlayTileAction) action);
            }
            if (action instanceof RummikubSelectTileAction) {
                return selectTileAction((RummikubSelectTileAction) action);
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

        //if we got this far, noting happened
        return false;
    }

    /**
     * attempts to play a tile
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
     * Attemps to play TileGroup
     * @param action
     * @return
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
     * attempts to select a tile
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean selectTileAction(RummikubSelectTileAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSelectTile(playerId,action.getTile());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to select a tile group
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
     * attempts to connect two tile groups
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
     * @param action the action sent by a player
     * @return whether the action was performed
     */
    private boolean splitAction(RummikubSplitAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        //since we are about to change the state, push a copy onto undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSplit(playerId,action.getGroup(), action.getTile());

        //if the state did not change,
        //we don't want to save the state on the undo stack
        if(!stateChanged){
            prevState.pop();
        }

        return stateChanged;
    }

    /**
     * attempts to end player's turn by drawing
     * @param action the action sent by player
     * @return whether the action was performed
     */
    private boolean drawAction(RummikubDrawAction action){
        int playerId= getPlayerIdx(action.getPlayer());
        boolean turnEnded = false;
        try {
            turnEnded = state.canDraw(playerId);
        }
        catch(RuntimeException rte){
            for( int i = 0; i < state.getNumPlayers(); i++ ){
                    sendUpdatedStateTo(players[i]);
            }

        }
        //if the turn ended
        if(turnEnded){
            //we want to no longer be able to undo
            prevState.clear();
        }

        return turnEnded;
    }

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
     * @param action the action sent by player
     * @return whether the action was performed
     */
    private boolean knockAction(RummikubKnockAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        boolean turnEnded= state.canKnock(playerId);

        try {
            turnEnded = state.canDraw(playerId);
        }
        catch(RuntimeException rte){
            for( int i = 0; i < state.getNumPlayers(); i++ ){
                sendUpdatedStateTo(players[i]);
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
     * attempts to undo the last move
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
