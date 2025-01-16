package ia.problemes;
import ia.framework.common.Action;
import ia.framework.common.State;

public class DilemmeFermierState extends State {
    // Positions des éléments : "L" pour rive gauche, "R" pour rive droite
    private String farmer;
    private String wolf;
    private String goat;
    private String cabbage;

    /**
     * Constructeur pour un état donné.
     *
     * @param farmer Position du fermier
     * @param wolf Position du loup
     * @param goat Position de la chèvre
     * @param cabbage Position du chou
     */
    public DilemmeFermierState(String farmer, String wolf, String goat, String cabbage) {
        this.farmer = farmer;
        this.wolf = wolf;
        this.goat = goat;
        this.cabbage = cabbage;
    }

    @Override
    public State cloneState() {
        return new DilemmeFermierState(farmer, wolf, goat, cabbage);
    }

    @Override
    public boolean equalsState(State o) {
        DilemmeFermierState other = (DilemmeFermierState) o;
        return this.farmer.equals(other.farmer) &&
                this.wolf.equals(other.wolf) &&
                this.goat.equals(other.goat) &&
                this.cabbage.equals(other.cabbage);
    }

    @Override
    public int hashState() {
        return 31 * (farmer.hashCode() + wolf.hashCode() + goat.hashCode() + cabbage.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Farmer: %s, Wolf: %s, Goat: %s, Cabbage: %s", farmer, wolf, goat, cabbage);
    }

    /**
     * Vérifie si une action est légale dans cet état.
     *
     * @param a L'action à vérifier
     * @return true si l'action est légale, false sinon
     */
    public boolean isLegal(Action a) {
        DilemmeFermierState newState = (DilemmeFermierState) this.clone();
        newState.applyAction(a);

        // Vérifie les contraintes : pas de chèvre et loup ensemble sans fermier
        if (newState.farmer.equals(newState.wolf) && newState.farmer.equals(newState.goat)) {
            return true; // Le fermier est présent, aucun problème
        }
        if (newState.goat.equals(newState.cabbage) && newState.goat.equals(newState.farmer)) {
            return true; // Le fermier est présent, aucun problème
        }

        // État interdit : chèvre et loup ensemble sans le fermier
        if (newState.wolf.equals(newState.goat) && !newState.farmer.equals(newState.goat)) {
            return false;
        }

        // État interdit : chèvre et chou ensemble sans le fermier
        if (newState.goat.equals(newState.cabbage) && !newState.farmer.equals(newState.goat)) {
            return false;
        }

        return true;
    }

    /**
     * Applique une action à l'état courant.
     *
     * @param a L'action à appliquer
     */
    public void applyAction(Action a) {
        if (a.equals(DilemmeFermier.CROSS_ALONE)) {
            farmer = togglePosition(farmer);
        } else if (a.equals(DilemmeFermier.CROSS_WOLF) && farmer.equals(wolf)) {
            farmer = togglePosition(farmer);
            wolf = togglePosition(wolf);
        } else if (a.equals(DilemmeFermier.CROSS_GOAT) && farmer.equals(goat)) {
            farmer = togglePosition(farmer);
            goat = togglePosition(goat);
        } else if (a.equals(DilemmeFermier.CROSS_CABBAGE) && farmer.equals(cabbage)) {
            farmer = togglePosition(farmer);
            cabbage = togglePosition(cabbage);
        }
    }

    /**
     * Change la position d'un élément ("L" <-> "R").
     *
     * @param pos Position actuelle ("L" ou "R")
     * @return Nouvelle position
     */
    private String togglePosition(String pos) {
        return pos.equals("L") ? "R" : "L";
    }
}
