package edu.up.cs301.game.actionMsg.Actions;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.rummikub.TileGroup;

/**
 * Class PlaceNewTileGroup
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class PlaceNewTileGroup extends GameAction {

    private TileGroup tileGroup; // TileGroup object to represent any group of tile sets

    /**
     * Constructor for PlaceNewTileGroup
     * @param player
     */
    public PlaceNewTileGroup(GamePlayer player, TileGroup tiles) {
        super(player);
        this.tileGroup = tiles;
    }

    /**
     * Get method that returns the Tiles being played
     * @return
     */
    public TileGroup getTileGroup(){
        return this.tileGroup;
    }
}
