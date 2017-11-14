package modeling.lab4.massservesystem;

import lombok.Getter;
import lombok.Setter;
import modeling.lab4.Requirement;
import modeling.lab4.block.UnblockAction;
import modeling.lab4.element.state.ChanelObjectState;
import modeling.lab4.numbergeneration.NumberGenerator;
import modeling.lab4.util.GeneralAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Chanel {

    private ChanelState chanelState;

    private double freeTime;
    private double busyTime;
    private double blockedTime;

    private int processCount;

    private double timeCurrent;
    private double timeNextProcessEnd;

    private final List<GeneralAction> afterStateChangeActions = new ArrayList<>();

    private final String id;

    private Requirement tempRequirement;

    private final NumberGenerator delayGenerator;
    private MassServeSystem parentSystem;

    public Chanel(String id, NumberGenerator delayGenerator) {
        this.id = id;
        this.delayGenerator = delayGenerator;
        timeNextProcessEnd = Double.MAX_VALUE;
        chanelState = ChanelState.FREE;
    }

    public void doInAction(Requirement requirement) {
        if (!isFree()) {
            throw new IllegalStateException("Chanel is " + chanelState);
        }
        this.chanelState = ChanelState.BUSY;
        afterStateChangeActions.forEach(GeneralAction::execute);
        this.tempRequirement = requirement;
        timeNextProcessEnd = getTimeCurrent() + delayGenerator.generateNumber();
    }

    public Requirement sendRequirement() {
        Requirement result = tempRequirement;
        chanelState = ChanelState.FREE;
        tempRequirement = null;
        processCount++;
        timeNextProcessEnd = Double.MAX_VALUE;
        afterStateChangeActions.forEach(GeneralAction::execute);
        return result;
    }

    public UnblockAction blockChanel() {
        if (!ChanelState.BLOCKED.equals(chanelState)) {
            chanelState = ChanelState.BLOCKED;
            timeNextProcessEnd = Double.MAX_VALUE;
            afterStateChangeActions.forEach(GeneralAction::execute);
        }
        return this::trySendRequirement;
    }

    private void trySendRequirement() {
        checkParentSystem();
        parentSystem.trySendRequirement(this);
    }

    private void checkParentSystem() {
        if (parentSystem == null) {
            throw new IllegalStateException("Chanel is not paced into system");
        }
    }

    protected void doOutAction() {
        trySendRequirement();
    }

    public boolean isFree() {
        return ChanelState.FREE.equals(chanelState);
    }

    public void updateStatistics(double delay) {
        if (ChanelState.FREE.equals(chanelState)) {
            freeTime += delay;
        } else if (ChanelState.BLOCKED.equals(chanelState)) {
            blockedTime += delay;
        } else {
            busyTime += delay;
        }
    }

    public ChanelObjectState getState() {
        return ChanelObjectState.builder()
                .state(chanelState)
                .id(getId())
                .blockedTime(blockedTime)
                .busyTime(busyTime)
                .freeTime(freeTime)
                .processCount(processCount)
                .timeCurrent(timeCurrent)
                .timeNextProcessEnd(timeNextProcessEnd)
                .tempRequirement(tempRequirement != null ? tempRequirement.getRequirementType(): "null")
                .build();
    }

    public void addAfterStateChangeAction(GeneralAction generalAction) {
        afterStateChangeActions.add(generalAction);
    }

}
