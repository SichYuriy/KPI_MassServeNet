package modeling.lab4.element;

import lombok.Data;
import modeling.lab4.Requirement;
import modeling.lab4.arc.ArcConnector;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.util.GeneralAction;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class  Element {

    private final String id;

    private double timeCurrent;

    private int inActionCount;
    private int outActionCount;

    private final List<GeneralAction> beforeInActions = new ArrayList<>();
    private final List<GeneralAction> afterInActions = new ArrayList<>();

    private final List<GeneralAction> beforeOutActions = new ArrayList<>();
    private final List<GeneralAction> afterOutActions = new ArrayList<>();

    private ArcConnector arcConnector;

    public final void doInAction(Requirement requirement) {
        inActionCount++;
        beforeInActions.forEach(GeneralAction::execute);
        inAction(requirement);
        afterInActions.forEach(GeneralAction::execute);
    }

    public final void doOutAction() {
        outActionCount++;
        beforeOutActions.forEach(GeneralAction::execute);
        outAction();
        afterOutActions.forEach(GeneralAction::execute);
    }

    protected abstract void outAction();
    protected abstract void inAction(Requirement requirement);

    public void updateStatistics(double timeStep) {}

    public void addBeforeInAction(GeneralAction action) {
        beforeInActions.add(action);
    }

    public void addBeforeOutAction(GeneralAction action) {
        beforeOutActions.add(action);
    }

    public void addAfterInAction(GeneralAction action) {
        afterInActions.add(action);
    }

    public void addAfterOutAction(GeneralAction action) {
        afterOutActions.add(action);
    }

    public abstract double getTimeNext();

    public abstract ElementState getState();

}
