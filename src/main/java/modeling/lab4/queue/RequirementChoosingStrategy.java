package modeling.lab4.queue;

import modeling.lab4.Requirement;

import java.util.List;

public interface RequirementChoosingStrategy {
    Requirement chooseNext(List<Requirement> queue);
}
