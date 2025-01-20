package mlp.mnist;

public abstract class AlgoClassification {
    protected Donnees donnees;

    public AlgoClassification(Donnees donnees) {
        this.donnees = donnees;
    }

    public abstract int predire(Imagette imagette);
}
