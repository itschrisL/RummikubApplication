package edu.up.cs301.rummikub;


import android.util.Log;

import java.util.ArrayList;

import edu.up.cs301.game.infoMsg.GameState;

/**
 * Created on 3/26/2018.
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubState extends GameState{
    private int numPlayers; //number of players in the game

    // These instance variables are parallel to players[]
    // Instance variables for Player information
    private String[] players; //names of players
    private TileGroup[] playerHands; //groups of tiles in players' hands
    private int[] playerScores; //indicates score of each player
    private boolean[] playersMelded; //indicates whether each player has melded
    private int[] playersID; //parallel to players[], TODO do we need this?
    private int currentPlayer; //index of players[], indicates whose turn it is
    private boolean currentPlayerPlayed; //Boolean if Current player has made a move yet.
    // TODO possible variables timer int

    private int round;

    private TileGroup drawPile; //tiles that are not played/in player's hand

    private TileGroup selectedGroup; //the group on table that is selected by player
    //null if no group selected

    private ArrayList<TileGroup> tableTileGroups; //tiles and sets on the table

    // TODO add a previous tableTileGroup variable

    /**
     * RumikubState Constructor
     */
    public RummikubState() {
        this.numPlayers = 2;
        this.round = 1;
        this.players = new String[numPlayers];
        this.players[0] = "Matt";
        this.players[1] = "Nux";

        this.playersID = new int[numPlayers];
        playersID[0] = 0;
        playersID[1] = 1;

        initDrawPile();

        this.playerHands = new TileGroup[numPlayers];

        this.playerHands[0] = new TileGroup();
        this.playerHands[1] = new TileGroup();

        dealHands();

        this.playerScores = new int[numPlayers];
        this.playerScores[0] = 0;
        this.playerScores[1] = 0;

        this.playersMelded = new boolean[numPlayers];
        this.playersMelded[0] = false;
        this.playersMelded[1] = false;

        this.currentPlayer = 0;
        this.currentPlayerPlayed = false;
        this.selectedGroup = null;
        this.tableTileGroups = new ArrayList<TileGroup>();
    }

    /**
     * Copy constructor for gameState
     *
     * @param copy rummikubState to copy
     * @param playerIndex player that this is a copy for
     *                    if playerIndex == -1, a complete copy is made
     */
    public RummikubState (RummikubState copy, int playerIndex) {
        this.setThisToCopy(copy,playerIndex);
    }

    /**
     * Called from copy constructor for gameState
     * sets this RummikubState to a deep copy of copy
     *
     * @param copy rummikubState to copy
     * @param playerIndex player that this is a copy for
     *                    if playerIndex == -1, a complete copy is made
     */
    private void setThisToCopy(RummikubState copy, int playerIndex){
        if (-1 <= playerIndex && playerIndex < copy.numPlayers) {
            //copies num of players
            numPlayers = copy.numPlayers;
            round = copy.round;
            //copies names of players
            players = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                this.players[i] = new String(copy.players[i]);
            }

            //copies playersID array
            playersID = new int[numPlayers];
            for(int i = 0; i < numPlayers; i++){
                this.playersID[i] = copy.playersID[i];
            }

            //copies players' hands
            playerHands = new TileGroup[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                if (i == playerIndex || playerIndex == -1) {
                    this.playerHands[i] = new TileGroup(copy.playerHands[i]);
                }
                else {
                    this.playerHands[i] = null;
                }
            }

            //copies players' scores
            playerScores = new int[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerScores[i] = playerScores[i];
            }

            //copies boolean[] whether player melded or not
            playersMelded = new boolean[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playersMelded[i] = playersMelded[i];
            }

            //copies current player
            currentPlayer = copy.currentPlayer;

            //copies draw pile, invisible to all players
            if(playerIndex == -1){
                this.drawPile= new TileGroup(copy.drawPile);
            }
            else drawPile = null;

            //copies selectedGroup on table
            this.selectedGroup = copy.selectedGroup;

            //copies tableTileGroups
            tableTileGroups = new ArrayList<TileGroup>();
            for (TileGroup group : copy.tableTileGroups) {
                this.tableTileGroups.add(new TileGroup(group));
            }
        }
        else {
            Log.i ("state copy", "Invalid player index");
            System.exit(-1);
        }
    }

    /**
     * Initial Setup State's Draw Pile
     */
    private void initDrawPile(){
        drawPile = new TileGroup();
        for(int i = 0; i < 2; i++){
            for(int val = 1; val <= 13; val++){
                for(int col = 0; col <= 3; col++){
                    drawPile.add(new Tile(-1, -1, val, Tile.colorArray[col]));
                }
            }
        }

        drawPile.randomize();
    }

    /**
     * deals 14 tiles from drawpile to each player's hand
     */
    private void dealHands(){
        for(int i=0;i<14;i++){
            for(int j=0;j<numPlayers;j++){
                playerHands[j].add(drawPile.draw());
            }
        }

        //go through each hand and set the tiles x and y
        //to be drawn on the racks
        for(int i=0;i<numPlayers;i++){
            ArrayList<Tile> tiles= playerHands[i].getTileGroup();

            int xOffset= 20; //the offset to draw the tiles on the rack
            int yOffset= 20; //we don't want them to be right at the edge
            int tileX= xOffset;
            int tileY= yOffset;
            for(int j=0;j<tiles.size();j++){
                tiles.get(j).setX(tileX);
                tiles.get(j).setY(tileY);

                tileX+= Tile.WIDTH;
                if(j == 6){
                    tileX= xOffset;
                    tileY+= Tile.HEIGHT+yOffset;
                }
            }
        }
    }

    /**
     * Helper method that returns boolean if the given playerID has
     *
     * @param playerID
     * @return whether it is the player's turn
     */
    public boolean isPlayerTurn(int playerID){
        if(playerID == playersID[currentPlayer]){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * change variables for next player's turn
     */
    private void nextTurn(){
        currentPlayer++;
        if(currentPlayer >= numPlayers){
            currentPlayer = 0;
        }
        currentPlayerPlayed = false;
        selectedGroup = null;
    }

    /**
     * Method to draw and add tile to player's hand and update state
     *
     * @param playerID
     */
    private void giveTileToPlayer(int playerID){
        int p= getPlayerIndexByID(playerID);

        if (playerID == playersID[p]){
            playerHands[p].add(drawPile.draw());
        }

    }

    /**
     * Helper method which returns if a player can draw
     *
     * @param playerID
     * @return
     *  - false - if player has not made move and can't draw
     *  - true - if player has made move, end draw
     */
    public boolean canDraw(int playerID){
        if (isPlayerTurn(playerID)){
            if(!(currentPlayerPlayed)){
                giveTileToPlayer(playerID);
                nextTurn();
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method which returns if a player can knock
     *
     * @param playerID
     * @return
     *  - false - if player has not made move and can't knock
     *  - true - if player has made move, end turn
     */
    public boolean canKnock(int playerID){
        if(!isPlayerTurn(playerID)) return false;
        else if(!currentPlayerPlayed) return false;
        else if(!isValidTable()) return false; //todo java seems to skip to return statement
        else {
            Log.i("cool kids", "daylin ");
            nextTurn();
            return true;
        }
    }

    /**
     * Player can select the menu to display a popup
     * Returns false until menu popup function TODO update once menu setup
     *
     * @param playerID
     * @return
     */
    public boolean canShowMenu(int playerID){
        return false;
    }

    /**
     * todo implement
     *
     * @param playerID
     * @param tile
     * @return
     */
    public boolean canSelectTile(int playerID, Tile tile){
        if (isPlayerTurn(playerID)){
            if(playerHands[currentPlayer].contains(tile)){
                return true;
            }
        }
        return false;
    }

    /**
     * If group can be selected, then will highlight indicating selected group
     * Group cannot be selected if it is not player's turn
     *
     * @return whether group can be selected
     */
    public boolean canSelectGroup (int playerID, TileGroup group) {
        if (!isPlayerTurn(playerID)) return false;
        if (!isOnTable(group)) return false;

        selectedGroup = group;

        return true;
    }

    /**
     * connects/merges two TileGroups
     *
     * @param playerID the id of the player who is taking action
     * @param group1 the two groups to merge
     * @param group2
     * @return weather it was a valid move and merged
     */
    public boolean canConnect(int playerID, TileGroup group1, TileGroup group2){
        if(!isPlayerTurn(playerID)) return false;

        if(!isOnTable(group1) || !isOnTable(group2)) return false;

        group1.merge(group2);
        tableTileGroups.remove(group2);

        return true;
    }

    /**
     * splits a tile group on table into
     * several single-tile tile groups on table
     */
    public boolean canSplit(int playerId, TileGroup group){
        if(!isPlayerTurn(playerId)) return false;

        if(!isOnTable(group)) return false;

        ArrayList<Tile> tilesInGroup = group.getTileGroup();
        //go thru each tile in the tile group
        for(Tile tile : tilesInGroup){
            //add each tile to the table
            tableTileGroups.add(new TileGroup(tile));
        }

        //remove the group from the table
        tableTileGroups.remove(group);

        return true;
    }

    /**
     * Method to check if given TileGroup is on the current table
     * Calls .contains(group) from the class TileGroup
     * @param group
     * @return
     */
    private boolean isOnTable(TileGroup group){
        return tableTileGroups.contains(group);
    }

    /**
     * Checks every TileGroup of table is a valid set
     * Converts valid tile groups to tile sets
     *
     * @return whether every group on table is valid set
     */
    private boolean isValidTable(){
        // Iterate though TileGroups on table
        boolean isValidTable= true;
        for(TileGroup TG : tableTileGroups){
            if(TileSet.isValidSet(TG)) {
                TileSet tempSet = new TileSet(TG);
                tableTileGroups.add(tempSet);
                tableTileGroups.remove(TG);
            }
            else{ //we found an invalid set
                isValidTable= false;
            }
        }
        return isValidTable;
    }

    /**
     * moves a players tile from hand to table
     *
     * @param playerID the player trying to play
     * @param tile the tile in players hand
     * @return whether the tile was able to be played
     */
    public boolean canPlayTile(int playerID, Tile tile){
        if (!isPlayerTurn(playerID)) return false;
        int p = getPlayerIndexByID(playerID);

        if (!playerHands[p].contains(tile)) return false;

        TileGroup tg = new TileGroup();

        tg.add(tile);
        playerHands[p].remove(tile);
        tableTileGroups.add(tg);
        return true;
    }

    /**
     *
     * @param tiles
     * @param playerID
     */
    public void isMeld(TileGroup tiles, int playerID){
        int meldVal = tiles.groupPointValues();
        int i;
        if(meldVal >= 30){
            int p= getPlayerIndexByID(playerID);
            playersMelded[p] = true;
        }
    }

    /**
     * Getter method to get player index by player ID
     *
     * @param playerID
     * @return the index of player ID, -1 if not found
     */
    private int getPlayerIndexByID(int playerID){
        int i, index = -1;
        for(i = 0; i < players.length; i++){
            if (playersID[i] == playerID){
                index = i;
            }
        }
        return index;
    }

    /**
     * this state as a string
     * will be the variable name followed by a colon and newline
     * then the value of the variable
     * e.g. for numPlayers part of the string:
     * "numPlayers:\n
     * 2\n"
     *
     * in the case of arrays it will look like this:
     * "playerHands[0]:\n
     * B7,U8\n
     * playerHands[1]:\n
     * G8,R12\n"
     *
     * @return a string representation of this state
     */
    @Override
    public String toString(){
        String stateString= "";

        stateString+= getNumPlayerString();
        stateString+= getPlayersString();
        stateString+= getPlayersIDString();
        stateString+= getPlayerHandsString();
        stateString+= getPlayerScoresString();
        stateString+= getPlayersMeldedString();
        stateString+= getCurrentPlayerString();
        stateString+= getDrawPileString();
        stateString+= getTableTileGroupString();
        stateString+= getRoundString();

        return stateString;
    }

    /**
     *
     * @return string representation of the variable numPlayers
     */
    private String getNumPlayerString(){
        return "numPlayers:\n"+
                numPlayers+"\n";
    }

    /**
     *looks like:
     *
     * players[0]:
     * Matt
     * players[1]:
     * Nux
     *
     * @return string representation of the array players
     */
    private String getPlayersString(){
        //playerString is the string of the entire players array
        String playersString= "";
        for(int i=0;i<numPlayers;i++){
            //currPlayerString is each string in players
            String currPlayerString=
                    "players["+i+"]:\n";
            currPlayerString+= players[i];
            currPlayerString+= "\n";

            playersString+= currPlayerString;
        }

        return playersString;
    }

    /**
     *looks like:
     *
     * playerScores[0]:
     * 89
     * playerScores[1]:
     * -45
     *
     * @return string representation of the array playerScores
     */
    private String getPlayerScoresString(){
        //playerScoresString is the string of the entire playersScores array
        String playerScoresString= "";
        for(int i=0;i<numPlayers;i++){
            String currPlayerScoreString=
                    "playerScores["+i+"]:\n";
            currPlayerScoreString+= String.valueOf(playerScores[i]);
            currPlayerScoreString+= "\n";

            playerScoresString+= currPlayerScoreString;
        }

        return playerScoresString;
    }

    /**
     *looks like:
     *
     * playerHands[0]:
     * B6,R8
     * playerHands[1]:
     * G12,U5,B6
     *
     * @return string representation of the array playerHands
     */
    private String getPlayerHandsString(){
        //playerHandsString is the string of the entire playersHands array
        String playerHandsString= "";
        for(int i=0;i<numPlayers;i++){
            if(playerHands[i] == null) continue;
            //currPlayerHandsString is each string in of a single player's hand
            String currPlayerHandsString=
                    "playerHands["+i+"]:\n";
            currPlayerHandsString+= playerHands[i].toString();
            currPlayerHandsString+= "\n";

            playerHandsString+= currPlayerHandsString;
        }

        return playerHandsString;
    }

    /**
     * looks like:
     *
     * playersMelded[0]:
     * T
     * playersMelded[1]:
     * F
     *
     * @return string representation of the array playersMelded
     */
    private String getPlayersMeldedString(){
        //playersMeldedString is the string of the entire playersMelded array
        String playersMeldedString= "";
        for(int i=0;i<numPlayers;i++){
            //currPlayerHandsString is each string in of a single player's hand
            String currPlayerMeldedString=
                    "playersMelded["+i+"]:\n";

            if(playersMelded[i]) currPlayerMeldedString+= "T";
            else currPlayerMeldedString+= "F";
            currPlayerMeldedString+= "\n";

            playersMeldedString+= currPlayerMeldedString;
        }

        return playersMeldedString;
    }

    /**
     *
     * @return string representation of the variable currentPlayer
     */
    private String getCurrentPlayerString(){
        return "currentPlayer:\n"+
                currentPlayer+"\n";
    }

    /**
     *
     * @return string representation of the variable drawPile
     */
    private String getDrawPileString(){
        if (drawPile == null) return "";
        return "drawPile:\n"+
                drawPile.toString()+"\n";
    }

    /**
     * looks like:
     *
     * tableTileGroups<0>:
     * B7
     * tableTileGroups<1>:
     * U8,B8,R8_Book
     * tableTileGroups<2>:
     * G4,G5,G6_Run
     *
     * @return string representation of the arraylist tableTileGroups
     */
    private String getTableTileGroupString(){
        String tableGroupsString= "";
        for(int i=0;i<tableTileGroups.size();i++){
            TileGroup currGroup= tableTileGroups.get(i);

            //currGroupString is each group in the arraylist as a string
            String currGroupString=
                    "tableTileGroups<"+i+">:\n";

            currGroupString+= currGroup.toString();
            currGroupString+= "\n";

            tableGroupsString+= currGroupString;
        }

        return tableGroupsString;
    }

    /**
     * Getter method for round integer
     * @return
     */
    private String getRoundString(){
        return "round:\n"+
                round+"\n";
    }

    /**
     *looks like:
     *
     * playersID[0]:
     * 0
     * playersID[1]:
     * 1
     *
     * @return string representation of the array playersID
     */
    private String getPlayersIDString(){
        //playersIDString is the string of the entire playersID array
        String playersIDString= "";
        for(int i=0;i<numPlayers;i++){
            String currPlayerIDString=
                    "playersID["+i+"]:\n";
            currPlayerIDString+= String.valueOf(playersID[i]);
            currPlayerIDString+= "\n";

            playersIDString+= currPlayerIDString;
        }

        return playersIDString;
    }

    public ArrayList<TileGroup> getTableTileGroups(){
        return tableTileGroups;
    }


    public TileGroup[] getPlayerHands(){
        return playerHands;
    }
}
