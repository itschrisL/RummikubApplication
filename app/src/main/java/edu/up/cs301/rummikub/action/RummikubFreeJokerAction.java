package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * class RummikubFreeJokerAction
 *
 * Represents an action to free the joker
 *
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */

public class RummikubFreeJokerAction extends GameAction {

    private int groupContainsJoker; //group on table with joker
    private int groupToSwap; //tile that will replace joker

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public RummikubFreeJokerAction(
            GamePlayer player, int withJoker, int swapper) {
        super(player);

        this.groupContainsJoker= withJoker;
        this.groupToSwap= swapper;
    }

    public int getGroupContainsJoker () {return groupContainsJoker;}
    public int getGroupToSwap () {return groupToSwap;}
}
