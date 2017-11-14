package modeling.lab4.massservesystem;

import modeling.lab4.element.Element;
import modeling.lab4.Requirement;
import modeling.lab4.arc.Arc;
import modeling.lab4.element.state.MassServeSystemState;
import modeling.lab4.queue.RequirementQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MassServeSystem extends Element {

    private final RequirementQueue requirementQueue;

    private final List<Chanel> chanelList = new ArrayList<>();

    private Chanel nextChanel;

    public MassServeSystem(String id, RequirementQueue requirementQueue, List<Chanel> chanelList) {
        super(id);
        this.requirementQueue = requirementQueue;
        this.chanelList.addAll(chanelList);
        chanelList.forEach(c -> c.setParentSystem(this));
        updateNextEvent();
    }

    public void trySendRequirement(Chanel childChanel) {
        arcConnectorCheck();
        Requirement requirementToSend = childChanel.getTempRequirement();

        Arc nextArc = getArcConnector().chooseNextArc(requirementToSend);
        if (nextArc.isBlocked()) {
            nextArc.getBlock().putBlockedElement(childChanel.getId(), childChanel.blockChanel());
            childChanel.blockChanel();

        } else {
            nextArc.push(childChanel.sendRequirement());
            if (requirementQueue.getSize() != 0) {
                childChanel.doInAction(requirementQueue.popRequirement());
            }
        }
    }

    private void arcConnectorCheck() {
        if (getArcConnector() == null) {
            throw new IllegalStateException("Element is not bound with next");
        }
    }


    @Override
    public void inAction(Requirement requirement) {
        Optional<Chanel> freeChanel = chanelList.stream().filter(Chanel::isFree).findFirst();
        if (freeChanel.isPresent()) {
            freeChanel.get().doInAction(requirement);
            updateNextEvent();
        } else {
            requirementQueue.pushRequirement(requirement);
        }
    }

    @Override
    public void outAction() {
        nextChanel.doOutAction();
        updateNextEvent();
    }

    @Override
    public double getTimeNext() {
        return chanelList.stream()
                .map(Chanel::getTimeNextProcessEnd)
                .min(Double::compare).orElse(Double.MAX_VALUE);
    }

    @Override
    public MassServeSystemState getState() {
        return MassServeSystemState.builder()
                .id(getId())
                .queueState(requirementQueue.getState())
                .chanelMap(chanelList.stream().collect(Collectors.toMap(Chanel::getId, Chanel::getState)))
                .build();
    }

    private void updateNextEvent() {
        nextChanel = chanelList.stream()
                .min(Comparator.comparingDouble(Chanel::getTimeNextProcessEnd)).orElse(null);
    }

    @Override
    public void updateStatistics(double timeStep) {
        chanelList.forEach(c -> c.updateStatistics(timeStep));
    }

    @Override
    public void setTimeCurrent(double timeCurrent) {
        super.setTimeCurrent(timeCurrent);
        chanelList.forEach(c -> c.setTimeCurrent(timeCurrent));
    }
}
