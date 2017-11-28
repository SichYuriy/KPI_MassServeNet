package modeling.lab4;

import com.google.common.collect.ImmutableMap;
import modeling.lab4.arc.Arc;
import modeling.lab4.arc.Arcs;
import modeling.lab4.block.Blocks;
import modeling.lab4.element.CreateElement;
import modeling.lab4.element.DelayElement;
import modeling.lab4.element.DisposeElement;
import modeling.lab4.massservesystem.Chanel;
import modeling.lab4.massservesystem.Channels;
import modeling.lab4.massservesystem.MassServeSystem;
import modeling.lab4.numbergeneration.ErlangNumberGenerator;
import modeling.lab4.numbergeneration.ExpNumberGenerator;
import modeling.lab4.numbergeneration.NumberGenerator;
import modeling.lab4.numbergeneration.UnifNumberGenerator;
import modeling.lab4.queue.Queues;
import modeling.lab4.queue.RequirementQueue;
import modeling.lab4.queue.RequirementQueueImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static modeling.lab4.Main.printLogs;

public class Task4 {

    public static void main(String[] args) {
        task4();
    }

    private static void task4() {
        String type1 = "type_1";
        String type2 = "type_2";
        String type3 = "type_3";

        CreateElement createElement = new CreateElement<>("CR_1", new ExpNumberGenerator(15),
                () -> new Requirement(Math.random() < 0.5 ? type1 : (Math.random() < 0.2 ? type2 : type3)));

        // reception
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

        MassServeSystem receptionSystem = new MassServeSystem("reception", receptionQueue, Arrays.asList(doc1, doc2));

        // reception-lab way
        DelayElement receptionLabWay = new DelayElement("reception_lab", new UnifNumberGenerator(2, 5));

        //helpers
        List<Chanel> helpers = Channels.createChannels(new UnifNumberGenerator(3, 8), "helper", 3);
        RequirementQueue helpersQueue = Queues.newFifoQueue("helpersQueue", Integer.MAX_VALUE);
        MassServeSystem helpersSystem = new MassServeSystem("helpers_system", helpersQueue, helpers);

        // chamber
        DisposeElement chamber = new DisposeElement("chamber");

        //lab registry
        RequirementQueue labRegistryQueue = Queues.newFifoQueue("labRegistry_queue", Integer.MAX_VALUE);
        Chanel labRegistry = new Chanel("labRegistry", new ErlangNumberGenerator(4.5, 3));
        MassServeSystem labRegistrySystem = new MassServeSystem("labRegistry_system",
                labRegistryQueue, Collections.singletonList(labRegistry));

        //lab
        RequirementQueue labQueue = Queues.newFifoQueue("lab_queue", Integer.MAX_VALUE);
        List<Chanel> assistants = Channels.createChannels(new ErlangNumberGenerator(4, 2), "assistant", 2);
        MassServeSystem labSystem = new MassServeSystem("lab_system", labQueue, assistants);

        // dispose for type3
        DisposeElement labDispose = new DisposeElement("labDispose");

        //lab reception way
        DelayElement labReceptionWay = new DelayElement("lab_reception", new UnifNumberGenerator(2, 5));
        labReceptionWay.addBeforeOutAction(
                () -> labReceptionWay.getRequirements().peek().getKey().setRequirementType(type1));


        Arcs.bindWithSingleArc(createElement, receptionSystem);
        Arcs.bindOneToManyWithBranching(receptionSystem, Arrays.asList(receptionLabWay, helpersSystem),
                (arcs, r) -> type1.equals(r.getRequirementType()) ? arcs.get(1) : arcs.get(0));
        Arcs.bindWithSingleArc(helpersSystem, chamber);
        Arcs.bindWithSingleArc(receptionLabWay, labRegistrySystem);
        Arcs.bindWithSingleArc(labRegistrySystem, labSystem);
        Arcs.bindOneToManyWithBranching(labSystem, Arrays.asList(labDispose, labReceptionWay),
                (arcs, r) -> type3.equals(r.getRequirementType()) ? arcs.get(0) : arcs.get(1));
        Arcs.bindWithSingleArc(labReceptionWay, receptionSystem);

        MassServeNet massServeNet = new MassServeNet(Arrays.asList(createElement, receptionSystem, helpersSystem, chamber,
                receptionLabWay, labRegistrySystem, labSystem, labDispose, labReceptionWay));

        massServeNet.simulate(10000);

        printLogs(massServeNet);

        //RESULTS

        List<Requirement> req1List = chamber.getRequirements().stream().filter(r -> type1.equals(r.getOriginalType())).collect(Collectors.toList());
        List<Requirement> req2List = chamber.getRequirements().stream().filter(r -> type2.equals(r.getOriginalType())).collect(Collectors.toList());
        List<Requirement> req3List = labDispose.getRequirements();
        Double req1AvgLife = req1List.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / req1List.size();
        Double req2AvgLife = req2List.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / req2List.size();
        Double req3AvgLife = req3List.stream().mapToDouble(r -> r.getLifeTime(massServeNet.getTimeCurrent())).sum() / req3List.size();

        System.out.println("type1 serve time: " + req1AvgLife);
        System.out.println("type2 serve time: " + req2AvgLife);
        System.out.println("type3 serve time: " + req3AvgLife);

        System.out.println("lab incoming interval: " + (massServeNet.getTimeCurrent() / labSystem.getInActionCount()));
    }
}
