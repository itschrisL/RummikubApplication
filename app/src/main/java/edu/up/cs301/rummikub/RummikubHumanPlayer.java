package edu.up.cs301.rummikub;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubFreeJokerAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;
import edu.up.cs301.rummikub.action.RummikubReturnTileAction;
import edu.up.cs301.rummikub.action.RummikubRevertAction;
import edu.up.cs301.rummikub.action.RummikubSelectTileGroupAction;
import edu.up.cs301.rummikub.action.RummikubSplitAction;
import edu.up.cs301.rummikub.action.RummikubUndoAction;

/**
 * class RummikubHumanPlayer
 *
 * Creates and updates GUI display of Rummikub
 * for human player to see
 *
 * @author Harry Thoma
 * @author Daylin Kuboyama
 * @author Riley Snook
 * @author Chris Lytle
 */

public class RummikubHumanPlayer extends GameHumanPlayer
        implements View.OnClickListener, View.OnTouchListener {

    // References to text views displaying scores and tile counts
    private TextView playerScores;
    private TextView playerTileCount;

    // References to buttons
    private Button drawKnockButton;
    private Button undoButton;
    private Button revertButton;

    // References to surface views
    private GameBoard table;
    private Hand hand;

    // Reference to Main activity and state
    private GameMainActivity myActivity;
    private RummikubState state;

    /**
     * RummikubHumanPlayer Constructor
     *
     * @param name - player's name
     */
    public RummikubHumanPlayer(String name){
        super(name);
    }

    /**
     * Setting up gui and set up all needed buttons,
     * text views, and surface views
     *
     * @param activity
     */
    public void setAsGui(GameMainActivity activity) {

        //remember the activity
        myActivity = activity;

        // Load the layout resource for our GUI
        activity.setContentView(R.layout.rummikub_human_player);

        // make this object the listener for the buttons

        undoButton = (Button) activity.findViewById(R.id.ButtonUndo);
        undoButton.setOnClickListener(this);

        revertButton = (Button) activity.findViewById(R.id.ButtonRevert);
        revertButton.setOnClickListener(this);

        drawKnockButton = (Button) activity.findViewById(R.id.ButtonKnockDraw);
        drawKnockButton.setOnClickListener(this);


        table= (GameBoard) activity.findViewById(R.id.ViewGameBoard);
        table.setOnTouchListener(this);

        hand= (Hand) activity.findViewById(R.id.ViewHand);
        hand.setOnTouchListener(this);

        Button scrollButton=
                (Button) activity.findViewById(R.id.ButtonScroll);

        //set the listener for the scroll button
        scrollButton.setOnTouchListener(hand);

        //initializes text views
        this.playerScores =
                (TextView) activity.findViewById(R.id.textViewScoreBoard);
        this.playerTileCount =
                (TextView) activity.findViewById(R.id.textViewTileCount);

        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }


    }

    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * Receives any state changes and updates any displays
     *
     * @param info - given state info
     */
    public void receiveInfo(GameInfo info) {
        /**
         * External Citation
         * Date: 4/26/18
         * Problem: wanted to create a pop up message to show that the
         *      round has ended
         * Source: https://stackoverflow.com/questions/3500197/
         *      how-to-display-toast-in-android
         * Inspiration from Bohnanza team and referenced stack overflow
         * Solution: Looked over the question and solutions and then wrote
         *      the line of code below. Didn't copy the line exactly
         */
        if( info instanceof EndRoundInfo){
            Toast.makeText(myActivity,
                    "Round Over", Toast.LENGTH_SHORT).show();
        }
        // update our state if its a instance of rummikubstate;
        // then update the display
        if( info instanceof RummikubState) {
            this.state = (RummikubState) info;
        }

        updateDisplay();
    }

    /**
     * Responds to button clicks
     *
     * @param view - button being clicked
     */
    public void onClick(View view) {
        //we might not have a game to send actions to
        if(game == null) return;

        // Setting up action variable, initially no action
        GameAction action= null;

        //after any button touch, we don't want to see invalid groups
        table.setOutlineInvalidGroups(false);

        if(view == drawKnockButton){
            //if they want to knock
            if(state.hasCurrentPlayerPlayed()){
                //send the action
                action= new RummikubKnockAction(this);
            }
            //if they want to draw
            else {
                action = new RummikubDrawAction(this);
            }

            //draw the invalid groups
            table.setOutlineInvalidGroups(true);
            updateTable();
        }
        else if(view == undoButton){
            action= new RummikubUndoAction(this);
        }
        else if(view == revertButton){
            action= new RummikubRevertAction(this);
        }

        //if there is an action to send
        if(action != null){
            //send the action
            game.sendAction(action);
        }
    }

    /**
     * Responds to any touch on the surface views
     *
     * @param view - surface view being touched
     * @param motionEvent - Reference to touch event
     * @return - If there is a valid action
     *      - True - Valid action
     *      - False - Invalid touch
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //we might not have a game or state to send actions to
        if(game == null || state == null) return false;

        // Only responds to touch down event.  Else return invalid
        if(!(motionEvent.getAction() == MotionEvent.ACTION_DOWN)){
            return false;
        }

        //after any touch, we don't want to see invalid groups
        table.setOutlineInvalidGroups(false);

        //where player touched on screen
        float x= motionEvent.getX();
        float y= motionEvent.getY();
        GameAction action;

        //touched in player's hand
        if(view == hand){
            //see if we should play a tile
            action= playTileAction(x,y,state.getPlayerHand(playerNum));
            if (action != null) {
                game.sendAction(action);
                return true;
            }
        }
        //touched table
        else if(view == table){
            //get the table
            ArrayList<TileGroup> tableGroup= state.getTableTileGroups();

            //check for split first, because we don't want to
            //try to connect a group to itself
            action = splitAction(x,y,tableGroup);
            if( action != null){
                game.sendAction(action);
                return true;
            }

            //next, see if we want to return a tile to the hand
            action = returnTileAction(x,y,tableGroup);
            if(action != null) {
                game.sendAction(action);
                return true;
            }

            //check for free joker before connect, because it is a
            //more specific case
            action= freeJokerAction (x,y,tableGroup);
            if (action != null) {
                game.sendAction(action);
                return true;
            }

            //see if we want to connect tiles
            action= connectAction(x,y,tableGroup);
            if (action != null) {
                game.sendAction(action);
                return true;
            }

            //the last thing to check is select, because if we
            //check this first, it won't do anything else
            action= selectTileAction(x,y,tableGroup);
            if (action != null) {
                game.sendAction(action);
                return true;
            }
        }
        return false;
    }

    /**
     * creates a play tile action
     *
     * @param x the x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param hand the TileGroup that is this players hand
     * @return an action to play a tile
     *          null is no tile should be played
     */
    private RummikubPlayTileAction playTileAction(
            float x, float y, TileGroup hand){
        //find the index of the tile we touched
        int touchedTile=
                hand.hitTile(x,y);

        //if we touched a tile
        if(touchedTile != -1){
            //create a select tile action
            return new RummikubPlayTileAction(this,touchedTile);
        }

        //we didn't touch a tile
        return null;
    }

    /**
     * creates a select tile group action
     *
     * @param x the x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param tableGroup the TileGroups on the table
     * @return an action to select a group
     *           null is no tile group should be selected
     */
    private RummikubSelectTileGroupAction selectTileAction(
            float x, float y, ArrayList<TileGroup> tableGroup){
        //initially no group selected
        int selectedGroup= -1;

        //goes thru groups on table
        for(int i=0; i < tableGroup.size(); i++){
            //if we hit group
            if(tableGroup.get(i).hitTile(x,y) != -1){
                //select this group
                selectedGroup= i;
                //we found what we hit, so leave the loop
                break;
            }
        }

        return new RummikubSelectTileGroupAction(this,selectedGroup);
    }

    /**
     * creates a freeJoker action
     *
     * @param x the x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param tableGroup the TileGroups on the table
     * @return an action to free a joker
     *          null is no joker should be freed
     */
    private RummikubFreeJokerAction freeJokerAction
            (float x, float y, ArrayList<TileGroup> tableGroup) {

        //Step 1: find the selected group on table

        //the group selected on table
        TileGroup selectedTileGroup= state.getSelectedGroup();

        //no group selected
        if (selectedTileGroup == null) return null;

        //index of group selected on table
        int selectedGroupIndex= -1;

        //find the selected group
        for (int i= 0; i<tableGroup.size(); i++){
            //found selectedGroup on table
            if (tableGroup.get(i) == selectedTileGroup) {
                selectedGroupIndex= i;
                break;
            }
        }

        //Step 2: find the second group player touched

        //the index of tileGroup player touched on table
        int touchedGroupIndex = -1;

        //find index of tileGroup touched
        for (int i= 0; i<tableGroup.size(); i++) {
            //if tableGroup was touched
            if (tableGroup.get(i).hitTile(x,y) != -1) {
                touchedGroupIndex= i;
                break;
            }
        }

        //Step 3: checks to see if the two groups are valid

        //selected group does not contain joker
        if(!(selectedTileGroup.containsJoker())) return null;

        //if no group was touched
        if(touchedGroupIndex == -1) return null;

        //check that touched Group only has one tile
        if (tableGroup.get(touchedGroupIndex).groupSize()!=1) return null;

        //looks at each tile in the selected group
        for(Tile t : selectedTileGroup.tiles){
            if(t instanceof JokerTile){
               if(!((JokerTile) t).getAssigned()) {
                   return null;
               }
            }
        }
        return new RummikubFreeJokerAction(
                this,selectedGroupIndex,touchedGroupIndex);
    }


    /**
     * creates a connect action
     *
     * @param x the x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param tableGroup the TileGroups on the table
     * @return an action to connect two tile groups
     *          null is no tile groups should be connected
     */
    private RummikubConnectAction connectAction
            (float x, float y, ArrayList<TileGroup> tableGroup ) {

        //Step 1: finds selected group on table

        //the group selected on table
        TileGroup selectedTileGroup= state.getSelectedGroup();

        //no group selected
        if (selectedTileGroup == null) return null;

        //index of group selected on table
        int selectedGroupIndex= -1;

        //find the selected group
        for (int i= 0; i<tableGroup.size(); i++){
            //found selectedGroup on table
            if (tableGroup.get(i) == selectedTileGroup) {
                selectedGroupIndex= i;
                break;
            }
        }

        //Step 2: finding the second group the player touched

        //the index of tileGroup player touched on table
        int touchedGroupIndex = -1;

        //find index of tileGroup touched
        for (int i= 0; i<tableGroup.size(); i++) {
            //if tableGroup was touched
            if (tableGroup.get(i).hitTile(x,y) != -1) {
                touchedGroupIndex= i;
                break;
            }
        }

        //if same tile group
        if(touchedGroupIndex == selectedGroupIndex){
            return null;
        }

        //if we made an action
        if(touchedGroupIndex != -1){
            return new RummikubConnectAction
                    (this,selectedGroupIndex,touchedGroupIndex);

        }

        return null;
    }

    /**
     * creates a split tile action
     *
     * @param x x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param tableGroup the TileGroup that is this players hand
     * @return an action to split a tile group
     *          null is no tile group should be split
     */
    private RummikubSplitAction splitAction
    ( float x, float y, ArrayList<TileGroup> tableGroup ){

        //if there isn't a selected group
        if( state.getSelectedGroup() == null ) return null;

        //hit group is the group touched
        int hitGroup = -1;

        //hit tile is the index of the touched tile in hit group
        int hitTile = -1;

        //finds the touched tile in the selected group
        for( int i = 0; i < tableGroup.size(); i++){
            hitTile = tableGroup.get(i).hitTile(x,y);
            if( hitTile != -1) {
                hitGroup = i;
                break;
            }
        }

        //checks if no group was hit
        if( hitGroup == -1 ) return null;
        //checks to see if group is single tile
        if(tableGroup.get(hitGroup).groupSize() < 2 ) return null;
        //if hit group isn't the selected group you can't split
        if( !(tableGroup.get(hitGroup) == state.getSelectedGroup())){
            return null;
        }

        return new RummikubSplitAction(this, hitGroup, hitTile);
    }

    /**
     * creates a return tile action
     *
     * @param x x-coord of the touch event
     * @param y the y-coord of the touch event
     * @param tableGroup the TileGroup that is this players hand
     * @return an action to return a tile
     *          null is no tile should be returned
     */
    private RummikubReturnTileAction returnTileAction(
            float x, float y, ArrayList<TileGroup> tableGroup ){
        //the group selected on table
        TileGroup selectedTileGroup= state.getSelectedGroup();

        //no group selected
        if (selectedTileGroup == null) return null;

        //index of tile group touched
        int hitGroup = -1;
        //finds touched group
        for( int i = 0; i < tableGroup.size(); i++){
            int hitTile = tableGroup.get(i).hitTile(x,y);
            if( hitTile != -1) {
                hitGroup = i;
                break;
            }
        }

        //if no group was touched
        if( hitGroup == -1 ) return null;

        //if group size is larger than 1
        if(tableGroup.get(hitGroup).groupSize() > 1 ) return null;

        //if the touched group is the selected group
        if( selectedTileGroup == tableGroup.get(hitGroup)){
            return new RummikubReturnTileAction(this, hitGroup);
        }
        return null;
    }

    /**
     * updates everything on the gui
     */
    protected void updateDisplay(){
        updateHand();
        updateTable();
        updateTextViews();
        updateDrawKnock();
    }

    /**
     * changes button to either draw or knock depending
     * on current player's play status
     */
    private void updateDrawKnock() {

        if(state.hasCurrentPlayerPlayed()){
            if(state.getCurrentPlayer() == this.playerNum){
                drawKnockButton.setText("Knock");
            }
        }
        else{ //player has not played on table
            drawKnockButton.setText("Draw");
        }

    }

    /**
     * changes table to represent all sets played
     */
    private void updateTable(){
        table.setTileGroups(state.getTableTileGroups());
        table.setSelectedGroup(state.getSelectedGroup());
        table.invalidate();
    }

    /**
     * changes hand to represent the player's hand
     */
    private void updateHand(){
        TileGroup playerHand= state.getPlayerHand(playerNum);

        hand.setTiles(playerHand);
        hand.invalidate();
    }

    /**
     * updates the text views on the board to display updated information
     */
    private void updateTextViews() {

        //updates tile text view based on number of players
        playerTileCount.setText("");
        for (int i = 0; i < allPlayerNames.length; i++) {
            if( i == 0){
                if( i == state.getCurrentPlayer()){
                    playerTileCount.append( "*" + allPlayerNames[i] + "\n" +
                            state.getPlayerHand(i).groupSize());
                }
                else {
                    playerTileCount.append(allPlayerNames[i] + "\n" +
                            state.getPlayerHand(i).groupSize());
                }
            }
            else {
                if( i == state.getCurrentPlayer()) {
                    playerTileCount.append("\n" + "*" + allPlayerNames[i] +
                            "\n" + state.getPlayerHand(i).groupSize());
                }
                else{
                    playerTileCount.append("\n" + allPlayerNames[i] +
                            "\n" + state.getPlayerHand(i).groupSize());
                }
            }
        }

        //updates score text view based on number of players
        playerScores.setText("");
        for (int i = 0; i < allPlayerNames.length; i++) {
            if( i == 0){
                playerScores.append(allPlayerNames[i] + "\n" + state.getScore(i));
            }
            else{
                playerScores.append("\n" + allPlayerNames[i] + "\n" +
                        state.getScore(i));
            }
        }
    }
}
