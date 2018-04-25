package edu.up.cs301.rummikub;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubSplitAction;
import edu.up.cs301.rummikub.action.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by snook on 4/15/2018.
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

        //draw card action test
        assertTrue(game.makeMove(new RummikubDrawAction(players[0])));

        //knock action test
        assertFalse(game.makeMove(new RummikubKnockAction(players[0])));
    }

}