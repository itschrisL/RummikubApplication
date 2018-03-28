package edu.up.cs301.rummikub;

import java.util.Stack;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.action.*;

/**
 * Created by Harrison on 3/27/2018.
 */

public class RummikubLocalGame extends LocalGame {
    RummikubState state;

    Stack<RummikubState> prevState= new Stack<RummikubState>();

    /**
     * External Citation
     * Problem: wanted stack for previous states
     * Source: java.util.Stack documentation
     * Solution: use java.util.Stack
     */

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        int playerId= getPlayerIdx(p);
        p.sendInfo(new RummikubState(state,playerId));
    }

    @Override
    protected boolean canMove(int playerIdx) {

        return state.isPlayerTurn(playerIdx);
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {

        if(action instanceof RummikubPlayTileAction){
            return playTileAction((RummikubPlayTileAction)action);
        }
        if(action instanceof RummikubSelectTileAction){
            return selectTileAction((RummikubSelectTileAction)action);
        }
        if(action instanceof RummikubSelectTileGroupAction){
            return selectTileGroupAction((RummikubSelectTileGroupAction)action);
        }
        if(action instanceof RummikubConnectAction){
            return connectAction((RummikubConnectAction)action);
        }
        if(action instanceof RummikubSplitAction){
            return splitAction((RummikubSplitAction)action);
        }
        if(action instanceof RummikubDrawAction){
            return drawAction((RummikubDrawAction)action);
        }
        if(action instanceof RummikubKnockAction){
            return knockAction((RummikubKnockAction)action);
        }
        if(action instanceof RummikubUndoAction){
            return undoAction((RummikubUndoAction)action);
        }
        if(action instanceof RummikubRevertAction){
            return revertAction((RummikubRevertAction)action);
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

        //since we are about to change the state, push a copy onto the undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by playing a tile
        boolean stateChanged=
                state.canPlayTile(playerId,action.getTile());

        //if the state did not change, we don't want to save the state on the undo stack
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

        //since we are about to change the state, push a copy onto the undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSelectTile(playerId,action.getTile());

        //if the state did not change, we don't want to save the state on the undo stack
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

        //since we are about to change the state, push a copy onto the undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSelectGroup(playerId,action.getGroup());

        //if the state did not change, we don't want to save the state on the undo stack
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

        //since we are about to change the state, push a copy onto the undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canConnect(playerId,action.getGroup1(),action.getGroup2());

        //if the state did not change, we don't want to save the state on the undo stack
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

        //since we are about to change the state, push a copy onto the undo stack
        prevState.push(new RummikubState(state,-1));

        //attempt to change the state by selecting a tile
        boolean stateChanged=
                state.canSplit(playerId,action.getGroup());

        //if the state did not change, we don't want to save the state on the undo stack
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

        boolean turnEnded= state.canDraw(playerId);

        //if the turn ended
        if(turnEnded){
            //we want to no longer be able to undo
            prevState.clear();
        }

        return turnEnded;
    }

    /**
     * attempts to end player's turn by knocking
     * @param action the action sent by player
     * @return whether the action was performed
     */
    private boolean knockAction(RummikubKnockAction action){
        int playerId= getPlayerIdx(action.getPlayer());

        boolean turnEnded= state.canKnock(playerId);

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

        //testing yeilded that index 0 contained the first pushed object
        //set the state to the state at the bottom of the stack
        state= prevState.get(0);

        //we went back to the begining so there is nothing now to undo
        prevState.clear();

        return true;
    }
}
