package modeling.lab4;

import modeling.lab4.arc.Arcs;
import modeling.lab4.arc.way.ProbabilityWayChoosingStrategy;
import modeling.lab4.element.CreateElement;
import modeling.lab4.element.DisposeElement;
import modeling.lab4.massservesystem.Chanel;
import modeling.lab4.massservesystem.Channels;
import modeling.lab4.massservesystem.MassServeSystem;
import modeling.lab4.numbergeneration.ExpNumberGenerator;
import modeling.lab4.queue.Queues;
import modeling.lab4.queue.RequirementQueue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Task2 {

    public static void main(String[] args) {
        task2();
    }

    private static void task2() {

        CreateElement createElement = new CreateElement<>("creator", new ExpNumberGenerator(2), Requirement::new);

        RequirementQueue q1 = Queues.newFifoQueue("q1", Integer.MAX_VALUE);
        Chanel k1 = new Chanel("k1", new ExpNumberGenerator(0.6));
        MassServeSystem smo1 = new MassServeSystem("smo1", q1, Collections.singletonList(k1));

        RequirementQueue q2 = Queues.newFifoQueue("q2", Integer.MAX_VALUE);
        Chanel k2 = new Chanel("k2", new ExpNumberGenerator(0.3));
        MassServeSystem smo2 = new MassServeSystem("smo2", q2, Collections.singletonList(k2));

        RequirementQueue q3 = Queues.newFifoQueue("q3", Integer.MAX_VALUE);
        Chanel k3 = new Chanel("k3", new ExpNumberGenerator(0.4));
        MassServeSystem smo3 = new MassServeSystem("smo3", q3, Collections.singletonList(k3));

        RequirementQueue q4 = Queues.newFifoQueue("q4", Integer.MAX_VALUE);
        List<Chanel> smo4Channels = Channels.createChannels(new ExpNumberGenerator(0.1), "k4", 2);
        MassServeSystem smo4 = new MassServeSystem("smo4", q4, smo4Channels);

        DisposeElement disposeElement = new DisposeElement("disposer");

        Arcs.bindWithSingleArc(createElement, smo1);
        Arcs.bindOneToManyWithBranching(smo1, Arrays.asList(smo2, smo3, smo4, disposeElement),
                new ProbabilityWayChoosingStrategy(Arrays.asList(0.15, 0.13, 0.3)));
        Arcs.bindWithSingleArc(smo2, smo1);
        Arcs.bindWithSingleArc(smo3, smo1);
        Arcs.bindWithSingleArc(smo4, smo1);

        MassServeNet massServeNet = new MassServeNet(Arrays.asList(createElement, smo1, smo2, smo3, smo4, disposeElement));

        massServeNet.setCollectState(false);
        massServeNet.simulate(100000);

        System.out.println("q1 avgSize: " + q1.calcAvgQueueSize());
        System.out.println("q2 avgSize: " + q2.calcAvgQueueSize());
        System.out.println("q3 avgSize: " + q3.calcAvgQueueSize());
        System.out.println("q4 avgSize: " + q4.calcAvgQueueSize());

        System.out.println("k1 load " + k1.calcLoad());
        System.out.println("k2 load " + k2.calcLoad());
        System.out.println("k3 load " + k3.calcLoad());
        System.out.println("k41 load " + smo4Channels.get(0).calcLoad());
        System.out.println("k42 load " + smo4Channels.get(1).calcLoad());
    }
}
