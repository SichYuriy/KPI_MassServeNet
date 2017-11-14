package modeling.lab4.arc.way;

import modeling.lab4.Requirement;
import modeling.lab4.arc.Arc;

import java.util.List;

public interface WayChoosingStrategy {

    Arc chooseNextArc(List<Arc> arcs, Requirement requirement);
}
