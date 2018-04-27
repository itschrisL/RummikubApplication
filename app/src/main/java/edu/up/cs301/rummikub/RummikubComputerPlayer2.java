package edu.up.cs301.rummikub;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.rummikub.RummikubComputerPlayer;
import edu.up.cs301.rummikub.action.RummikubComputerSplitAction;
import edu.up.cs301.rummikub.action.RummikubConnectAction;
import edu.up.cs301.rummikub.action.RummikubPlayTileAction;

/**
 * This is a smart computer player
 * it will play tiles out of it's hand
 * and rearrange the table
 *
 * @author Harry Thoma
 * @author Chris Lytle
 * @author Daylin Kuboyama
 * @author Riley Snook
 */

public class RummikubComputerPlayer2 extends RummikubComputerPlayer1 {
    private int pointsPlayed;
    private LinkedList<RummikubState> attempts;

    /**
     * Constructor for objects of class CounterComputerPlayer1
     *
     * @param name the player's name
     */
    public RummikubComputerPlayer2(String name) {
        super(name);
    }

    @Override
    protected int findMove(){
        //first run the algorithm to play sets out of hand
        pointsPlayed= super.findMove();

        //if that found a play, great
        if(!playActions.isEmpty()){
            return pointsPlayed;
        }

        //if not, run the smart algorithm
        pointsPlayed= 0;

        attempts= new LinkedList<RummikubState>();
        LinkedList<GameAction> actions= new LinkedList<GameAction>();

        //play each tile and try to fix the board
        TileGroup hand= state.getPlayerHand(playerNum);
        for(int i=0;i<hand.groupSize();i++){
            RummikubState state= new RummikubState(this.state,playerNum);
            state.canPlayTile(playerNum,i);

            actions= fixTable(new RummikubState(state,playerNum));

            if(actions != null){
                actions.addFirst(new RummikubPlayTileAction(this,i));
                playActions= actions;
                return pointsPlayed;
            }
        }

        return 0;
    }

    /**
     * fixes the table
     * @param state the state to fix
     * @return the actions required to fix this table
     *          null if the table is unfixable
     */
    private LinkedList<GameAction> fixTable(RummikubState state){

        if(weveBeenHereBefore(state)){
            return null;
        }
        attempts.add(new RummikubState(state,playerNum));

        LinkedList<GameAction> actions= new LinkedList<GameAction>();

        if(state.isValidTable()) return actions;

        ArrayList<TileGroup> table= state.getTableTileGroups();

        //look for invalid groups
        for(int i=0;i<table.size();i++){
            TileGroup group = table.get(i);
            if(!TileSet.isValidSet(group)){
                actions= fixByPlaying(new RummikubState(state,playerNum),i);
                //we fixed the table
                if(actions != null){
                    return actions;
                }

                actions= fixBySplitting(new RummikubState(state,playerNum),i);
                //we fixed the table
                if(actions != null){
                    return actions;
                }
            }
        }

        //if we get here, the table is unfixable
        return null;
    }

    /**
     * attempts to fix the table by playing a tile from the hand
     * @param state the state to fix
     * @param groupIndex the index of the bad group we're working on
     * @return the actions required to fix the table
     *          null if the table is unfixable
     */
    private LinkedList<GameAction> fixByPlaying(RummikubState state, int groupIndex){

        //make an arraylist of tile groups with just the hand
        ArrayList<TileGroup> hand= new ArrayList<TileGroup>(1);
        hand.add(state.getPlayerHand(playerNum));

        //get the group we are working on
        TileGroup group= state.getTableTileGroups().get(groupIndex);

        int[] help= {0,-1};
        //let's see if we can fix it with a tile in the hand
        //we will break this loop when necessary
        while(true){
            //look for a helpful tile in the hand
            //starting after the last time we did this
            help= helpfulTile(hand,group,help[0],help[1]+1);

            //there is no helpful tile in the hand
            if(help == null) break;

            //the index that the tile were will play will be at
            int newIndex= state.getTableTileGroups().size();

            //get the score of the tile
            Tile tileToPlay= hand.get(0).getTile(help[1]);
            int tileScore= 0;
            if(!(tileToPlay instanceof JokerTile)){
                tileScore= tileToPlay.getValue();
            }

            RummikubState stateCopy= new RummikubState(state,playerNum);
            stateCopy.canPlayTile(playerNum,help[1]);
            stateCopy.canConnect(playerNum,groupIndex,newIndex);

            //now go fix the table
            LinkedList<GameAction> actions=
                    fixTable(new RummikubState(stateCopy,playerNum));

            //we fixed the table!!
            if(actions != null){
                pointsPlayed+= tileScore;
                //add the actions to play this tile,
                //in reverse order because we are pushing them on
                actions.addFirst(new RummikubConnectAction(this,groupIndex,newIndex));
                actions.addFirst(new RummikubPlayTileAction(this,help[1]));

                return actions;
            }
        }

        //if we get here, we can;t fix the table by playing
        return null;
    }

    /**
     * attempts to fix the table by splitting a different group
     * @param state the state to fix
     * @param groupIndex the index of the bad group we're working on
     * @return the actions required to fix the table
     *          null if the table is unfixable
     */
    private LinkedList<GameAction> fixBySplitting(RummikubState state, int groupIndex){

        ArrayList<TileGroup> table= state.getTableTileGroups();
        TileGroup group= table.get(groupIndex);

        int[] help= {0,-1};
        //let's see if we can fix it with a tile on the table
        //we will break this loop when necessary
        while(true){
            //look for a helpful tile on the table
            //starting after the last time we did this
            help= helpfulTile(table,group,help[0],help[1]+1);

            if(help == null) break;

            //if we havn't melded, the group must be from our hand
            if(!state.hasMelded(playerNum)){
                if(!state.isFromHand(table.get(help[0]))){
                    continue;
                }
            }
            //we also don't want to mess with joker groups
            if(table.get(help[0]).containsJoker()){
                continue;
            }

            int newIndex= table.size();

            RummikubState stateCopy= new RummikubState(state,playerNum);

            stateCopy.canSimpleSplit(playerNum,help[0],help[1]);
            stateCopy.canConnect(playerNum,groupIndex,newIndex);

            //now go fix the table
            LinkedList<GameAction> actions=
                    fixTable(new RummikubState(stateCopy,playerNum));

            //we fixed the table!!
            if(actions != null){
                //add the actions we must take,
                //in reverse order because we are pushing to the front
                actions.addFirst(new RummikubConnectAction(this,groupIndex,newIndex));
                actions.addFirst(new RummikubComputerSplitAction(this,help[0],help[1]));

                return actions;
            }
        }

        //if we get here, we can;t fix the table by playing
        return null;
    }

    /**
     * finds the index of a tile that might help the invalid group,
     * starts looking in the tileStart'th tile of the groupStart'th group
     *
     * @param groups the groups the might contain the helpful tile
     * @param badGroup the group that needs help
     * @param groupStart the group index to start the look
     * @param tileStart the tile index to start the look
     * @return a two big array,
     *          {group index of helpful tile, index within group}
     *          null if no helpful tile exists
     */
    private int[] helpfulTile(ArrayList<TileGroup> groups, TileGroup badGroup,
                                int groupStart, int tileStart){

        //starting at the given start point, go through each group
        for(int i=groupStart;i<groups.size();i++){
            TileGroup group= groups.get(i);

            //starting at the given start point, go throug heach tile
            for(int j=tileStart;j<group.groupSize();j++){
                Tile tile= group.getTile(j);

                //could this tile be helpful?
                boolean tileCouldHelp= false;
                //if it's just one tile, we want to know if the
                //current tile helps it get closer to a set
                if(badGroup.groupSize() == 1){
                    tileCouldHelp= hopeForSet(tile,badGroup.getTile(0));
                }
                //if it's two or more, we want to see if we
                //could make a set with the current tile
                else{
                    tileCouldHelp= badGroup.canAddForSet(tile);
                }

                if(tileCouldHelp){
                    return new int[]{i,j};
                }
            }
        }

        //if we get this far, there is no helpful tile
        return null;
    }

    /**
     *
     * @param t1
     * @param t2
     * @return whether t1 and t2 are missing just one tile
     *          in order to be a set
     */
    private boolean hopeForSet(Tile t1, Tile t2){
        //if either are jokers, the is hope
        if(t1 instanceof JokerTile || t2 instanceof JokerTile){
            return true;
        }

        int valDiff= Math.abs(t1.getValue()-t2.getValue());
        boolean sameColor= t1.getColor() == t2.getColor();

        //if they are more than two apart, there is no hope
        if(valDiff > 2){
            return false;
        }

        //if the same val, but different color
        //there is hope to be a book
        if(!sameColor && valDiff == 0){
            return true;
        }

        //by this point, there is no hope of being a book
        //so equal values are bad
        if(valDiff == 0){
            return false;
        }

        //by this point, the values are within 2 of each other
        //and there is hope for a run if and only if the colors match
        return sameColor;
    }

    private LinkedList<GameAction>
    mergeQueue(LinkedList<GameAction> first, LinkedList<GameAction> next){
        LinkedList<GameAction> queue= new LinkedList<GameAction>(first);
        for(int i=0;i<next.size();i++){
            queue.add(next.get(i));
        }

        return queue;
    }

    /**
     *
     * @param state
     * @return whether the specified state matches something in the attempts list
     */
    private boolean weveBeenHereBefore(RummikubState state){
        for(RummikubState attempt : attempts){
            if(tableMatches(state, attempt)) return true;
        }

        return false;
    }

    private boolean tableMatches(RummikubState state1, RummikubState state2){
        ArrayList<TileGroup> table1= state1.getTableTileGroups();
        ArrayList<TileGroup> table2= state2.getTableTileGroups();

        if(table1.size() != table2.size()) return false;

        for(int i=0;i<table1.size();i++){
            TileGroup group1= table1.get(i);
            TileGroup group2= table2.get(i);

            if(group1.groupSize() != group2.groupSize()){
                return false;
            }

            for(int j=0;j<group1.groupSize();j++){
                Tile tile1= group1.getTile(j);
                Tile tile2= group2.getTile(j);

                if(tile1.getValue() != tile2.getValue()){
                    return false;
                }
                if(tile1.getColor() != tile2.getColor()){
                    return false;
                }
            }
        }

        return true;
    }
}
