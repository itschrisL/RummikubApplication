package edu.up.cs301.rummikub;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created by Harrison on 3/27/2018.
 */

public class RummikubLocalGame extends LocalGame {
    RummikubState state;


    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        p.sendInfo(new RummikubState(state,-1));
    }

    @Override
    protected boolean canMove(int playerIdx) {
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }
}
