package modeling.lab4.numbergeneration;

import flanagan.analysis.Stat;

public class ErlangNumberGenerator implements NumberGenerator {
    private final double rate;
    private final int shape;

    public ErlangNumberGenerator(double rate, int shape) {
        this.rate = rate;
        this.shape = shape;
    }

    @Override
    public double generateNumber( ) {
        return Stat.erlangRand(rate, shape, 1)[0];
    }

    @Override
    public String getDescription() {
        return "Erlang: rate-" + rate + " shape:" + shape;
    }
}
