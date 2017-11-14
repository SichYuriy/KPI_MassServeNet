package modeling.lab4.arc;

import lombok.Data;
import modeling.lab4.Requirement;

@Data
public class SingleWayArcConnector implements ArcConnector {

    private final Arc arc;

    @Override
    public Arc chooseNextArc(Requirement requirement) {
        return arc;
    }

    @Override
    public Arc getLastChosenArc() {
        return arc;
    }
}
