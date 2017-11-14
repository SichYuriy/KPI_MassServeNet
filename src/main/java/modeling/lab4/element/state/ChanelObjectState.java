package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import modeling.lab4.massservesystem.ChanelState;

@AllArgsConstructor
@Builder
@Getter
public class ChanelObjectState extends ElementState {

    private final ChanelState state;

    private final String id;

    private final double freeTime;
    private final double busyTime;
    private final double blockedTime;

    private final int processCount;

    private final double timeCurrent;
    private final double timeNextProcessEnd;

    private final String tempRequirement;

    @Override
    public void printState() {
        System.out.println("--------------- CHANEL-" + id);
        System.out.println(state + " | " + timeCurrent + " -> " + timeNextProcessEnd);
    }
}
