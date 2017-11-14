package modeling.lab4.element;

import lombok.Getter;
import modeling.lab4.Requirement;
import modeling.lab4.element.state.DisposeElementState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DisposeElement extends Element {

    private final Map<String, Integer> requirementTypeCount = new HashMap<>();
    private final List<Requirement> requirements = new ArrayList<>();

    public DisposeElement(String id) {
        super(id);
    }

    @Override
    protected void inAction(Requirement requirement) {
        int prev = requirementTypeCount.getOrDefault(requirement.getRequirementType(), 0);
        requirement.setDisposeTime(getTimeCurrent());
        requirements.add(requirement);
        requirementTypeCount.put(requirement.getRequirementType(), prev + 1);
    }

    @Override
    public void outAction() {
        throw new UnsupportedOperationException("DisposeElement is the end point");
    }

    @Override
    public double getTimeNext() {
        return Double.MAX_VALUE;
    }

    @Override
    public DisposeElementState getState() {
        return DisposeElementState.builder()
                .id(getId())
                .requirementCount(getInActionCount())
                .requirementTypeCount(new HashMap<>(requirementTypeCount))
                .build();
    }

}
