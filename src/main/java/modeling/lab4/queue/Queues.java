package modeling.lab4.queue;

public class Queues {

    public static RequirementQueueImpl newFifoQueue(String id, int maxSize) {
        return new RequirementQueueImpl(id, new FifoStrategy(), maxSize);
    }
}
