package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Class RummikubDrawAction
 *
 * represents an action that the player would like to end turn by drawing
 *
 * @author Harry Thoma
 */

public class RummikubDrawAction extends GameAction {
    /**
     * constructor for RummikubDrawAction
     *
     * @param player the player who created the action
     */
    public RummikubDrawAction(GamePlayer player) {
        super(player);
    }
}
