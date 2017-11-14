package modeling.lab4.numbergeneration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConstNumberGenerator implements NumberGenerator {

    private final double val;

    @Override
    public double generateNumber() {
        return val;
    }

    @Override
    public String getDescription() {
        return "Const: val=" + val;
    }
}
