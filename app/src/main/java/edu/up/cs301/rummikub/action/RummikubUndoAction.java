package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Class RummikubUndoAction
 *
 * represents an action to undo
 *
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */

public class RummikubUndoAction extends GameAction {
    /**
     * constructor for RummikubUndoAction
     *
     * @param player the player who created the action
     */
    public RummikubUndoAction(GamePlayer player) {
        super(player);
    }
}
