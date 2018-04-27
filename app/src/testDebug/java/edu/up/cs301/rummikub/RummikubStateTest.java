package edu.up.cs301.rummikub;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by daylinkuboyama on 4/26/18.
 */
public class RummikubStateTest {

    /**
     * creates localGame and gameState with 2 players
     */
    public void gameSetup () {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);


    }


    @Test
    public void testIsPlayerTurn() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        assertTrue(state.isPlayerTurn(0));
        assertFalse(state.isPlayerTurn(3));
    }

    @Test
    public void canDraw() throws Exception {


    }

    @Test
    public void canKnock() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        assertTrue(state.isPlayerTurn(0));


        ArrayList<TileGroup> tableGroups= state.getTableTileGroups();
        TileGroup tg1= new TileGroup(new Tile(0,0,11,Tile.BLACK));
        TileGroup tg2= new TileGroup(new Tile(0,0,12,Tile.BLACK));
        TileGroup tg3= new TileGroup(new Tile(0,0,12,Tile.BLACK));

        tableGroups.add(tg1);
        tableGroups.add(tg2);
        tableGroups.add(tg3);

        int tg1Idx= tableGroups.indexOf(tg1);
        int tg2Idx= tableGroups.indexOf(tg2);
        int tg3Idx= tableGroups.indexOf(tg3);

        tg1.merge(tg2);
        tableGroups.remove(tg2);
        tg1.merge(tg3);

        //checks to make sure player cannot knock with invalid play
        //invalid play: B11, B12, B12
        assertFalse(state.canKnock(0));

        tableGroups.clear();
        TileGroup tg1Good= new TileGroup(new Tile(0,0,11,Tile.BLACK));
        TileGroup tg2Good= new TileGroup(new Tile(0,0,12,Tile.BLACK));
        TileGroup tg4= new TileGroup(new Tile(0,0,13,Tile.BLACK));

        tableGroups.add(tg1Good);
        tableGroups.add(tg2Good);
        tableGroups.add(tg4);

        tg1Good.merge(tg2Good);
        tableGroups.remove(tg2Good);
        tg1Good.merge(tg4);

        assertTrue(state.canKnock(0));
    }

    @Test
    public void canSelectTile() throws Exception {

    }

    @Test
    public void canSelectGroup() throws Exception {

    }

    @Test
    public void canConnect() throws Exception {

    }

    @Test
    public void canReturnTile() throws Exception {

    }

    @Test
    public void canFreeJoker() throws Exception {

    }

    @Test
    public void canSplit() throws Exception {

    }

    @Test
    public void canSimpleSplit() throws Exception {

    }

    @Test
    public void isValidTable() throws Exception {

    }

    @Test
    public void canPlayTile() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;




    }

    @Test
    public void canPlayTileGroup() throws Exception {

    }

    @Test
    public void isFromHand() throws Exception {

    }

    @Test
    public void hasCurrentPlayerPlayed() throws Exception {

    }

    @Test
    public void hasMelded() throws Exception {

    }

}