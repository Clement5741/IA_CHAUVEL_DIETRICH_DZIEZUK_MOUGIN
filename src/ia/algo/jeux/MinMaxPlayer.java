package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class MinMaxPlayer extends Player {

    private final int maxDepth;

    /**
     * Represente un joueur
     *
     * @param g            l'instance du jeux
     * @param player_one   si joueur 1
     * @param maxDepth la profondeur maximale à explorer
     */
    public MinMaxPlayer(Game g, boolean player_one, int maxDepth) {
        super(g, player_one);
        name = "MinMax";
        this.maxDepth = maxDepth;
    }

    @Override
    public Action getMove(GameState state) {
        if (this.player == PLAYER1) {
            return maxValue(state, 0).getAction();
        } else {
            return minValue(state, 0).getAction();
        }
    }

    private ActionValuePair maxValue(GameState state, int depth) {
        this.incStateCounter();

        if (game.endOfGame(state) || depth >= maxDepth) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMax = Double.NEGATIVE_INFINITY;
        Action cMax = null;

        for (Action action : game.getActions(state)) {
            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair value = minValue(nextState, depth + 1);
            if (value.getValue() >= vMax) {
                vMax = value.getValue();
                cMax = action;
            }
        }

        return new ActionValuePair(cMax, vMax);
    }

    private ActionValuePair minValue(GameState state, int depth) {
        this.incStateCounter();
        if (game.endOfGame(state) || depth >= maxDepth) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMin = Double.POSITIVE_INFINITY;
        Action cMin = null;

        for (Action action : game.getActions(state)) {
            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair value = maxValue(nextState, depth + 1);
            if (value.getValue() <= vMin) {
                vMin = value.getValue();
                cMin = action;
            }
        }

        return new ActionValuePair(cMin, vMin);
    }
}
