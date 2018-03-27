package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.TileGroup;

/**
 * Class RummikubConnectAction
 *
 * represents an action to connect two TileGroups on the table
 *
 * @author Harry Thoma
 */

public class RummikubConnectAction extends GameAction {
    private TileGroup group1;
    private TileGroup group2;

    /**
     * constructor for RummikubConnectAction
     *
     * @param player the player who created the action
     * @param group1 the first of the two groups to merge
     * @param group2 the other of the two groups to merge
     */
    public RummikubConnectAction(GamePlayer player, TileGroup group1, TileGroup group2) {
        super(player);

        this.group1= group1;
        this.group2= group2;
    }

    public TileGroup getGroup1() {
        return group1;
    }

    public TileGroup getGroup2() {
        return group2;
    }
}
