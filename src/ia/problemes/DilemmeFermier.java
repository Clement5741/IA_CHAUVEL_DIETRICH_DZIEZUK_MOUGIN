package ia.problemes;

import ia.framework.recherche.SearchProblem;
import java.util.ArrayList;

import ia.framework.common.Action;
import ia.framework.common.State;


public class DilemmeFermier extends SearchProblem {
    // Les actions possibles pour le fermier
    public static final Action TRAVERSE_SEUL = new Action("Traverse seul");
public static final Action TRAVERSE_LOUP = new Action("Traverse avec le loup");
public static final Action TRAVERSE_GOAT = new Action("Traverse avec la chèvre");
public static final Action TRAVERSE_CHOU = new Action("Traverse avec le chou");

    /**
     * L'état but (tout le monde sur la rive droite)
     */
    public static final DilemmeFermierState GOAL_STATE =
            new DilemmeFermierState("R", "R", "R", "R");

    /**
     * Crée une instance du problème du fermier
     */
    public DilemmeFermier() {
        // Description
        this.desc = "Le problème du fermier avec le loup, la chèvre et le chou";

        // La liste des actions possibles
        ACTIONS = new Action[] {TRAVERSE_SEUL, TRAVERSE_LOUP, TRAVERSE_GOAT, TRAVERSE_CHOU};
    }

    /**
     * {@inheritDoc}
     * <p>Chaque traversée coûte 1.</p>
     */
    public double getActionCost(State s, Action a) {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     * <p>Retourne uniquement les actions valides.</p>
     */
    public ArrayList<Action> getActions(State s) {
        ArrayList<Action> actions = new ArrayList<>();
        DilemmeFermierState currentState = (DilemmeFermierState) s;

        // Vérifie si chaque action est valide
        for (Action a : ACTIONS) {
            if (currentState.isLegal(a)) {
                actions.add(a);
            }
        }
        return actions;
    }

    /**
     * {@inheritDoc}
     * <p>Applique une action et retourne le nouvel état.</p>
     */
    public State doAction(State s, Action a) {
        DilemmeFermierState newState = (DilemmeFermierState) s.clone();
        newState.applyAction(a);
        return newState;
    }

    /**
     * {@inheritDoc}
     * <p>Vérifie si l'état courant est l'état but.</p>
     */
    public boolean isGoalState(State s) {
        return s.equals(GOAL_STATE);
    }
}
