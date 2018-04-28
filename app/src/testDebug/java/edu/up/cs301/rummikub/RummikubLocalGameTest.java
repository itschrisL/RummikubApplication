package edu.up.cs301.rummikub;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;
import edu.up.cs301.rummikub.action.RummikubRevertAction;
import edu.up.cs301.rummikub.action.RummikubUndoAction;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * RummikubLocalGameTest
 *
 * J-unit tests for RummikubLocalGame
 *
 * @author Riley Snook
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 */

/**
 * External Citation
 *
 * Date: 4-15-18
 * Problem: Mocked error when making unit tests
 * Solution: We used code from this website and put it into the
 * build.gradle(Module:app) folder/location. Now we no longer get the error
 * when run tests.
 * http://tools.android.com/tech-docs/unit-testing-support#TOC-Method-...-not-mocked.-
 */
public class RummikubLocalGameTest {

    @Test
    public void undo(){
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer jim= new RummikubHumanPlayer("Jim");
        GamePlayer jane= new RummikubHumanPlayer("JaneBot");

        GamePlayer[] players= {jane,jim};

        game.start(players);

        RummikubUndoAction undoAction= new RummikubUndoAction(jim);

        //no play has been made, cannot undo
        assertFalse(game.makeMove(undoAction));

        String state1= game.state.toString();

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        String state2= game.state.toString();

        //just to make sure we actually changed the state
        assertFalse( state1.equals(state2));

        //now we've made a play, we should be able to undo
        assertTrue(game.makeMove(undoAction));

        String state3= game.state.toString();

        //we should be back to previous state
        assertEquals(state1,state3);

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        String state4= game.state.toString();

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        //connect those two tiles
        game.makeMove(new RummikubConnectAction(jim,0,1));

        //we've made two changes, shouldn't be the same as state4
        String state5= game.state.toString();
        assertFalse( state5.equals(state4));

        //if we undo twice, we should get back to state4
        assertTrue(game.makeMove(undoAction));
        assertTrue(game.makeMove(undoAction));

        String state6= game.state.toString();
        assertEquals(state4,state6);

        assertTrue(game.makeMove(undoAction));
        //now we should be back at state1

        //now we shouldn't be able to undo
        assertFalse(game.makeMove(undoAction));
    }


    @Test
    public void revert(){
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer jim= new RummikubHumanPlayer("Jim");
        GamePlayer jane= new RummikubHumanPlayer("JaneBot");

        GamePlayer[] players= {jane,jim};

        game.start(players);


        String state1= game.state.toString();

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        String state2= game.state.toString();

        //just make sure we actually changed the state
        assertFalse( state1.equals(state2));

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));


        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        //connect those two tiles
        game.makeMove(new RummikubConnectAction(jim,0,1));

        game.makeMove(new RummikubUndoAction(jim));

        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));
        //play a tile
        game.makeMove(new RummikubPlayTileAction(jim,0));

        String state3= game.state.toString();

        //weve made more changes
        assertFalse(state2.equals(state3));

        //we should be able to revert
        assertTrue(game.makeMove(new RummikubRevertAction(jim)));

        String state4= game.state.toString();

        //and everything should be the same
        assertEquals(state1,state4);
    }

}