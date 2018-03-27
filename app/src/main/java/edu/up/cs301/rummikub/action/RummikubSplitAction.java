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
    private TileGroup group;

    /**
     * constructor for RummikubSplitAction
     *
     * @param player the player who created the action
     * @param group the tile group to split
     */
    public RummikubSplitAction(GamePlayer player, TileGroup group) {
        super(player);

        this.group= group;
    }

    public TileGroup getGroup() {
        return group;
    }
}
