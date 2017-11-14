package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class CreateElementState extends ElementState {

    private final String id;
    private final Map<String, Integer> requirementTypeCount;
    private final int requirementCount;

    private final double timeCurrent;
    private final double timeNext;

    @Override
    public void printState() {
        System.out.println("--------------- CREATOR-" + id + ": " + requirementCount);
        System.out.println(timeCurrent + " -> " + timeNext);
    }
}
