package ia.algo.recherche;

import java.util.PriorityQueue;
import java.util.Comparator;

import ia.framework.common.State;
import ia.framework.common.Action;
import ia.framework.recherche.TreeSearch;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.HasHeuristic;

public class GFS extends TreeSearch {

    public GFS(SearchProblem prob, State initial_state) {
        super(prob, initial_state);
        // Initialisation de la frontière comme une PriorityQueue triée par l'heuristique
        this.frontier = new PriorityQueue<>(Comparator.comparingDouble(SearchNode::getHeuristic));
    }

    @Override
    public boolean solve() {
        // 1. Créer un noeud correspondant à l'état initial
        SearchNode root_node = SearchNode.makeRootSearchNode(this.initial_state);

        // 2. Initialiser la frontière avec ce noeud
        this.frontier.clear();
        this.frontier.offer(root_node);

        // 3. Initialiser l'ensemble des états visités à vide
        this.explored.clear();

        // 4. Tant que la frontière n'est pas vide
        while (!this.frontier.isEmpty()) {
            // 5. Retirer un noeud de la frontière avec la valeur heuristique la plus basse
            SearchNode currentNode = this.frontier.poll();
            State currentState = currentNode.getState();

            // 6. Si le noeud contient un état but
            if (this.problem.isGoalState(currentState)) {
                this.end_node = currentNode;
                return true;
            }

            // 7. Vérifier si l'état courant dispose d'une heuristique
            if (!(currentState instanceof HasHeuristic)) {
                System.err.println("Erreur : L'état ne dispose pas de fonction heuristique.");
                return false;
            }

            // 8. Ajouter son état à l'ensemble des états visités
            this.explored.add(currentState);

            // 9. Étendre les enfants du noeud
            for (Action action : problem.getActions(currentState)) {
                // Générer un noeud enfant
                SearchNode childNode = SearchNode.makeChildSearchNode(problem, currentNode, action);
                State childState = childNode.getState();

                // 10. Vérifier si l'état enfant dispose d'une heuristique
                if (!(childState instanceof HasHeuristic)) {
                    continue; // Ignorer les noeuds sans heuristique
                }

                // 11. S'il n'est pas dans la frontière et son état n'a pas été visité
                if (!frontier.contains(childNode) && !explored.contains(childState)) {
                    // 12. L'insérer dans la frontière
                    frontier.offer(childNode);
                }
            }
        }
        return false;
    }
}
