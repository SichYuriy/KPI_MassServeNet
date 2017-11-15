package modeling.lab4;

import modeling.lab4.arc.Arcs;
import modeling.lab4.arc.way.ShortestQueueWayChoosingStrategy;
import modeling.lab4.element.CreateElement;
import modeling.lab4.element.DisposeElement;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.massservesystem.Chanel;
import modeling.lab4.massservesystem.MassServeSystem;
import modeling.lab4.numbergeneration.ExpNumberGenerator;
import modeling.lab4.numbergeneration.NormNumberGenerator;
import modeling.lab4.numbergeneration.NumberGenerator;
import modeling.lab4.queue.Queues;
import modeling.lab4.queue.RequirementQueueImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        task2();
    }

    public static void test() {
        NumberGenerator createDelayGen = new ExpNumberGenerator(2);
        NumberGenerator processDelayGen = new ExpNumberGenerator(2);

        CreateElement createElement = new CreateElement<>("CREATOR", createDelayGen, Requirement::new);

        RequirementQueueImpl kasa1Queue = Queues.newFifoQueue("Q_kasa1", 5);
        Chanel kasa1 = new Chanel("kasa1", processDelayGen);
        MassServeSystem massServeSystem = new MassServeSystem("Kasa1", kasa1Queue, Collections.singletonList(kasa1));

        DisposeElement disposeElement = new DisposeElement("DISPOSE");

        Arcs.bindWithSingleArc(createElement, massServeSystem);
        Arcs.bindWithSingleArc(massServeSystem, disposeElement);

        MassServeNet net = new MassServeNet(Arrays.asList(createElement, massServeSystem, disposeElement));

        net.simulate(450);

        printLogs(net);

        System.out.println("Created: " + createElement.getOutActionCount());
        System.out.println("Processed:" + disposeElement.getInActionCount());
        System.out.println("Failure:" + kasa1Queue.getState().getFailCount());

        System.out.println("QueueSize: " + kasa1Queue.getSize());
        System.out.println("State: " + kasa1.getState());
    }

    private static void task2() {
        CreateElement createElement = new CreateElement<>("CREATOR", new ExpNumberGenerator(0.5), Requirement::new);

        RequirementQueueImpl queue1 = Queues.newFifoQueue("queue1", 3);
        Chanel window1 = new Chanel("window1", new NormNumberGenerator(1, 0.3));
        MassServeSystem window1System = new MassServeSystem("wind1S", queue1, Collections.singletonList(window1));

        RequirementQueueImpl queue2 = Queues.newFifoQueue("queue2", 3);
        Chanel window2 = new Chanel("window2", new NormNumberGenerator(1, 0.3));
        MassServeSystem window2System = new MassServeSystem("wind2S", queue2, Collections.singletonList(window2));

        AtomicInteger changeLineCount = new AtomicInteger(0);

        queue1.addAfterPopAction(() -> {
            if (queue2.getSize() - queue1.getSize() >= 2) {
                queue1.pushRequirement(queue2.popRequirement());
                changeLineCount.incrementAndGet();
            }
        });

        queue2.addAfterPopAction(() -> {
            if (queue1.getSize() - queue2.getSize() >= 2) {
                queue2.pushRequirement(queue1.popRequirement());
                changeLineCount.incrementAndGet();
            }
        });

        DisposeElement system1Dispose = new DisposeElement("dispose1");
        DisposeElement system2Dispose = new DisposeElement("dispose2");

        Arcs.bindOneToManyWithBranching(createElement, Arrays.asList(window1System, window2System),
                new ShortestQueueWayChoosingStrategy(Arrays.asList(window1System, window2System)));
        Arcs.bindWithSingleArc(window1System, system1Dispose);
        Arcs.bindWithSingleArc(window2System, system2Dispose);

        // Start obstacles
        window1.inputRequirement(new Requirement());
        window2.inputRequirement(new Requirement());

        queue1.pushRequirement(new Requirement());
        queue1.pushRequirement(new Requirement());

        queue2.pushRequirement(new Requirement());
        queue2.pushRequirement(new Requirement());

        createElement.setTimeNext(0.1);

        //Simulation
        MassServeNet massServeNet = new MassServeNet(Arrays.asList(createElement, window1System, window2System, system1Dispose, system2Dispose));
        massServeNet.simulate(1000);

        //Results
        printLogs(massServeNet);

        double totalTime = massServeNet.getTimeCurrent();
        List<Requirement> allReq = new ArrayList<>();
        allReq.addAll(system1Dispose.getRequirements());
        allReq.addAll(system2Dispose.getRequirements());
        allReq.addAll(queue1.getFailCollector().getRequirements());
        allReq.addAll(queue2.getFailCollector().getRequirements());
        double avgClients = allReq.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / massServeNet.getTimeCurrent();
        double avgLifeTime = allReq.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / allReq.size();
        System.out.println("RESULTS: ");
        System.out.println("window1 load:" + window1.getBusyTime() / (window1.getBusyTime() + window1.getBlockedTime() + window1.getFreeTime()));
        System.out.println("window2 load:" + window2.getBusyTime() / (window2.getBusyTime() + window2.getBlockedTime() + window2.getFreeTime()));

        System.out.println("avg clients count: " + avgClients);

        System.out.println("window1 time per client: " + (totalTime / window1.getProcessCount()));
        System.out.println("window2 time per client: " + (totalTime / window2.getProcessCount()));

        System.out.println("avg client serve time: " + avgLifeTime);

        System.out.println("queue1 size: " + queue1.calcAvgQueuSize());
        System.out.println("queue2 size: " + queue2.calcAvgQueuSize());

        System.out.println("failure probability: " + (1.0 * (queue1.getFailCollector().getInActionCount() + queue2.getFailCollector().getInActionCount()) / allReq.size()));
        System.out.println("line change count: " + changeLineCount);

    }

    private static void printLogs(MassServeNet net) {
        net.getTimeList().forEach((t) -> {
            ElementState nextElement = net.getTimeNextElementMap().get(t);
            System.out.println("It's time for event in " + nextElement.getId() + ", time = " + t);
            net.getTimeStateMap().get(t).printState();
            System.out.println();
        });
    }
}
