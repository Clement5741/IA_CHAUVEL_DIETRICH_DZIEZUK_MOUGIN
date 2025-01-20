package mlp;

public class SigmoidFunction implements TransferFunction {

    @Override
    public double evaluate(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    @Override
    public double evaluateDer(double sigmoidValue) {
        // La dérivée de σ(x) en fonction de σ(x)
        return sigmoidValue - Math.pow(sigmoidValue, 2);
    }
}
