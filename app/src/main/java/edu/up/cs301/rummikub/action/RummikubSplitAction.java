package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.TileGroup;

/**
 * Class RummikubSplitAction
 *
 * represents an action to split a TileGroup on the table
 * into individual TileGroups on the table
 *
 * @author Harry Thoma
 */

public class RummikubSplitAction extends GameAction {
    private int groupIndex;

    /**
     * constructor for RummikubSplitAction
     *
     * @param player the player who created the action
     * @param groupIndex the tile group to split
     */
    public RummikubSplitAction(GamePlayer player, int groupIndex) {
        super(player);

        this.groupIndex= groupIndex;
    }

    public int getGroup() {
        return groupIndex;
    }
}
