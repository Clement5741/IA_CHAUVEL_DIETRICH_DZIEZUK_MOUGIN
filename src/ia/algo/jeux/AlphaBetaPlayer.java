package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.util.ArrayList;
import java.util.Collections;

public class AlphaBetaPlayer extends Player {

    private final int maxDepth;


    /**
     * Represente un joueur
     *
     * @param g            l'instance du jeux
     * @param player_one   si joueur 1
     * @param maxDepth la profondeur maximale à explorer
     */
    public AlphaBetaPlayer(Game g, boolean player_one, int maxDepth) {
        super(g, player_one);
        name = "AlphaBeta";
        this.maxDepth = maxDepth;
    }

    @Override
    public Action getMove(GameState state) {
        if (this.player == PLAYER1) {
            return maxValue(state, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getAction();
        } else {
            return minValue(state, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getAction();
        }
    }

    private ActionValuePair maxValue(GameState state, int depth, double alpha, double beta) {
        incStateCounter();

        if (state.isFinalState() || depth == maxDepth) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMax = Double.NEGATIVE_INFINITY;
        Action cMax = null;
        ArrayList<Action> actions = game.getActions(state);
        Collections.shuffle(actions);

        ActionValuePair value = null;
        for (Action c : actions) {
            GameState nextState = (GameState) game.doAction(state, c);
            value = minValue(nextState, depth + 1, alpha, beta);
            if (value.getValue() >= vMax) {
                vMax = value.getValue();
                cMax = c;
                if (vMax > alpha) {
                    alpha = vMax;
                }
            }
            if (vMax >= beta) {
                return new ActionValuePair(cMax, vMax);
            }
        }
        if (cMax == null) {
            vMax = value.getValue();
            cMax = actions.get(actions.size()-1);
        }

        return new ActionValuePair(cMax, vMax);
    }

    private ActionValuePair minValue(GameState state, int depth, double alpha, double beta) {
        incStateCounter();

        if (state.isFinalState() || depth == maxDepth) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMin = Double.POSITIVE_INFINITY;
        Action cMin = null;

        ArrayList<Action> actions = game.getActions(state);
        Collections.shuffle(actions);

        ActionValuePair value = null;
        for (Action c : actions) {
            GameState nextState = (GameState) game.doAction(state, c);
            value = maxValue(nextState, depth + 1, alpha, beta);
            if (value.getValue() <= vMin) {
                vMin = value.getValue();
                cMin = c;
                if (vMin < beta) {
                    beta = vMin;
                }
            }
            if (vMin <= alpha) {
                return new ActionValuePair(cMin, vMin);
            }
        }
        if (cMin == null) {
            vMin = value.getValue();
            cMin = actions.get(actions.size()-1);
        }

        return new ActionValuePair(cMin, vMin);
    }
}
