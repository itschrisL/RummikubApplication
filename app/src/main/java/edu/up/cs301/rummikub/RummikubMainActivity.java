package edu.up.cs301.rummikub;

import java.util.ArrayList;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * this is the primary activity for Rummikub Application
 *
 * - Enhancements
 * The joker tile would display the value and color of its assigned value in the
 * tile set on the table.  This value would only show up if the group containing the joker is
 * confirmed a valid set or book.  This is usually whenever the player ends their turn by pressing
 * knock or draw.  The joker values would diaper whenever the joker isn't part of a
 * proper set of run
 *
 * Known Bugs/Issues before Beta Testing
 * - Joker Value would disappear - If a human player were to try and split a group of tiles on the
 * table witch contained a joker the joker value would disappear when deselecting the tiles.
 * This was a issue that was found during our Beta Testing in class.
 * We discovered this bug before our testing session but discovered that the code to fix this issue
 * wasn't added to the version before class.  This was due to either a merge error or human error.
 * The Joker value was getting reset in the State before we would remove it form the current group.
 * In the case of the joker, the group can't be split due to the Freeing the Joker Rule.
 * Since the State thought that we would split the group it reset the joker tile prematurely.
 * While people were testing for failure the encountered this error.  We believe that this bug may
 * have been the cause to some of the network play bugs involving the joker tile not updating for
 * everyone.  Since the Beta Testing we have addressed this issue and are testing it further to see
 * if this indeed fixed the bug.
 * - Joker Value would be wrong for books, - This was the same issue as above and was fixed.  The
 * joker color value was being assigned before the other tiles in the group were checked.  This was
 * fixed by assigning the joker values after the other tiles in the book are checked.  Then the
 * joker color is set to one of the remaining unused colors in the book.
 * - AI would get stuck - While testing our game before class we didn't encounter the issue of the
 * the AI getting stuck during their turn.  We believe that we know the cause of the issue.  This
 * only happened when the smart AI would decide on what move to make.  This might have been
 * caused by an issue involving checking if a group containing a joker is valid.  In that case, the
 * joker bugs above may have played a factor into this as well.
 *
 *
 *
 * @author Andrew M. Nuxoll
 * @author Steven R. Vegdahl
 * @version July 2013
 *
 * Update: this is primary activity for Rummikub game
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
		playerTypes.add(new GamePlayerType("Smarter Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubComputerPlayer1(name);
			}});

		// a computer player type (player type 3)
		playerTypes.add(new GamePlayerType("Smart Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new RummikubComputerPlayer2(name);
			}});

		// Create a game configuration class for Counter:
		// - player types as given above
		// - from 1 to 2 players
		// - name of game is "Counter Game"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4, "Rummikub",
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
	 * @return
	 * 		the local game, a counter game
	 */
	@Override
	public LocalGame createLocalGame() {
		return new RummikubLocalGame();
	}

}
