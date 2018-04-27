package edu.up.cs301.rummikub;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * RummikubLocalGameTest
 *
 * @author Riley Snook
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 */

/**
 * Date: 4-15-18
 * Problem: Mocked error when making unit tests
 * Solution: We used code from this website and put it into the
 * build.gradle(Module:app) folder/location. Now we no longer get the error
 * when run tests.
 * http://tools.android.com/tech-docs/unit-testing-support#TOC-Method-...-not-mocked.-
 */
public class RummikubLocalGameTest {
    @org.junit.Test
    public void makeMove() throws Exception {
        RummikubLocalGame game = new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                                new RummikubComputerPlayer("Thalo")};

        game.start(players);

        //draw card action test - player 1 drew a tile
        assertTrue(game.makeMove(new RummikubDrawAction(players[0])));

        //knock action test - player 1 cannot knock bc already drew a tile
        assertFalse(game.makeMove(new RummikubKnockAction(players[0])));



        //player 2's turn now
        Tile t1= new Tile (0,0,10,Tile.BLACK);
        Tile t2= new Tile (0,0,11,Tile.BLACK);
        TileGroup tg1= new TileGroup();
        TileGroup tg2= new TileGroup();
        tg1.add(t1);
        tg2.add(t2);

        ArrayList<TileGroup> tableGroups= new ArrayList<TileGroup>();
        tableGroups.add(tg1);
        tableGroups.add(tg2);

        assertTrue(game.makeMove (new RummikubConnectAction
                (players[1], tableGroups.indexOf(tg1), tableGroups.indexOf(tg2) )));


    }


}