package ia.problemes;
import ia.framework.common.Action;
import ia.framework.common.State;

public class DilemmeFermierState extends State {
    // Positions des éléments : "L" pour rive gauche, "R" pour rive droite
    private String fermier;
    private String loup;
    private String goat;
    private String chou;

    /**
     * Constructeur pour un état donné.
     *
     * @param fermier Position du fermier
     * @param loup Position du loup
     * @param goat Position de la chèvre
     * @param chou Position du chou
     */
    public DilemmeFermierState(String fermier, String loup, String goat, String chou) {
        this.fermier = fermier;
        this.loup = loup;
        this.goat = goat;
        this.chou = chou;
    }

    @Override
    public State cloneState() {
        return new DilemmeFermierState(fermier, loup, goat, chou);
    }

    @Override
    public boolean equalsState(State o) {
        DilemmeFermierState other = (DilemmeFermierState) o;
        return this.fermier.equals(other.fermier) &&
                this.loup.equals(other.loup) &&
                this.goat.equals(other.goat) &&
                this.chou.equals(other.chou);
    }

    @Override
    public int hashState() {
        return 31 * (fermier.hashCode() + loup.hashCode() + goat.hashCode() + chou.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Fermier: %s, Loup: %s, Goat: %s, Chou: %s", fermier, loup, goat, chou);
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
        if (newState.fermier.equals(newState.loup) && newState.fermier.equals(newState.goat)) {
            return true; // Le fermier est présent, aucun problème
        }
        if (newState.goat.equals(newState.chou) && newState.goat.equals(newState.fermier)) {
            return true; // Le fermier est présent, aucun problème
        }

        // État interdit : chèvre et loup ensemble sans le fermier
        if (newState.loup.equals(newState.goat) && !newState.fermier.equals(newState.goat)) {
            return false;
        }

        // État interdit : chèvre et chou ensemble sans le fermier
        if (newState.goat.equals(newState.chou) && !newState.fermier.equals(newState.goat)) {
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
        if (a.equals(DilemmeFermier.TRAVERSE_SEUL)) {
            fermier = togglePosition(fermier);
        } else if (a.equals(DilemmeFermier.TRAVERSE_LOUP) && fermier.equals(loup)) {
            fermier = togglePosition(fermier);
            loup = togglePosition(loup);
        } else if (a.equals(DilemmeFermier.TRAVERSE_GOAT) && fermier.equals(goat)) {
            fermier = togglePosition(fermier);
            goat = togglePosition(goat);
        } else if (a.equals(DilemmeFermier.TRAVERSE_CHOU) && fermier.equals(chou)) {
            fermier = togglePosition(fermier);
            chou = togglePosition(chou);
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
