package modeling.lab4.arc.way;

import modeling.lab4.Requirement;
import modeling.lab4.arc.Arc;
import modeling.lab4.queue.Queues;
import modeling.lab4.queue.RequirementQueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShortestQueueWayChoosingStrategy implements WayChoosingStrategy {
    
    private final List<RequirementQueue> queues = new ArrayList<>();

    public ShortestQueueWayChoosingStrategy(List<RequirementQueue> requirementQueues) {
        queues.addAll(requirementQueues);
    }

    @Override
    public Arc chooseNextArc(List<Arc> arcs, Requirement requirement) {
        RequirementQueue result = queues.stream().min(Comparator.comparingInt(RequirementQueue::getSize)).orElse(null);
        return arcs.get(queues.indexOf(result));
    }
}
