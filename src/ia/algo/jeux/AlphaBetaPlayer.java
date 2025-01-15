package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.util.ArrayList;

public class AlphaBetaPlayer extends Player {

    private static final int POSITIVE_INFINITE = Integer.MAX_VALUE;
    private static final int NEGATIVE_INFINITE = Integer.MIN_VALUE;

    /**
     * Represente un joueur
     *
     * @param g            l'instance du jeux
     * @param player_one   si joueur 1
     * @param valueOfParam la profondeur maximale à explorer
     */
    public AlphaBetaPlayer(Game g, boolean player_one, int valueOfParam) {
        super(g, player_one);
    }

    @Override
    public Action getMove(GameState state) {
        int player = state.getPlayerToMove();
        if (player == 1) {
            return maxValue(state, NEGATIVE_INFINITE, POSITIVE_INFINITE).getAction();
        } else {
            return minValue(state, NEGATIVE_INFINITE, POSITIVE_INFINITE).getAction();
        }
    }

    private ActionValuePair maxValue(GameState state, double alpha, double beta) {
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double maxVal = Double.NEGATIVE_INFINITY;
        Action bestAction = null;
        ArrayList<Action> actions = game.getActions(state);

        for (Action action : actions) {
            GameState nextState = (GameState) game.doAction(state, action);
            double value = minValue(nextState, alpha, beta).getValue();
            if (value >= maxVal) {
                maxVal = value;
                bestAction = action;
                if (maxVal > alpha) {
                    alpha = maxVal;
                }
            }
            if (maxVal >= beta) {
                return new ActionValuePair(bestAction, maxVal);
            }
        }

        return new ActionValuePair(bestAction, maxVal);
    }

    private ActionValuePair minValue(GameState state, double alpha, double beta) {
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double minVal = Double.POSITIVE_INFINITY;
        Action bestAction = null;
        ArrayList<Action> actions = game.getActions(state);

        for (Action action : actions) {
            GameState nextState = (GameState) game.doAction(state, action);
            double value = maxValue(nextState, alpha, beta).getValue();
            if (value <= minVal) {
                minVal = value;
                bestAction = action;
                if (minVal < beta) {
                    beta = minVal;
                }
            }
            if (minVal <= alpha) {
                return new ActionValuePair(bestAction, minVal);
            }
        }

        return new ActionValuePair(bestAction, minVal);
    }
}
