package modeling.lab4.numbergeneration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpNumberGenerator implements NumberGenerator {

    private final double meanVal;

    @Override
    public double generateNumber() {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = -meanVal * Math.log(a);

        return a;
    }

    @Override
    public String getDescription() {
        return "Exp: mean=" + meanVal;
    }
}
