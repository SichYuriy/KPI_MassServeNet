package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import modeling.lab4.massservesystem.Chanel;

import java.util.Map;


@AllArgsConstructor
@Builder
@Getter
public class MassServeSystemState extends ElementState {

    private final String id;
    private final QueueState queueState;
    private final Map<String, ChanelObjectState> chanelMap;

    @Override
    public void printState() {
        System.out.println("------------------------------------------------- SYSTEM-" + id );
        queueState.printState();
        chanelMap.values().forEach(ChanelObjectState::printState);
        System.out.println("-----------------------------------------------------------END SYSTEM-" + id);

    }
}
