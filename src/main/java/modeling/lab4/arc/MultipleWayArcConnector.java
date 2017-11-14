package modeling.lab4.arc;

import modeling.lab4.Requirement;
import modeling.lab4.arc.way.WayChoosingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MultipleWayArcConnector implements ArcConnector {

    private final WayChoosingStrategy wayChoosingStrategy;
    private final List<Arc> arcs = new ArrayList<>();

    public MultipleWayArcConnector(WayChoosingStrategy wayChoosingStrategy, List<Arc> arcs) {
        this.wayChoosingStrategy = wayChoosingStrategy;
        this.arcs.addAll(arcs);
    }

    private Arc lastChosenArc;

    @Override
    public Arc chooseNextArc(Requirement requirement) {
        lastChosenArc = wayChoosingStrategy.chooseNextArc(arcs, requirement);
        return lastChosenArc;
    }

    @Override
    public Arc getLastChosenArc() {
        return lastChosenArc;
    }
}
