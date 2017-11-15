package modeling.lab4.element.state;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class QueueState extends ObjectState {

    private final String id;
    private final int queueSize;
    private final int maxQueueSize;

    private final int failCount;

    private final List<String> requirements;

    @Override
    public void printState() {
        System.out.print("--------------- QUEUE-" + id + ": " + queueSize + "/" + maxQueueSize);
        System.out.println("  fail:" + failCount);
        requirements.forEach(s -> System.out.print(" -> " + s));
        System.out.println();
    }
}
