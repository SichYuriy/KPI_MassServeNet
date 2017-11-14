package modeling.lab4.queue;

import modeling.lab4.Requirement;
import modeling.lab4.element.state.QueueState;
import modeling.lab4.util.GeneralAction;

public interface RequirementQueue {

    String getId();

    int getMaxSize();
    int getSize();

    void pushRequirement(Requirement requirement);
    Requirement popRequirement();

    void addAfterPopAction(GeneralAction generalAction);
    void addAfterPushAction(GeneralAction generalAction);

    QueueState getState();

    void setTimeCurrent(double time);

    void updateStatistics(double timeStep);

    double calcAvgQueuSize();
}
