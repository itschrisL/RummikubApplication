package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Class RummikubRevertAction
 *
 * represtends an action to revert to state at beginning of turn
 *
 * @author Harry Thoma
 */

public class RummikubRevertAction extends GameAction {
    /**
     * constructor for RummikubRevertAction
     *
     * @param player the player who created the action
     */
    public RummikubRevertAction(GamePlayer player) {
        super(player);
    }
}
