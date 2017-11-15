package modeling.lab4;

import com.google.common.collect.ImmutableMap;
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
import modeling.lab4.queue.RequirementQueue;
import modeling.lab4.queue.RequirementQueueImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        task3();
//        task4();
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

    private static void task3() {
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
        MassServeNet massServeNet = new MassServeNet(
            Arrays.asList(createElement, window1System, window2System, system1Dispose, system2Dispose));
        massServeNet.simulate(1000);

        //Results
        printLogs(massServeNet);

        double totalTime = massServeNet.getTimeCurrent();
        List<Requirement> allReq = new ArrayList<>();
        allReq.addAll(system1Dispose.getRequirements());
        allReq.addAll(system2Dispose.getRequirements());
        allReq.addAll(queue1.getFailCollector().getRequirements());
        allReq.addAll(queue2.getFailCollector().getRequirements());
        double avgClients =
            allReq.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / massServeNet
                .getTimeCurrent();
        double avgLifeTime =
            allReq.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / allReq.size();
        System.out.println("RESULTS: ");
        System.out.println("window1 load:" + window1.getBusyTime() / (
            window1.getBusyTime() + window1.getBlockedTime() + window1.getFreeTime()));
        System.out.println("window2 load:" + window2.getBusyTime() / (
            window2.getBusyTime() + window2.getBlockedTime() + window2.getFreeTime()));

        System.out.println("avg clients count: " + avgClients);

        System.out.println("window1 time per client: " + (totalTime / window1.getProcessCount()));
        System.out.println("window2 time per client: " + (totalTime / window2.getProcessCount()));

        System.out.println("avg client serve time: " + avgLifeTime);

        System.out.println("queue1 size: " + queue1.calcAvgQueuSize());
        System.out.println("queue2 size: " + queue2.calcAvgQueuSize());

        System.out.println("failure probability: " + (
            1.0 * (queue1.getFailCollector().getInActionCount() + queue2.getFailCollector().getInActionCount()) / allReq
                .size()));
        System.out.println("line change count: " + changeLineCount);

    }

    private static void task4() {
        String type1 = "type_1";
        String type2 = "type_2";
        String type3 = "type_3";

        CreateElement createElement = new CreateElement<>("CR_1", new ExpNumberGenerator(15),
            () -> new Requirement(Math.random() < 0.5 ? type1 : (Math.random() < 0.2 ? type2 : type3)));

        Chanel doc1 = new Chanel("doc1", null);
        Chanel doc2 = new Chanel("doc2", null);

        Map<String, NumberGenerator> typeDelayMap = ImmutableMap.of(
            type1, new ExpNumberGenerator(15),
            type2, new ExpNumberGenerator(40),
            type3, new ExpNumberGenerator(30));


        NumberGenerator doc1DelayGen = () -> typeDelayMap.get(doc1.getTempRequirement().getRequirementType()).generateNumber();
        NumberGenerator doc2DelayGen = () -> typeDelayMap.get(doc2.getTempRequirement().getRequirementType()).generateNumber();

        doc1.setDelayGenerator(doc1DelayGen);
        doc2.setDelayGenerator(doc2DelayGen);

        RequirementQueue receptionQueue = new RequirementQueueImpl("reception_q",
            lr -> lr.stream().filter(r -> r.getRequirementType().equals(type1)).findFirst().orElseGet(() -> lr.get(0)),
            Integer.MAX_VALUE);

        MassServeSystem reception = new MassServeSystem("reception", receptionQueue, Arrays.asList(doc1, doc2));



        DisposeElement disposeElement = new DisposeElement("disposeEl");

        Arcs.bindWithSingleArc(createElement, reception);
        Arcs.bindWithSingleArc(reception, disposeElement);

        MassServeNet massServeNet = new MassServeNet(Arrays.asList(createElement, reception, disposeElement));

        massServeNet.simulate(500);

        printLogs(massServeNet);
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
