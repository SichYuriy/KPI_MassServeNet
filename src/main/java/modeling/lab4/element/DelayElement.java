package modeling.lab4.element;

import javafx.util.Pair;
import modeling.lab4.Requirement;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.numbergeneration.NumberGenerator;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DelayElement extends Element {

    private final PriorityQueue<Pair<Requirement, Double>> requirements = new PriorityQueue<>(Comparator.comparingDouble(Pair<Requirement, Double>::getValue));

    private final NumberGenerator delayGenerator;

    public DelayElement(String id, NumberGenerator delayGenerator) {
        super(id);
        this.delayGenerator =  delayGenerator;
    }

    @Override
    protected void outAction() {
        Requirement pushRequirement = requirements.poll().getKey();
        getArcConnector().chooseNextArc(pushRequirement).push(pushRequirement);
    }

    @Override
    protected void inAction(Requirement requirement) {
        requirements.add(new Pair<>(requirement, getTimeCurrent() + delayGenerator.generateNumber()));
    }

    @Override
    public double getTimeNext() {
        return requirements.peek().getValue();
    }

    @Override
    public ElementState getState() {
        return null;
    }
}
