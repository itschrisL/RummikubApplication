package edu.up.cs301.rummikub;

import org.junit.Test;

import java.util.ArrayList;

import edu.up.cs301.game.GamePlayer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * RummikubStateTest
 *
 * J-unit tests for RummikubState.
 *
 * @author Riley Snook
 * @author Daylin Kuboyama
 * @author Chris Lytle
 * @author Harry Thoma
 */
public class RummikubStateTest {

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
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        assert state.isPlayerTurn(0);

        assertTrue(state.canDraw(0));
        assertFalse(state.canKnock(1));

    }

    @Test
    public void canKnock() throws Exception {
        //todo
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

        tg1.merge(tg2);
        tg1.merge(tg3);

        //checks to make sure player cannot knock with invalid play
        //invalid play: B11, B12, B12
        assertFalse(state.canKnock(0));
        //still player 1's turn
        assertTrue(state.isPlayerTurn(0));

        tableGroups.clear();

        TileGroup tg1Final= new TileGroup(new Tile(0,0,11,Tile.BLACK));
        TileGroup tg2Final= new TileGroup(new Tile(0,0,12,Tile.BLACK));
        TileGroup tg3Final= new TileGroup(new Tile(0,0,13,Tile.BLACK));

        tableGroups.add(tg1Final);
        tableGroups.add(tg2Final);
        tableGroups.add(tg3Final);

        tg1Final.merge(tg2Final);
        tg1Final.merge(tg3Final);

        assertTrue(state.canKnock(0));

    }

    @Test
    public void canSelectTile() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        //player selects a card that is in their hand
        assertTrue(state.canSelectTile(0,state.getPlayerHand(0).getTile(4)));

        //player cant select a card in the other players hand
        assertFalse(state.canSelectTile(0,state.getPlayerHand(1).getTile(9)));
    }

    @Test
    public void canSelectGroup() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        state.getTableTileGroups().add
                (new TileGroup(new Tile(0,0,6,Tile.GREEN),
                        new Tile(0,0,9,Tile.BLUE)));
        state.getTableTileGroups().add
                (new TileGroup(new Tile(0,0,9,Tile.GREEN),
                        new Tile(0,0,12,Tile.BLUE)));

        //shows that a group was selected
        assertTrue(state.canSelectGroup(0,0));

        //shows that the right group was selected
        assertEquals
                (state.getSelectedGroup(),state.getTableTileGroups().get(0));

        //shows that the other group on the table isnt selected
        assertFalse
                ((state.getSelectedGroup() == state.getTableTileGroups().get(1)));

        //deselects a group
        state.canSelectGroup(0,-1);

        //shows that a group is no longer selected
        assertEquals(state.getSelectedGroup(),null);

    }

    @Test
    public void canConnect() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        assertTrue(state.isPlayerTurn(0));

        ArrayList<TileGroup> tableGroups= state.getTableTileGroups();

        state.getPlayerHand(0).add(new Tile(0,0,11,Tile.BLACK));
        state.getPlayerHand(0).add(new Tile(0,0,12,Tile.BLACK));

        state.canPlayTile(0,14);
        state.canPlayTile(0,14);

        //connects the two tiles that are solo groups
        assertTrue(state.canConnect(0,0,1));

        //player 2 cannot connect any tiles
        assertFalse(state.canConnect(1,0,1));

        //player has joker in hand
        state.getPlayerHand(0).add(new JokerTile(0,0,0,Tile.ORANGE));
        state.canPlayTile(0,14);

        //player 1 able to connect joker to tileGroups on table
        assertTrue(state.canConnect(0,0,1));
    }

    @Test
    public void canReturnTile() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;


        ArrayList<TileGroup> tableGroups= state.getTableTileGroups();

        //add tiles to hand, its index is 14
        state.getPlayerHand(0).add(new Tile(0,0,11,Tile.BLACK));

        //plays the tile above to the table
        state.canPlayTile(0,14);

        //shows that you can return that tile to the players hand
        assertTrue(state.canReturnTile(0,0));

        tableGroups.clear();

        //todo

        //adds a tile to the table
        tableGroups.add(new TileGroup(new Tile(0,0,9,Tile.BLACK)));
        assertFalse(state.canReturnTile(0,0));

    }

    @Test
    public void canFreeJoker() throws Exception {
        //todo
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        ArrayList<TileGroup> tableGroups= state.getTableTileGroups();

        TileGroup jokerGroup= new TileGroup(new JokerTile (0,0,9,Tile.BLACK),
                                            new Tile (0,0,10, Tile.BLACK),
                                            new Tile (0,0,11,Tile.BLACK));
        TileGroup tileGroup= new TileGroup(new Tile (0,0,9,Tile.BLACK));

        tableGroups.add(jokerGroup);
        tableGroups.add(tileGroup);

        int jokerGroupIdx= tableGroups.indexOf(jokerGroup);
        int tileGroupIdx= tableGroups.indexOf(tileGroup);

        assertTrue(state.canFreeJoker(0,jokerGroupIdx,tileGroupIdx ));

    }

    @Test
    public void canSplit() throws Exception {
        //todo
    }

    @Test
    public void canSimpleSplit() throws Exception {
        //todo
    }

    @Test
    public void isValidTable() throws Exception {
        //todo
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;


        ArrayList<TileGroup> tableGroups= state.getTableTileGroups();

        //adds a black 10, black 11, and black 13 to the table
        tableGroups.add(new TileGroup(new Tile(0,0,11,Tile.BLACK)));
        tableGroups.add(new TileGroup(new Tile(0,0,12,Tile.BLACK)));

        //shows that having those three separate solo tile groups
        //on the board isn't a valid table
        assertFalse(state.isValidTable());

        tableGroups.clear();

        //adds a valid run group to the table
        tableGroups.add(new TileGroup(new Tile(0,0,11,Tile.BLACK), new Tile(0,0,12,Tile.BLACK),new Tile(0,0,13,Tile.BLACK)));

        //shows that the board is valid with a run on the board
        assertTrue(state.isValidTable());

        tableGroups.clear();

        //adds a valid book to the board
        tableGroups.add(new TileGroup(new Tile(0,0,10,Tile.BLUE), new Tile(0,0,10,Tile.BLACK),new Tile(0,0,10,Tile.RED)));

        assertTrue(state.isValidTable());

    }

    @Test
    public void canPlayTile() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        //plays the first tile in the players hand, player 1
        assertTrue(state.canPlayTile(0,0));

        //plays the first tile in player 2 hand
        assertTrue(state.canPlayTile(1,0));

        //plays a tile index that is bigger than the players hand
        assertFalse(state.canPlayTile(0,(state.getPlayerHand(0).groupSize())+10));

        //an invalid tile index(-2)
        assertFalse(state.canPlayTile(1,-2));
    }

    @Test
    public void canPlayTileGroup() throws Exception {
        //todo help

        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        TileGroup testGroup = new TileGroup(new Tile(0,0,3,Tile.BLACK),
                new Tile(0,0,7,Tile.GREEN));
        int[] tiles = {0,1};
        assertTrue(state.canPlayTileGroup(0, tiles));

        int[] tiles2 = {0,1,6,4,3,2,5,3};
        assertTrue(state.canPlayTileGroup(0, tiles2));
    }

    @Test
    public void isFromHand() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        //this tile is not from the players hand
        state.getTableTileGroups().add(new TileGroup(new Tile(0,0,7,Tile.BLACK)));
        assertFalse(state.isFromHand(state.getTableTileGroups().get(0)));

        //plays a tile from the players hand
        state.canPlayTile(0,0);
        assertTrue(state.isFromHand(state.getTableTileGroups().get(1)));

        //one tile from hand is connected with a tile that was on the board
        state.canConnect(0,0,1);
        assertFalse(state.isFromHand(state.getTableTileGroups().get(0)));

        //todo
    }

    @Test
    public void hasCurrentPlayerPlayed() throws Exception {
        RummikubLocalGame game= new RummikubLocalGame();

        GamePlayer[] players= {new RummikubHumanPlayer("Bob"),
                new RummikubComputerPlayer("Thalo")};

        game.start(players);
        RummikubState state= game.state;

        //this is false because the player hasnt played
        assertFalse(state.hasCurrentPlayerPlayed());

        //player 0 plays a card from there hand
        state.canPlayTile(0,5);

        //shows if the player has played
        assertTrue(state.hasCurrentPlayerPlayed());

    }

}