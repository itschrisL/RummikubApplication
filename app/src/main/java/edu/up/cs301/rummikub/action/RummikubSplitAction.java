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
    private int tileIndex;

    /**
     * constructor for RummikubSplitAction
     *
     * @param player the player who created the action
     * @param groupIndex the tile group to split
     */
    public RummikubSplitAction(GamePlayer player, int groupIndex, int tileIndex) {
        super(player);

        this.groupIndex = groupIndex;
        this.tileIndex = tileIndex;
    }

    public int getGroup() {
        return groupIndex;
    }
    public int getTile() {
        return tileIndex;
    }
}
