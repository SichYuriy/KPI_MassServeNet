package modeling.lab4.massservesystem;

import lombok.Getter;
import lombok.Setter;
import modeling.lab4.Requirement;
import modeling.lab4.block.UnblockAction;
import modeling.lab4.element.Element;
import modeling.lab4.element.state.ChanelObjectState;
import modeling.lab4.numbergeneration.NumberGenerator;
import modeling.lab4.util.GeneralAction;

@Getter
@Setter
public class Chanel extends Element {

    private ChanelState chanelState;

    private double freeTime;
    private double busyTime;
    private double blockedTime;

    private int processCount;

    private double timeCurrent;
    private double timeNextProcessEnd;

    private Requirement tempRequirement;

    private final NumberGenerator delayGenerator;
    private MassServeSystem parentSystem;

    public Chanel(String id, NumberGenerator delayGenerator) {
        super(id);
        this.delayGenerator = delayGenerator;
        timeNextProcessEnd = Double.MAX_VALUE;
        chanelState = ChanelState.FREE;
    }

    @Override
    public void inAction(Requirement requirement) {
        if (!isFree()) {
            throw new IllegalStateException("Chanel is " + chanelState);
        }
        this.chanelState = ChanelState.BUSY;
        this.tempRequirement = requirement;
        timeNextProcessEnd = getTimeCurrent() + delayGenerator.generateNumber();
    }

    public Requirement sendRequirement() {
        Requirement result = tempRequirement;
        chanelState = ChanelState.FREE;
        tempRequirement = null;
        processCount++;
        timeNextProcessEnd = Double.MAX_VALUE;
        getAfterOutActions().forEach(GeneralAction::execute);
        return result;
    }

    public UnblockAction blockChanel() {
        chanelState = ChanelState.BLOCKED;
        timeNextProcessEnd = Double.MAX_VALUE;
        getAfterOutActions().forEach(GeneralAction::execute);
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

    @Override
    protected void outAction() {
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

    @Override
    public double getTimeNext() {
        return timeNextProcessEnd;
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

}
