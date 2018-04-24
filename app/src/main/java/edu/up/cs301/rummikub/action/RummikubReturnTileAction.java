package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.Tile;

/**
 * Class RummikubPlayTileAction
 *
 * represents an action that a player would like to play a tile
 *
 * @author Riley Snook
 */

public class RummikubReturnTileAction extends GameAction {

    private int groupIndex;

    /**
     * constructor for RummikubPlayTileAction
     *
     * @param player the player who created the action
     * @param groupIndex the index of the tile to play
     */
    public RummikubReturnTileAction(GamePlayer player, int groupIndex) {
        super(player);
        this.groupIndex= groupIndex;
    }

    public int getGroupIndex() {
        return groupIndex;
    }
}
