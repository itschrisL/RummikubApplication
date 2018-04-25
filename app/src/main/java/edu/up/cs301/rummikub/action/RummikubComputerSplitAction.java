package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;

/**
 * Class RummikubComputerSplitAction
 *
 * represents an action to split a TileGroup on the table
 * into individual TileGroups on the table
 * this is for the computer, so the split performs expectedly
 *
 * @author Harry Thoma
 */

public class RummikubComputerSplitAction extends RummikubSplitAction {
    /**
     * constructor for RummikubSplitAction
     *
     * @param player     the player who created the action
     * @param groupIndex the tile group to split
     * @param tileIndex
     */
    public RummikubComputerSplitAction(GamePlayer player, int groupIndex, int tileIndex) {
        super(player, groupIndex, tileIndex);
    }
}
