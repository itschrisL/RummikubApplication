package edu.up.cs301.rummikub;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        GameAction action= null;

        if(view == hand){
            //get our hand
            TileGroup handGroup= state.getPlayerHand(playerNum);
            //find the index of the tile we touched
            int touchedTile=
                    handGroup.hitTile(motionEvent.getX(),motionEvent.getY());

            //if we touched a tile
            if(touchedTile != -1){
                //create a select tile action
                action= new RummikubPlayTileAction(this,touchedTile);
            }
        }

        //if we made an action
        if(action != null){
            //send it
            game.sendAction(action);
        }

        return false;
    }

    protected void updateDisplay(){
        //updates the gui

        updateHand();
        updateTable();

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
}
