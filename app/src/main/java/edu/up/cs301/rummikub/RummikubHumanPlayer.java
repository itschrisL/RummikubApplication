package edu.up.cs301.rummikub;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;
import edu.up.cs301.rummikub.action.RummikubRevertAction;
import edu.up.cs301.rummikub.action.RummikubSelectTileAction;
import edu.up.cs301.rummikub.action.RummikubSelectTileGroupAction;
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

    private TextView player1Score;
    private TextView player2Score;
    private TextView player1Tiles;
    private TextView player2Tiles;

    private Button drawKnockButton;
    private Button undoButton;
    private Button revertButton;

    private GameBoard table;
    private Hand hand;

    private GameMainActivity myActivity;
    private RummikubState state;

    //todo surfaceViews perhaps

    public RummikubHumanPlayer(String name){
        super(name);
    }
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

        //initializes text views
        this.player1Tiles =
                (TextView) activity.findViewById(R.id.textViewPlayer1Tiles);
        this.player2Tiles =
                (TextView) activity.findViewById(R.id.textViewPlayer2Tiles);
        this.player1Score =
                (TextView) activity.findViewById(R.id.textViewPlayer1Score);
        this.player2Score =
                (TextView) activity.findViewById(R.id.textViewPlayer2Score);

        // if we have a game state, "simulate" that we have just received
        // the state from the game so that the GUI values are updated
        if (state != null) {
            receiveInfo(state);
        }


    }

    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    public void receiveInfo(GameInfo info) {
        // ignore the message if it's not a RummikubState message
        if (!(info instanceof RummikubState)) return;

        // update our state; then update the display
        this.state = (RummikubState) info;
        updateDisplay();
    }

    public void onClick(View view) {
        //we might not have a game to send actions to
        if(game == null) return;

        GameAction action= null;

        if(view == drawKnockButton){
            if(state.hasCurrentPlayerPlayed()){
                action= new RummikubKnockAction(this);
            }
            else {
                action = new RummikubDrawAction(this);
            }
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

    public boolean onTouch(View view, MotionEvent motionEvent) {
        //we might not have a game to send actions to
        if(game == null) return false;


        float x= motionEvent.getX();
        float y= motionEvent.getY();
        GameAction action= null;

        if(view == hand){
            //see if we should play a tile
            action= playTileAction(x,y,state.getPlayerHand(playerNum));
        }
        else if(view == table){
            action= selectTileAction(x,y,state.getTableTileGroups());
        }

        //if we made an action
        if(action != null){
            //send it
            game.sendAction(action);
        }

        return false;
    }

    /**
     * creates a play tile action
     * @param x the x-coord of the touch event
     * @param y the y-ccord of the touch event
     * @param hand the TileGroup that is this players hand
     * @return an action to play a tile
     *          null is no tile should be played
     */
    private RummikubPlayTileAction playTileAction(float x, float y, TileGroup hand){
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
     * creates a play tile action
     * @param x the x-coord of the touch event
     * @param y the y-ccord of the touch event
     * @param tableGroup the TileGroups on the table
     * @return an action to play a tile
     *          null is no tile should be played
     */
    private RummikubSelectTileGroupAction
                selectTileAction(float x, float y, ArrayList<TileGroup> tableGroup){
        int selectedGroup= -1;

        for(int i=0; i < tableGroup.size(); i++){
            //if we hit group
            if(tableGroup.get(i).hitTile(x,y) != -1){
                //select this group
                selectedGroup= i;
                //we found what we hit, so leave the loop
                break;
            }
        }

        //if we selected a tile
        if(selectedGroup != -1){
            return new RummikubSelectTileGroupAction(this,selectedGroup);
        }

        //we didn't select a tile
        return null;
    }

    protected void updateDisplay(){
        //updates the gui

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
            drawKnockButton.setText("Knock");
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
    private void updateTextViews(){
        player1Tiles.setText( name + " " + state.getPlayerHand(0).groupSize());
        player2Tiles.setText( allPlayerNames[1] + " " + state.getPlayerHand(1).groupSize());

        player1Score.setText( name + " " + state.getScore(0));
        player2Score.setText( allPlayerNames[1] + " " + state.getScore(1));
    }
}
