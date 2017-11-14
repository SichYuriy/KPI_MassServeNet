package modeling.lab4.arc.way;

import modeling.lab4.Requirement;
import modeling.lab4.arc.Arc;
import modeling.lab4.massservesystem.MassServeSystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShortestQueueWayChoosingStrategy implements WayChoosingStrategy {

    private final List<MassServeSystem> systems = new ArrayList<>();

    public ShortestQueueWayChoosingStrategy(List<MassServeSystem> requirementQueues) {
        systems.addAll(requirementQueues);
    }

    @Override
    public Arc chooseNextArc(List<Arc> arcs, Requirement requirement) {
        MassServeSystem result = systems.stream().filter(s -> s.getNextChanel().isFree()).findFirst().orElseGet(
                () -> systems.stream().min(Comparator.comparingInt(s -> s.getRequirementQueue().getSize())).orElse(null)
        );
        return arcs.get(systems.indexOf(result));
    }
}
