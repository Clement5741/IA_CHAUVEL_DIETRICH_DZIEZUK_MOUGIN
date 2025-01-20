package mlp;

public class TanhFunction implements TransferFunction {

    @Override
    public double evaluate(double value) {
        return Math.tanh(value);
    }

    @Override
    public double evaluateDer(double tanhValue) {
        // La dérivée de tanh(x) en fonction de tanh(x)
        return 1 - Math.pow(tanhValue, 2);
    }
}
