package modeling.lab4.numbergeneration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnifNumberGenerator implements NumberGenerator {

    private final double minVal;
    private final double maxVal;

    @Override
    public double generateNumber() {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = minVal + a * (maxVal - minVal);

        return a;
    }

    @Override
    public String getDescription() {
        return "Unif: min=" + minVal + " max=" + maxVal;
    }
}
