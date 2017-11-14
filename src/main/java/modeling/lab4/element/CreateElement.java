package modeling.lab4.element;

import lombok.Getter;
import lombok.Setter;
import modeling.lab4.Requirement;
import modeling.lab4.element.state.CreateElementState;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.numbergeneration.NumberGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@Setter
public class CreateElement<T extends Requirement> extends Element {

    private final NumberGenerator delayGenerator;
    private final Supplier<T> supplier;

    private Map<String, Integer> requirementTypeCount = new HashMap<>();

    private double timeNext;

    public CreateElement(String id, NumberGenerator delayGenerator, Supplier<T> supplier) {
        super(id);
        this.delayGenerator = delayGenerator;
        this.supplier = supplier;
        timeNext = delayGenerator.generateNumber();
    }

    @Override
    public void inAction(Requirement requirement) {
        throw new UnsupportedOperationException("Generator only creates requirements");
    }

    @Override
    protected void outAction() {
        arcConnectorCheck();
        T requirement = supplier.get();
        requirement.setCreationTime(getTimeCurrent());
        getArcConnector().chooseNextArc(requirement).push(requirement);
        timeNext = getTimeCurrent() + delayGenerator.generateNumber();

        int prev = requirementTypeCount.getOrDefault(requirement.getRequirementType(), 0);
        requirementTypeCount.put(requirement.getRequirementType(), prev + 1);
    }

    private void arcConnectorCheck() {
        if (getArcConnector() == null) {
            throw new IllegalStateException("Element is not bound with next");
        }
    }

    @Override
    public double getTimeNext() {
        return timeNext;
    }

    @Override
    public CreateElementState getState() {
        return CreateElementState.builder()
                .id(getId())
                .requirementCount(getOutActionCount())
                .requirementTypeCount(new HashMap<>(requirementTypeCount))
                .timeCurrent(getTimeCurrent())
                .timeNext(getTimeNext())
                .build();
    }


}
