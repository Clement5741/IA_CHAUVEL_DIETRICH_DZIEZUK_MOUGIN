package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.util.ArrayList;

public class MinMaxPlayer extends Player {

    private static final int DEFAULT_DEPTH = Integer.MAX_VALUE;
    private final int depth;

    /**
     * Represente un joueur
     *
     * @param g            l'instance du jeux
     * @param player_one   si joueur 1
     * @param valueOfParam la profondeur maximale à explorer
     */
    public MinMaxPlayer(Game g, boolean player_one, int valueOfParam) {
        super(g, player_one);
        if (valueOfParam == -1) {
            depth = DEFAULT_DEPTH;
        } else {
            depth = valueOfParam;
        }
    }

    @Override
    public Action getMove(GameState state) {
        int player = state.getPlayerToMove();
        Action action;
        if (player == 1) {
            action = maxValue(state, depth).getAction();
        } else {
            action = minValue(state, depth).getAction();
        }
        return action;
    }

    private ActionValuePair maxValue(GameState state, int depth) {
        this.incStateCounter(); // Incrémenter le compteur

        if (state.isFinalState() || depth == 0) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double maxVal = Double.NEGATIVE_INFINITY;
        Action bestAction = null;
        ArrayList<Action> actions = game.getActions(state);

        for (Action action : actions) {
            GameState nextState = (GameState) game.doAction(state, action);
            double value = minValue(nextState, depth - 1).getValue();
            if (value > maxVal) {
                maxVal = value;
                bestAction = action;
            }
        }

        return new ActionValuePair(bestAction, maxVal);
    }

    private ActionValuePair minValue(GameState state, int depth) {
        this.incStateCounter(); // Incrémenter le compteur

        if (state.isFinalState() || depth == 0) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double minVal = Double.POSITIVE_INFINITY;
        Action bestAction = null;
        ArrayList<Action> actions = game.getActions(state);

        for (Action action : actions) {
            GameState nextState = (GameState) game.doAction(state, action);
            double value = maxValue(nextState, depth - 1).getValue();
            if (value < minVal) {
                minVal = value;
                bestAction = action;
            }
        }

        return new ActionValuePair(bestAction, minVal);
    }
}
