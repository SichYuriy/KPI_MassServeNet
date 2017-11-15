package modeling.lab4.numbergeneration;

public interface NumberGenerator {

    double generateNumber();

    default String getDescription() {
        return "";
    }
}
