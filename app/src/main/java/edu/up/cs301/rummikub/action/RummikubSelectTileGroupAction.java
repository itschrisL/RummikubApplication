package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.TileGroup;

/**
 * Class RummikubSelectTileGroupAction
 *
 * represents an action to select a tile group
 *
 * @author Harry Thoma
 */

public class RummikubSelectTileGroupAction extends GameAction {
    //the index of the group to select
    private int group;

    /**
     * constructor for RummikubSelectTileGroupAction
     *
     * @param player the player who created the action
     * @param group the tile group to select
     */
    public RummikubSelectTileGroupAction(GamePlayer player, int group) {
        super(player);

        this.group= group;
    }

    public int getGroup() {
        return group;
    }
}
