package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.Tile;

/**
 * Class RummikubPlayTileAction
 *
 * represents an action that a player would like to play a tile
 *
 * @author Harry Thoma
 */

public class RummikubPlayTileAction extends GameAction {
    Tile tile;

    /**
     * constructor for RummikubPlayTileAction
     *
     * @param player the player who created the action
     * @param tile the tile to play
     */
    public RummikubPlayTileAction(GamePlayer player, Tile tile) {
        super(player);

        this.tile= tile;
    }

    public Tile getTile() {
        return tile;
    }
}
