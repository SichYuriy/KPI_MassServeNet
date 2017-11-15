package modeling.lab4.queue;

import modeling.lab4.Requirement;

import java.util.List;

public class FifoStrategy implements RequirementChoosingStrategy {

    @Override
    public Requirement chooseNext(List<Requirement> queue) {
        return queue.get(0);
    }
}
