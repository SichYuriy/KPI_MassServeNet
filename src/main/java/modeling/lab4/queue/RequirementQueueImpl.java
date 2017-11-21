package modeling.lab4.queue;

import lombok.Getter;
import lombok.Setter;
import modeling.lab4.Requirement;
import modeling.lab4.element.DisposeElement;
import modeling.lab4.element.state.QueueState;
import modeling.lab4.util.GeneralAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RequirementQueueImpl implements RequirementQueue {

    private final RequirementChoosingStrategy requirementChoosingStrategy;
    private final int maxSize;
    private final String id;

    private final DisposeElement failCollector;

    private RejectAction rejectAction;

    private double timeCurrent;

    private double meanSizeQueue;

    private final List<Requirement> requirements = new ArrayList<>();

    private final List<GeneralAction> afterPopActions = new ArrayList<>();
    private final List<GeneralAction> afterPushActions = new ArrayList<>();

    public RequirementQueueImpl(String id, RequirementChoosingStrategy requirementChoosingStrategy, int maxSize) {
        this.id = id;
        this.requirementChoosingStrategy = requirementChoosingStrategy;
        this.maxSize = maxSize;
        this.failCollector = new DisposeElement(id + "_failed");
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public int getSize() {
        return requirements.size();
    }

    @Override
    public void pushRequirement(Requirement requirement) {
        if (requirements.size() == maxSize) {
            rejectRequirement(requirement);
        } else {
            requirements.add(requirement);
            afterPushActions.forEach(GeneralAction::execute);
        }

    }

    private void rejectRequirement(Requirement requirement) {
        if (rejectAction == null) {
            failCollector.doInAction(requirement);

        } else {
            rejectAction.process(requirement);
        }

    }

    @Override
    public Requirement popRequirement() {
        Requirement requirement = requirementChoosingStrategy.chooseNext(requirements);
        requirements.remove(requirement);
        afterPopActions.forEach(GeneralAction::execute);
        return requirement;
    }

    @Override
    public void addAfterPopAction(GeneralAction generalAction) {
        afterPopActions.add(generalAction);
    }

    @Override
    public void addAfterPushAction(GeneralAction generalAction) {
        afterPushActions.add(generalAction);
    }

    @Override
    public QueueState getState() {
        return QueueState.builder()
                .id(id)
                .maxQueueSize(maxSize)
                .queueSize(getSize())
                .requirements(requirements.stream().map(Requirement::getRequirementType).collect(Collectors.toList()))
                .failCount(failCollector.getInActionCount())
                .build();
    }

    public void setRejectAction(RejectAction rejectAction) {
        this.rejectAction = rejectAction;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setTimeCurrent(double timeCurrent) {
        failCollector.setTimeCurrent(timeCurrent);
        this.timeCurrent = timeCurrent;
    }

    @Override
    public void updateStatistics(double timeStep) {
        meanSizeQueue += timeStep * getSize();
    }

    @Override
    public double calcAvgQueueSize() {
        return meanSizeQueue / timeCurrent;
    }
}
