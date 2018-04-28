package edu.up.cs301.rummikub;

import java.util.ArrayList;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * @author Andrew M. Nuxoll
 * @author Steven R. Vegdahl
 * @version July 2013
 *
 * this is the primary activity for Rummikub Application
 *
 * How to use GUI:
 * Orientation: plays in both portrait and landscape
 * Select tile/tileGroup on table: press tile/tileGroup on table
 * Play tile: press single tile from hand
 * Return tile from table to hand: first select tile, then touch it again
 * Connecting two groups: select first group, then tap second group
 * Splitting tileGroups: first select group, then press specific tile where
 * 						you want group to split
 * Freeing the joker: first select group containing joker, then press tile
 * 						that will free the joker
 * 			If you wish to connect a tile to a group containing a joker,
 * 				select the single tile first
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 *
 */
public class RummikubMainActivity extends GameMainActivity {
	
	// the port number that this game will use when playing over the network
	private static final int PORT_NUMBER = 2234;

	/**
	 * Create the default configuration for this game:
	 * - one human player vs. one computer player
	 * - minimum of 1 player, maximum of 4
	 * - two kinds of computer player and one kind of human player available
	 * 
	 * @return
	 * 		the new configuration object, representing the default configuration
	 */
	@Override
	public GameConfig createDefaultConfig() {
		
		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		// a human player player type (player type 0)
		playerTypes.add(new GamePlayerType("Local Human Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubHumanPlayer(name);
			}});

		// a computer player type (player type 1)
		playerTypes.add(new GamePlayerType("Very Dumb Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubComputerPlayer(name);
			}});

		// a computer player type (player type 2)
		playerTypes.add(new GamePlayerType("Normal Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubComputerPlayer1(name);
			}});

		// a computer player type (player type 3)
		playerTypes.add(new GamePlayerType("Smart Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubComputerPlayer2(name);
			}});

		// Create a game configuration class for Rummikub:
		// - player types as given above
		// - from 1 to 2 players
		// - name of game is "Counter Game"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes,2,4,"Rummikub",
				PORT_NUMBER);

		// Add the default players to the configuration
		defaultConfig.addPlayer("Human", 0); // player 1: a human player
		defaultConfig.addPlayer("Two Tiles", 1); // player 2: a computer player
		
		// Set the default remote-player setup:
		// - player name: "Remote Player"
		// - IP code: (empty string)
		// - default player type: human player
		defaultConfig.setRemoteData("Remote Player", "", 0);
		
		// return the configuration
		return defaultConfig;
	}//createDefaultConfig

	/**
	 * create a local game
	 * 
	 * @return the local game, a Rummikub game
	 */
	@Override
	public LocalGame createLocalGame() {
		return new RummikubLocalGame();
	}

}
