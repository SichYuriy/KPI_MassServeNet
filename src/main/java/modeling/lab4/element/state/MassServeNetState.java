package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class MassServeNetState extends ObjectState {

    private final Map<String, ElementState> elements;

    @Override
    public void printState() {
        elements.values().forEach(ElementState::printState);
    }

}
