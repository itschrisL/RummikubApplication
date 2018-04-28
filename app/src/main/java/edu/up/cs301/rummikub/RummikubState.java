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
    private static final long serialVersionUID = 7737393762469851826L;

    private int numPlayers; //number of players in the game
    // These instance variables are parallel to each other
    private TileGroup[] playerHands; //groups of tiles in players' hands
    private int[] playerScores; //indicates score of each player
    private boolean[] playersMelded; //indicates whether each player has melded

    //the index of the winner
    private int winner= -1;

    //index of player, indicates whose turn it is
    private int currentPlayer;

    //Boolean if Current player has made a move yet.
    private boolean currentPlayerPlayed;

    // Round number
    private int round;

    //tiles that are not played/in player's hand
    private TileGroup drawPile;

    //the group on table that is selected by player
    //null if no group selected
    private TileGroup selectedGroup;

    //tiles played from player's hand
    //null if no tiles played
    private TileGroup tilesFromHand;

    //tiles and sets on the table
    private ArrayList<TileGroup> tableTileGroups;

    /**
     * RummikubState Constructor
     *
     * @param inPlayers - number of players
     */
    public RummikubState( int inPlayers ) {
        this.numPlayers = inPlayers;
        // Set round based on number of players
        if( numPlayers == 2 || numPlayers == 4) this.round = 3;
        else if( numPlayers == 3) this.round = 2;

        // Set all scores to 0
        this.playerScores = new int[numPlayers];
        for( int i = 0; i < numPlayers; i++){
            this.playerScores[i] = 0;
        }
        newRound(); // Start round
    }

    /**
     * Deep Copy constructor for gameState
     *
     * @param copy rummikubState to copy
     * @param playerIndex player that this is a copy for
     *                    if playerIndex == -1, a complete copy is made
     */
    public RummikubState (RummikubState copy, int playerIndex) {
        //if playerIndex is invalid
        if (!(-1 <= playerIndex && playerIndex < copy.numPlayers)) {
            // Shouldn't happen, for debugging purpose
            Log.i ("state copy", "Invalid player index");
            System.exit(-1);
        }

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


        //copy tilesFromHand
        this.tilesFromHand= new TileGroup();
        //go through each tile group on table
        for(int i=0; i<copy.tableTileGroups.size();i++){
            TileGroup group= copy.tableTileGroups.get(i);
            //go through each tile on the table
            for(int j=0; j<group.groupSize(); j++){
                //if this tile is in copy's tileFromHand
                if(copy.tilesFromHand.contains(group.getTile(j))){
                    //add our tile to the group
                    this.tilesFromHand.add(
                            this.tableTileGroups.get(i).getTile(j));
                }
            }
        }
    }

    /**
     * initializes a new round
     */
    private void newRound(){

        this.playerHands = new TileGroup[numPlayers];
        this.playersMelded = new boolean[numPlayers];
        this.tableTileGroups = new ArrayList<TileGroup>();

        this.selectedGroup = null;
        this.tilesFromHand= new TileGroup();
        winner = -1;
        //makes sure a different player starts every round
        this.currentPlayer = round%numPlayers;
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
     * Initial Setup State's Draw Pile
     */
    private void initDrawPile(){
        drawPile = new TileGroup();
        //adds 2 tiles of each color and value
        for(int i = 0; i < 2; i++){
            for(int val = 1; val <= 13; val++){
                for(int col = 0; col <= 3; col++){
                    drawPile.add(new Tile(val, Tile.colorArray[col]));
                }
            }
        }

        //adds 2 jokers to drawPile
        for(int j = 0; j < 2; j++){
            JokerTile jokerTile = new JokerTile();
            drawPile.add(jokerTile);
        }

        //shuffles drawPile
        drawPile.randomize();
    }

    /**
     * deals 14 tiles from drawPile to each player's hand
     */
    private void dealHands(){
        for(int i=0;i<14;i++){
            for(int j=0;j<numPlayers;j++){
                playerHands[j].add(drawPile.draw());
            }
        }
    }

    /**
     * @param playerIdx
     * @return whether it is the player's turn
     */
    public boolean isPlayerTurn(int playerIdx){
        return playerIdx== currentPlayer;
    }

    /**
     * change variables for next player's turn
     */
    private void nextTurn(){
        //changes whose turn it is
        currentPlayer++;
        if(currentPlayer >= numPlayers){
            currentPlayer = 0;
        }

        //resets variables specific to each turn
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

            playerHands[playerIdx].add(drawTile);
    }

    /**
     * updates the scores when the round ends
     * checks to see if it was the final round of the game
     * if so it sets the winner
     */
    public void roundOver(){
        //updates player scores
        for( int i = 0; i < numPlayers; i++  ){
            //if round ended by someone winning
            if(drawPile.groupSize() != 0 ){
                //winner gets everyone's scores
                playerScores[currentPlayer] +=
                        playerHands[i].roundGroupPointValues();
            }

            //each player loses points in hand
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
     * draws a tile
     *
     * @param playerIdx
     * @return
     *  - false - if player has not made move and can't draw
     *  - true - if player has made move, end draw
     *
     *  @throws RuntimeException
     *  if the draw was unsuccessful because the round ended
     */
    public boolean canDraw(int playerIdx){
        //if draw pile is empty
        if( drawPile.groupSize()== 0){
            roundOver();
            throw new RuntimeException("Reset Round");
        }

        //if player is able to draw
        if(!(currentPlayerPlayed) && isValidTable()) {
            giveTileToPlayer(playerIdx);
            nextTurn();

            return true;
        }
        return false;
    }

    /**
     * player knocks
     *
     * @param playerIdx
     * @return
     *  - false - if player has not made move and can't knock
     *  - true - if player has made move, end turn
     *@throws RuntimeException
     *  if knock resulted in round ending
     */
    public boolean canKnock(int playerIdx){
        if(!currentPlayerPlayed) return false;
        else if(!isValidTable()) return false;
        else if (!playersMelded[playerIdx]) {
            //if player has not played 30 point meld
            if (tilesFromHand.groupPointValues() < 30) return false;
            //now player has melded
            playersMelded[playerIdx]= true;
        }

        //if the player played all their tiles
        if(playerHands[currentPlayer].groupSize() == 0){
            roundOver();
            throw new RuntimeException("Reset Round");

        }

        nextTurn();
        return true;
    }

    /**
     * If group can be selected, then will highlight indicating selected group
     * Group cannot be selected if it is not player's turn
     *
     * @param group the index of the group to select
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
     * @param g1 first group
     * @param g2 second group
     * @return whether it was a valid move and merged
     */
    public boolean canConnect(int playerIdx, int g1, int g2){

        //makes sure valid groups exist
        if (g1 >= tableTileGroups.size() || g1<0) return false;
        if (g2 >= tableTileGroups.size() || g2<0 ) return false;

        TileGroup group1 = tableTileGroups.get(g1);
        TileGroup group2 = tableTileGroups.get(g2);

        //if player has not melded
        if (!playersMelded[playerIdx]) {
            //if either group is not completely from player's hand
            if (! isFromHand(group1) || ! isFromHand(group2)) return false;
        }

        group1.merge(group2);
        tableTileGroups.remove(group2);

        //deselect groups after connecting
        selectedGroup= null;

        return true;
    }

    /**
     * returns tile to player's hand
     *
     * @param playerIdx index of player taking action
     * @param groupIndex index of tileGroup
     * @return whether returned tile to player's hand
     */
    public boolean canReturnTile(int playerIdx, int groupIndex){
        //makes sure valid groups exist
        if (groupIndex >= tableTileGroups.size() || groupIndex<0) return false;

        Tile tile = tableTileGroups.get(groupIndex).getTile(0);

        //checks to see if the tile selected is from player's hand
        if(tilesFromHand.contains(tile)){
            playerHands[playerIdx].add(tile);
            tableTileGroups.remove(groupIndex);
            tilesFromHand.remove(tile);
        }
        else {
            return false;
        }
        // If tiles from hand is 0, meaning he hasn't played any tiles
        // then current player hasn't made a move
        if(tilesFromHand.groupSize() == 0){
            currentPlayerPlayed = false;
        }

        selectedGroup = null;

        return true;
    }

    /**
     * frees a joker using a tile
     *
     * @param playerIdx index of player taking action
     * @param jokerGroup group that contains joker
     * @param tileGroup tile used to free the joker
     * @return whether joker was freed by tile
     */
    public boolean canFreeJoker (int playerIdx, int jokerGroup, int tileGroup){

        //gets the tileGroups
        TileGroup groupWithJoker = tableTileGroups.get(jokerGroup);
        TileGroup groupWithTile = tableTileGroups.get(tileGroup);

        //check to make sure joker is in jokerGroup
        if (!groupWithJoker.containsJoker()) return false;
        if (groupWithTile.groupSize() != 1) return false;

        //index of joker within jokerGroup
        int jokerIndex= -1;

        ArrayList<JokerTile> jokersInSet = new ArrayList<JokerTile>();

        //find index of joker within its group
        for (int i= 0; i< groupWithJoker.groupSize(); i++) {
            if (groupWithJoker.getTile(i) instanceof JokerTile) {
                jokerIndex= i;
                jokersInSet.add((JokerTile)groupWithJoker.getTile(i));
            }
        }

        Tile joker= groupWithJoker.getTile(jokerIndex);

        //swap joker and tile
        int jokerVal= joker.getValue();
        int jokerCol= joker.getColor();
        int tileVal= groupWithTile.getTile(0).getValue();
        int tileCol= groupWithTile.getTile(0).getColor();

        // If groupWithJoker is a book, go through and figure out what are the
        // available colors for the joker
        boolean[] colorBooleanList = new boolean[4];
        //represents if available color matches tile color
        boolean found = false;

        if(TileSet.isBook(groupWithJoker)){
            colorBooleanList = groupWithJoker.findColorsInGroup();

            //iterates through tiles in group and finds available colors
            for(int i = 0; i < colorBooleanList.length; i++){
                // If target tile isn't a color of the
                if(!colorBooleanList[i]){
                    if(tileCol == Tile.colorArray[i]){
                        found = true;
                    }
                }
            }
            if(!found) return false;
        }
        // If there is more then one joker in this run
        else if(jokersInSet.size() > 1) {
            boolean match = false; // Boolean for if tile matches joker

            for(JokerTile tile : jokersInSet){
                // Check if any jokers match the target tile
                if(tile.getJokerVal() == tileVal &&
                        tile.getColor() == tileCol){
                    match = true;
                }
            }
            // If no match found return false.
            if(!match){
                return false;
            }
        }
        else if (!(jokerVal == tileVal && jokerCol == tileCol)) return false;

        //canConnect(playerIdx,jokerGroup,tileGroup);
        Tile replaceTile = groupWithTile.getTile(0);
        groupWithJoker.tiles.add(jokerIndex,replaceTile);
        tableTileGroups.remove(tileGroup);

        groupWithJoker.remove(joker); //remove joker from jokerGroup
        ((JokerTile)joker).setJokerAssigned(false); // reset joker values
        TileGroup singleJoker= new TileGroup(); //create new tileGroup
        singleJoker.add(joker);
        tableTileGroups.add(singleJoker); //places joker tile as last tile

        return true;
    }

    /**
     * splits a tile group on table into
     * several single-tile tile groups on table
     *
     * @param playerIdx index of player taking action
     * @param groupIndex index of group trying to split
     * @param tileIndex index of tile to split
     */
    public boolean canSplit(int playerIdx, int groupIndex, int tileIndex){

        //makes sure valid groups exist
        if (groupIndex >= tableTileGroups.size() || groupIndex<0) return false;

        TileGroup group = tableTileGroups.get(groupIndex);

        //if player has not melded
        if (!playersMelded[playerIdx]) {
            //if group is not completely from player's hand
            if (! isFromHand(group)) return false;
        }

        ArrayList<Tile> tilesInGroup = group.getTileGroup();

        //creates group of every tile to the left of tile index
        TileGroup leftGroup = new TileGroup();
        for( int i = 0; i < tileIndex; i++){
            leftGroup.add(group.getTile(i));
        }

        //creates a group with just the tileIndex tile
        TileGroup midGroup = new TileGroup(group.getTile(tileIndex));

        //creates a group of every tile to the left of tile index
        TileGroup rightGroup = new TileGroup();
        for (int i = tileIndex + 1; i < group.groupSize(); i++) {
            rightGroup.add(group.getTile(i));
        }

        //checks to see if any of the split groups contain a joker and
        //are less than 3 tiles
        if( leftGroup.containsJoker() && leftGroup.groupSize() < 3 ){
            return false;
        }
        else if( midGroup.containsJoker()){
            return false;
        }
        else if( rightGroup.containsJoker() && rightGroup.groupSize() < 3){
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

        //go thru each tile in the tile group
        for(Tile tile : tilesInGroup){
            //add each tile to the table
            if(tile instanceof JokerTile) {
                ((JokerTile) tile).setJokerAssigned(false);
            }
        }

        //remove the group from the table
        tableTileGroups.remove(group);

        selectedGroup= null;

        return true;
    }

    /**
     * pulls the designated tile out of the designated group
     * and adds it to the back of the table
     * Computer-use only
     *
     * @param playerIdx the player making the action
     * @param groupIndex the group to split
     * @param tileIndex the tile to pull out
     * @return whether the split happened
     */
    public boolean canSimpleSplit(
            int playerIdx, int groupIndex, int tileIndex){
        //out of bounds index
        if(!(0 <= groupIndex && groupIndex < tableTileGroups.size())){
            return false;
        }

        TileGroup group= tableTileGroups.get(groupIndex);
        //out of bounds tile index
        if(!(0 <= tileIndex && tileIndex < group.groupSize())){
            return false;
        }

        //can't simple split a joker group
        if(group.containsJoker()){
            return false;
        }

        Tile splitTile= group.getTile(tileIndex);

        group.remove(splitTile);

        tableTileGroups.add(new TileGroup(splitTile));
        return true;
    }

    /**
     * Checks every TileGroup on table to make sure is a valid set
     * Converts valid tile groups to tile sets
     *
     * @return whether every group on table is valid set
     */
    public boolean isValidTable(){
        ArrayList<TileGroup> validGroups=
                new ArrayList<TileGroup>();

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

            // If joker is in set create its values
            tempSet.numericalOrder();

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

        Tile tile= hand.getTile(tileIndex); //tile from player's hand

        TileGroup tg = new TileGroup();
        tg.add(tile); //added to new tileGroup
        playerHands[playerIdx].remove(tile);
        tableTileGroups.add(tg);

        tilesFromHand.add(tile); //every tile player plays gets added to array

        currentPlayerPlayed= true;

        return true;
    }

    /**
     * Moves a TileGroup from hand to the table
     * Computer-use only
     *
     * @param playerIdx index of player taking action
     * @param tileIndexs array of indexes in hand that make up set you
     *                    want to play
     * @return whether tileGroup was played
     */
    public boolean canPlayTileGroup(int playerIdx, int[] tileIndexs){

        TileGroup hand = playerHands[playerIdx];
        TileGroup tg = new TileGroup();

        int i;
        //go through backward, because all indexes are in order
        //and we want to pull the back one first
        for(i = tileIndexs.length-1; i >= 0; i--){
            // If index is within players hand
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
     * Checks if specific group contains any tiles NOT from player's hand
     *
     * @param group the group checking
     * @return whether every tile is from player's hand
     */
    public boolean isFromHand (TileGroup group ){
        //checks each tile in group
        for (int i= 0; i< group.groupSize(); i++) {
            //if tile was NOT from player's hand
            if (!tilesFromHand.contains(group.getTile(i))) {
                 return false;
            }
        }
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

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getWinner() {
        return winner;
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

    public boolean[] getPlayersMelded() {return playersMelded;}

    public int getRound(){return round;}

    public int getCurrentPlayer(){ return currentPlayer; }

    public int getScore( int index){
        return playerScores[index];
    }

}
