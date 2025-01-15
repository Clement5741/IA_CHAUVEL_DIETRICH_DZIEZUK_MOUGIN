package ia.algo.recherche;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import ia.framework.common.State;
import ia.framework.common.Action;

import ia.framework.recherche.TreeSearch;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.SearchNode;

public class DFS extends TreeSearch {

    public DFS(SearchProblem prob, State initial_state) {
        super(prob, initial_state);
        // Initialisation de la frontière comme une Queue LIFO
        this.frontier = Collections.asLifoQueue(new ArrayDeque<>());
    }

    public boolean solve() {
        // 1. Créer un noeud correspondant à l'état initial
        SearchNode root_node = SearchNode.makeRootSearchNode(initial_state);

        // 2. Initialiser la frontière avec ce noeud
        this.frontier.clear();
        this.frontier.offer(root_node);

        // 3. Initialiser l'ensemble des états visités à vide
        this.explored.clear();

        // 4. Tant que la frontière n'est pas vide
        while (!this.frontier.isEmpty()) {
            // 5. Retirer un noeud de la frontière (LIFO)
            SearchNode cur_node = this.frontier.poll();
            State cur_state = cur_node.getState();

            // 6. Si le noeud contient un état but
            if (problem.isGoalState(cur_state)) { // Si but
                this.end_node = cur_node;
                return true;
            }

            // 7. Ajouter son état à l'ensemble des états visités
            this.explored.add(cur_state);

            // 8. Étendre les enfants du noeud
            ArrayList<Action> actions = problem.getActions(cur_state);

            // 9. Pour chaque noeud enfant
            for (Action a : actions) {
                // Générer un noeud enfant
                SearchNode child_node = SearchNode.makeChildSearchNode(problem, cur_node, a);
                State child_state = child_node.getState();

                // 10. S'il n'est pas dans la frontière et son état n'a pas été visité
                if (!frontier.contains(child_node) && !explored.contains(child_state)) {
                    // 11. L'insérer dans la frontière
                    this.frontier.offer(child_node);
                }
            }
        }
        return false;
    }
}
