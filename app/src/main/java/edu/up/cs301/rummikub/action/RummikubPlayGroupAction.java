package edu.up.cs301.rummikub.action;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * Created on 4/9/2018.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubPlayGroupAction extends GameAction {

    public int[] indexes;

    public RummikubPlayGroupAction(GamePlayer p, int[] index){
        super(p);
        this.indexes = index;
    }

    public int[] getTiles(){
        return indexes;
    }
}
