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
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */

public class RummikubPlayTileAction extends GameAction {
    private int tileIndex;

    /**
     * constructor for RummikubPlayTileAction
     *
     * @param player the player who created the action
     * @param tileIndex the index of the tile to play
     */
    public RummikubPlayTileAction(GamePlayer player, int tileIndex) {
        super(player);

        this.tileIndex= tileIndex;
    }

    public int getTileIndex() {
        return tileIndex;
    }
}
