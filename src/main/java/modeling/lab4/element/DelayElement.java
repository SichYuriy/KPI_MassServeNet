package modeling.lab4.element;

import lombok.Getter;
import modeling.lab4.Requirement;
import modeling.lab4.element.state.DelayElementState;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.numbergeneration.NumberGenerator;
import modeling.lab4.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Getter
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
        return requirements.isEmpty() ? Double.MAX_VALUE : requirements.peek().getValue();
    }

    @Override
    public ElementState getState() {
        return DelayElementState.builder()
                .id(getId())
                .requirements(requirements.stream().map(p -> new Pair<>(p.getKey().getRequirementType(), p.getValue())).collect(Collectors.toList()))
                .build();
    }
}
