package modeling.lab4.arc;

import modeling.lab4.Requirement;

public interface ArcConnector {

    Arc chooseNextArc(Requirement requirement);
    Arc getLastChosenArc();
}
