package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Class RummikubKnockAction
 *
 * represents an action that the player would like end turn by knocking
 *
 * @author Harry Thoma
 */

public class RummikubKnockAction extends GameAction {
    /**
     * constructor for RummikubKnockAction
     *
     * @param player the player who created the action
     */
    public RummikubKnockAction(GamePlayer player) {
        super(player);
    }
}
