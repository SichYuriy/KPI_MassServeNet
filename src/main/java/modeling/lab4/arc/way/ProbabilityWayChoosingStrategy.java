package modeling.lab4.arc.way;

import modeling.lab4.Requirement;
import modeling.lab4.arc.Arc;

import java.util.ArrayList;
import java.util.List;

public class ProbabilityWayChoosingStrategy implements WayChoosingStrategy {

    private final List<Double> probabilityList = new ArrayList<>();

    public ProbabilityWayChoosingStrategy(List<Double> probabilityList) {
        this.probabilityList.addAll(probabilityList);
    }

    @Override
    public Arc chooseNextArc(List<Arc> arcs, Requirement requirement) {
        if (arcs.size() != probabilityList.size() + 1) {
            throw new IllegalArgumentException("Supposed arc count " + (probabilityList.size() + 1) + " but found " + arcs.size());
        }
        double next = Math.random();
        int resultIndex = 0;
        for (Double prob: probabilityList) {
            if (prob < next) {
                return arcs.get(resultIndex);
            }
            next -= prob;
            resultIndex++;
        }
        return arcs.get(resultIndex);
    }
}
