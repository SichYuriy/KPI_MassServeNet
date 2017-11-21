package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import modeling.lab4.Requirement;
import modeling.lab4.util.Pair;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class DelayElementState extends ElementState {

    private final List<Pair<String, Double>> requirements;
    private final String id;

    @Override
    public void printState() {
        System.out.println("--------------- DELAY-" + id + ": " + requirements.size());
        requirements.forEach(p -> System.out.println(p.getKey() + " -> " + p.getValue()));
    }
}
