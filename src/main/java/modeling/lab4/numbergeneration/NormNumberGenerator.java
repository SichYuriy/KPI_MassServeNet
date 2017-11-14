package modeling.lab4.numbergeneration;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class NormNumberGenerator implements NumberGenerator {

    private final double meanVal;
    private final double deviationVal;

    private final Random r = new Random();

    @Override
    public double generateNumber() {
        double a;
        a = meanVal + deviationVal * r.nextGaussian();
        return a;
    }

    @Override
    public String getDescription() {
        return "";
    }
}
