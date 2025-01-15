package ia.algo.recherche;

import java.util.LinkedList;

import ia.framework.common.State;
import ia.framework.common.Action;
import ia.framework.recherche.TreeSearch;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.SearchNode;

public class BFS extends TreeSearch {

    public BFS(SearchProblem prob, State initial_state) {
        super(prob, initial_state);
        this.frontier = new LinkedList<SearchNode>();
    }

    @Override
    public boolean solve() {
        // 1. Créer un noeud correspondant à l'état initial
        SearchNode root_node = SearchNode.makeRootSearchNode(this.initial_state);

        // 2. Initialiser la frontière avec ce noeud
        this.frontier.clear();
        this.frontier.add(root_node);

        // 3. Initialiser l'ensemble des états visités à vide
        this.explored.clear();

        // 4. Tant que la frontière n'est pas vide
        while (!this.frontier.isEmpty()) {
            // 5. Retirer un noeud de la frontière (FIFO)
            SearchNode currentNode = this.frontier.poll();
            State currentState = currentNode.getState();

            // 6. Si le noeud contient un état but
            if (this.problem.isGoalState(currentState)) {
                this.end_node = currentNode;
                return true;
            }

            // 7. Ajouter son état à l'ensemble des états visités
            this.explored.add(currentState);

            // 8. Étendre les enfants du noeud
            for (Action action : problem.getActions(currentState)) {
                // Générer un noeud enfant
                SearchNode child_node = SearchNode.makeChildSearchNode(problem, currentNode, action);
                State child_state = child_node.getState();

                // 10. S'il n'est pas dans la frontière et son état n'a pas été visité
                if (!frontier.contains(child_node) && !explored.contains(child_state)) {
                    // 11. L'insérer dans la frontière
                    frontier.add(child_node);
                }
            }
        }
        return false;
    }
}