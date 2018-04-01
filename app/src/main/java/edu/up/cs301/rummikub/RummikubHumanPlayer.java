package edu.up.cs301.rummikub;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.up.cs301.counter.CounterState;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.rummikub.action.RummikubDrawAction;
import edu.up.cs301.rummikub.action.RummikubKnockAction;

/**
 * Created by snook on 3/26/2018.
 *
 *
 */

public class RummikubHumanPlayer extends GameHumanPlayer
        implements View.OnClickListener, View.OnTouchListener {

    private TextView player1Score;
    private TextView player2Score;
    private TextView player1Tiles;
    private TextView player2Tiles;

    private Button drawKnockButton;

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

        Button undoButton = (Button) activity.findViewById(R.id.ButtonUndo);
        undoButton.setOnClickListener(this);
        Button revertButton = (Button) activity.findViewById(R.id.ButtonRevert);
        revertButton.setOnClickListener(this);
        drawKnockButton = (Button) activity.findViewById(R.id.ButtonKnockDraw);
        drawKnockButton.setOnClickListener(this);

        table= (GameBoard) activity.findViewById(R.id.ViewGameBoard);
        hand= (Hand) activity.findViewById(R.id.ViewHand);


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
        // ignore the message if it's not a CounterState message
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

        //if there is an action to send
        if(action != null){
            //send the action
            game.sendAction(action);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }

    protected void updateDisplay(){
        //updates the gui

        updateHand();
        updateTable();

        updateDrawKnock();


    }

    private void updateDrawKnock() {
        if(state.hasCurrentPlayerPlayed()){
            drawKnockButton.setText("Knock");
        }
        else{
            drawKnockButton.setText("Draw");
        }
    }

    private void updateTable(){
        table.setTileGroups(state.getTableTileGroups());
        table.invalidate();
    }

    private void updateHand(){
        TileGroup[] hands= state.getPlayerHands();

        //the non-null hand is this players hand
        TileGroup playerHand= null;
        for(int i=0; i<hands.length; i++){
            if(hands[i] != null){
                playerHand= hands[i];
                break;
            }
        }

        hand.setTiles(playerHand);
        hand.invalidate();
    }
}
