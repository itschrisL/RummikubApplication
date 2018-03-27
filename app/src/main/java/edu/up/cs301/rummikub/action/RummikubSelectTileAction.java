package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.Tile;

/**
 * Class RummikubSelectTileAction
 *
 * represents an action to select a tile
 *
 * @author Harry Thoma
 */

public class RummikubSelectTileAction extends GameAction {
    private Tile tile;

    /**
     * constructor for RummikubSelectTileAction
     *
     * @param player the player who created the action
     */
    public RummikubSelectTileAction(GamePlayer player, Tile tile) {
        super(player);

        this.tile= tile;
    }

    public Tile getTile() {
        return tile;
    }
}
