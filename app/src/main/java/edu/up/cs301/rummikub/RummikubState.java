package edu.up.cs301.rummikub;


import android.util.Log;
import java.util.ArrayList;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * class RummikubState
 *
 * Currently has all tests to check if our actions work
 * Also saves each state of game - includes copy constructors
 * Includes all toString for action methods
 *
 * @author Daylin Kuboyama
 * @author Harry Thoma
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubState extends GameState{
    private int numPlayers; //number of players in the game

    private static final long serialVersionUID = 7737393762469851826L;

    // These instance variables are parallel to players[]
    // Instance variables for Player information
    private String[] players; //names of players
    private TileGroup[] playerHands; //groups of tiles in players' hands
    private int[] playerScores; //indicates score of each player
    private boolean[] playersMelded; //indicates whether each player has melded

    //the index of the winner
    private int winner= -1;

    private int currentPlayer; //index of players[], indicates whose turn it is
    private boolean currentPlayerPlayed; //Boolean if Current player has made a move yet.

    private int round;

    private TileGroup drawPile; //tiles that are not played/in player's hand

    private TileGroup selectedGroup; //the group on table that is selected by player
    //null if no group selected

    private TileGroup tilesFromHand; //tiles played from player's hand
    //null if no tiles played

    private ArrayList<TileGroup> tableTileGroups; //tiles and sets on the table


    /**
     * RumikubState Constructor
     */
    public RummikubState( int inPlayers ) {
        this.numPlayers = inPlayers;
        if( numPlayers == 2 || numPlayers == 4) this.round = 3;
        if( numPlayers == 3) this.round = 2;

        this.playerScores = new int[numPlayers];
        for( int i = 0; i < numPlayers; i++){
            this.playerScores[i] = 0;
        }
        newRound();
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

    private void newRound(){

        this.playerHands = new TileGroup[numPlayers];
        this.playersMelded = new boolean[numPlayers];
        this.tableTileGroups = new ArrayList<TileGroup>();

        this.selectedGroup = null;
        this.tilesFromHand= new TileGroup();
        winner = -1;
        this.currentPlayer = 0;
        this.currentPlayerPlayed = false;

        for( int i = 0; i < numPlayers; i++){
            playerHands[i] = new TileGroup();
            playersMelded[i] = false;
            this.playerHands[i] = new TileGroup();
        }

        initDrawPile();
        dealHands();
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

            //copies players' hands
            playerHands = new TileGroup[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                //if this is the player for whom we are making a copy
                if (i == playerIndex || playerIndex == -1) {
                    //make a copy of their hand
                    this.playerHands[i] =
                            new TileGroup(copy.playerHands[i]);
                }
                //if this is a different player's hand
                else {
                    //make a "hidden" copy
                    this.playerHands[i] =
                            new HiddenTileGroup(copy.playerHands[i]);
                }
            }

            //copies players' scores
            playerScores = new int[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerScores[i] = copy.playerScores[i];
            }

            //copies boolean[] whether player melded or not
            playersMelded = new boolean[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playersMelded[i] = copy.playersMelded[i];
            }

            //copies current player
            currentPlayer = copy.currentPlayer;
            currentPlayerPlayed= copy.currentPlayerPlayed;

            //copies draw pile, hidden to all players
            if(playerIndex == -1){
                this.drawPile= new TileGroup(copy.drawPile);
            }
            else drawPile = new HiddenTileGroup(copy.drawPile);

            //copies selectedGroup on table
            if(copy.selectedGroup == null){
                this.selectedGroup= null;
            }
            else{
                this.selectedGroup = new TileGroup(copy.selectedGroup);
            }

            //copies player's tiles played from hand
            tilesFromHand = new TileGroup(copy.tilesFromHand);

            //copies tableTileGroups
            tableTileGroups = new ArrayList<TileGroup>();
            for (TileGroup group : copy.tableTileGroups) {
                //if group is the selected group
                if(group == copy.selectedGroup){
                    //we don't want to make a new copy
                    this.tableTileGroups.add(this.selectedGroup);
                }
                //otherwise, make a new copy
                else {
                    this.tableTileGroups.add(new TileGroup(group));
                }
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
        for(int j = 0; j < 2; j++){
            JokerTile jokerTile = new JokerTile(-1, -1, 0, Tile.colorArray[4]);
            drawPile.add(jokerTile);
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
        playerHands[0].add(new JokerTile(0,0,0, Tile.ORANGE));
    }

    //gets players name from players array
    public String getPlayerName( int index){
        return players[index];
    }

    public int getCurrentPlayer(){ return currentPlayer; }

    //gets player score from playerScores array
    public int getScore( int index){
        return playerScores[index];
    }

    /**
     *
     * @param playerIdx
     * @return whether it is the player's turn
     */
    public boolean isPlayerTurn(int playerIdx){
        if(playerIdx == currentPlayer){
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
        tilesFromHand = new TileGroup();
    }

    /**
     * Method to draw and add tile to player's hand and update state
     *
     * @param playerIdx
     */
    private void giveTileToPlayer(int playerIdx){

            Tile drawTile= drawPile.draw();

            //checks to see if there is a tile to draw in pile
            if( drawTile == null ) return;


            drawTile.setX(500);
            drawTile.setY(300);

            playerHands[playerIdx].add(drawTile);
    }

    /**
     * Helper method which returns if a player can draw
     *
     * @param playerIdx
     * @return
     *  - false - if player has not made move and can't draw
     *  - true - if player has made move, end draw
     */
    public boolean canDraw(int playerIdx){
        if(!(currentPlayerPlayed) && isValidTable() == true) {
            giveTileToPlayer(playerIdx);
            nextTurn();
            return true;
        }
        return false;
    }

    /**
     * Helper method which returns if a player can knock
     *
     * @param playerIdx
     * @return
     *  - false - if player has not made move and can't knock
     *  - true - if player has made move, end turn
     */
    public boolean canKnock(int playerIdx){

        if(!currentPlayerPlayed) return false;
        else if(!isValidTable()) return false;
        else if (!playersMelded[playerIdx]) {
            //if player has not played 30 point meld
            if (tilesFromHand.groupPointValues() < 30) return false;
            playersMelded[playerIdx]= true;
        }

        //if the player played all their tiles
        if(playerHands[currentPlayer].groupSize() == 0){
            roundOver();
            return true;
        }

        nextTurn();
        return true;
    }

    /**
     * updates the scores when the round ends
     * checks to see if it was the final round of the game
     * if so it sets the winner
     */
    private void roundOver(){
        for( int i = 0; i < numPlayers; i++  ){
            playerScores[currentPlayer] +=  playerHands[i].roundGroupPointValues();
            playerScores[i] -= playerHands[i].roundGroupPointValues();
        }

        //checks to see if the game is completely over
        if( round == 0){
            winner = currentPlayer;
            return;
        }
        round--;
        newRound();
    }

    /**
     * Player can select the menu to display a popup
     * Returns false until menu popup function TODO update once menu setup
     *
     * @param playerIdx
     * @return
     */
    public boolean canShowMenu(int playerIdx){
        return false;
    }

    /**
     *
     * @param playerIdx
     * @param tile
     * @return
     */
    public boolean canSelectTile(int playerIdx, Tile tile){
        if(playerHands[currentPlayer].contains(tile)){
            return true;
        }
        return false;
    }

    /**
     * If group can be selected, then will highlight indicating selected group
     * Group cannot be selected if it is not player's turn
     *
     * @param group the index of the group to select
     *
     * @return whether group can be selected
     */
    public boolean canSelectGroup (int playerID, int group) {
        //if this is an invalid group index
        if (!(0 <= group && group < tableTileGroups.size())){
            selectedGroup= null;
            return true;
        }

        selectedGroup = tableTileGroups.get(group);

        return true;
    }

    /**
     * connects/merges two TileGroups
     *
     * @param playerIdx the index of the player who is taking action
     * @param g1 the two groups to merge
     * @param g2
     * @return weather it was a valid move and merged
     */
    public boolean canConnect(int playerIdx, int g1, int g2){

        if (g1 >= tableTileGroups.size() || g1<0) return false;
        if (g2 >= tableTileGroups.size() || g2<0 ) return false;

        TileGroup group1 = tableTileGroups.get(g1);
        TileGroup group2 = tableTileGroups.get(g2);

        if(!isOnTable(group1) || !isOnTable(group2)) return false;

        group1.merge(group2);
        tableTileGroups.remove(group2);

        //deselect groups after connecting
        selectedGroup= null;

        return true;
    }

    /**
     *
     * @param playerIdx
     * @param jokerGroup
     * @param tileGroup
     * @return
     */
    public boolean canFreeJoker (int playerIdx, int jokerGroup, int tileGroup) {

        TileGroup groupWithJoker = tableTileGroups.get(jokerGroup);
        TileGroup groupWithTile = tableTileGroups.get(tileGroup);

        //check to make sure joker is in jokerGroup
        if (!groupWithJoker.containsJoker()) return false;
        if (groupWithTile.groupSize() != 1) return false;


        int jokerIndex= -1;
        //find index of joker within its group
        for (int i= 0; i< groupWithJoker.groupSize(); i++) {
            if (groupWithJoker.getTile(i) instanceof JokerTile) {
                jokerIndex= i;
            }
        }

        Tile joker= groupWithJoker.getTile(jokerIndex);

        //swap joker and tile
        int jokerVal= joker.getValue();
        int jokerCol= joker.getColor();
        int tileVal= groupWithTile.getTile(0).getValue();
        int tileCol= groupWithTile.getTile(0).getColor();

        if (!(jokerVal == tileVal && jokerCol == tileCol)) return false;

        canConnect(playerIdx,jokerGroup,tileGroup);

        groupWithJoker.remove(joker); //remove joker from jokerGroup
        TileGroup singleJoker= new TileGroup(joker); //create new tileGroup
        tableTileGroups.add(singleJoker); //places joker tile as last tile

        return true;
    }

    /**
     * splits a tile group on table into
     * several single-tile tile groups on table
     */
    public boolean canSplit(int playerIdx, int groupIndex, int tileIndex){

        TileGroup group = tableTileGroups.get(groupIndex);
        if(!isOnTable(group)) return false;

        ArrayList<Tile> tilesInGroup = group.getTileGroup();

        //go thru each tile in the tile group
        for(Tile tile : tilesInGroup){
            //add each tile to the table
            if(tile instanceof JokerTile) {
                ((JokerTile) tile).setAssigned(false);
            }
        }

        TileGroup leftGroup = new TileGroup();
        for( int i = 0; i < tileIndex; i++){
            leftGroup.add(group.getTile(i));
        }

        TileGroup midGroup = new TileGroup(group.getTile(tileIndex));

        TileGroup rightGroup = new TileGroup();
        for (int i = tileIndex + 1; i < group.groupSize(); i++) {
            rightGroup.add(group.getTile(i));
        }

        //checks to see if any of the split groups contain a joker and
        //are less than 3 tiles
        if( leftGroup.containsJoker() == true && leftGroup.groupSize() < 3 ){
            return false;
        }
        else if( midGroup.containsJoker() == true){
            return false;
        }
        else if( rightGroup.containsJoker() == true && rightGroup.groupSize() < 3){
            return false;
        }

        //makes sure there are tiles in the left group before adding it
        if( leftGroup.groupSize() != 0){
            tableTileGroups.add(leftGroup);
        }

        //adds middle touched tile to the table as its own group
        tableTileGroups.add(midGroup);

        //makes sure there are tiles in the right group before adding it
        if( rightGroup.groupSize() != 0){
            tableTileGroups.add(rightGroup);
        }

        //remove the group from the table
        tableTileGroups.remove(group);

        selectedGroup= null;

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
    public boolean isValidTable(){
        ArrayList<TileGroup> validGroups=
                new ArrayList<TileGroup>();

        int validCount= 0;
        for(TileGroup tg : tableTileGroups){
            if(TileSet.isValidSet(tg)){
                validCount++;
            }
        }

        // Iterate though TileGroups on table

        boolean isValidTable= true;
        for (TileGroup TG : tableTileGroups) {
            if (TileSet.isValidSet(TG)) {
                validGroups.add(TG);
            } else { //we found an invalid set
                isValidTable = false;
            }
        }

        //we found the valid groups, go make them into sets
        for(TileGroup group : validGroups){
            //make the set we want to add
            TileSet tempSet = new TileSet(group);
            // If joker is in set create it's values
            tempSet.findJokerValues();
            //find where the old TileGroup version is
            int index= tableTileGroups.indexOf(group);
            //replace the old group version with the new set version
            tableTileGroups.set(index,tempSet);
        }

        return isValidTable;
    }

    /**
     * moves a players tile from hand to table
     *
     * @param playerIdx the player trying to play
     * @param tileIndex the tile in players hand
     * @return whether the tile was able to be played
     */
    public boolean canPlayTile(int playerIdx, int tileIndex){
        TileGroup hand= playerHands[playerIdx];

        //if this is an invalid index
        if (!(0<= tileIndex && tileIndex < hand.groupSize())){
            return false;
        }

        Tile tile= hand.getTile(tileIndex);

        TileGroup tg = new TileGroup();

        tg.add(tile);
        playerHands[playerIdx].remove(tile);
        tableTileGroups.add(tg);

        tilesFromHand.add(tile); //every tile player plays gets added to array

        currentPlayerPlayed= true;

        return true;
    }

    /**
     * Moves a TileGroup from hand to the table
     * @param playerIdx
     * @param tileIndexs
     * @return
     */
    public boolean canPlayTileGroup(int playerIdx, int[] tileIndexs){

        TileGroup hand = playerHands[playerIdx];
        TileGroup tg = new TileGroup();
        int i;
        //go through backward, because all indexes are in order
        //and we want to pull the back one first
        for(i = tileIndexs.length-1; i >= 0; i--){
            // If index is wihtin players hand
            if(!(0 <= tileIndexs[i] && tileIndexs[i] < hand.groupSize())){
                return false;
            }
            else {
                // Else add tile to tg which will be returned if method returns true
                tg.add(hand.getTile(tileIndexs[i]));

                //add each tile to tilesFromHand
                tilesFromHand.add(hand.getTile(tileIndexs[i]));

                playerHands[playerIdx].remove(hand.getTile(tileIndexs[i]));

            }
        }

        tableTileGroups.add(tg);
        currentPlayerPlayed = true;

        return true;
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

    public ArrayList<TileGroup> getTableTileGroups(){
        return tableTileGroups;
    }

    public TileGroup getDrawPile() {
        return drawPile;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getWinner() {
        return winner;
    }

    public TileGroup[] getPlayerHands(){
        return playerHands;
    }

    public TileGroup getPlayerHand(int playerIdx){
        return playerHands[playerIdx];
    }

    public boolean hasCurrentPlayerPlayed() {
        return currentPlayerPlayed;
    }

    public TileGroup getSelectedGroup() {
        return selectedGroup;
    }

    public boolean hasMelded(int playerIdx){ return playersMelded[playerIdx]; }

    public int getRound(){return round;}

}
