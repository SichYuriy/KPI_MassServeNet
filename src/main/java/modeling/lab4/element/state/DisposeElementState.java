package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class DisposeElementState extends ElementState {

    private final String id;
    private final Map<String, Integer> requirementTypeCount;
    private final int requirementCount;

    @Override
    public void printState() {
        System.out.println("--------------- DISPOSE-" + id + ": " + requirementCount);
    }
}
