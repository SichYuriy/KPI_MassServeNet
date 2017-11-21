package modeling.lab4.arc;

import modeling.lab4.arc.way.WayChoosingStrategy;
import modeling.lab4.element.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Arcs {

    public static Arc bindWithSingleArc(Element from, Element to) {
        return bindWithSingleArc(from, to, genArcId(from, to));
    }

    public static Arc bindWithSingleArc(Element from, Element to, String id) {
        Arc arc = new Arc(id, to);
        SingleWayArcConnector singleWayArcConnector = new SingleWayArcConnector(arc);
        from.setArcConnector(singleWayArcConnector);
        return arc;
    }

    public static Map<String, Arc> bindOneToManyWithBranching(Element from, List<Element> toElements,
                                                              WayChoosingStrategy wayChoosingStrategy) {
       List<Arc> arcs = toElements.stream()
                .map(el -> new Arc(genArcId(from, el), el))
                .collect(Collectors.toList());
        MultipleWayArcConnector arcConnector =
                new MultipleWayArcConnector(wayChoosingStrategy, new ArrayList<>(arcs));
        from.setArcConnector(arcConnector);
        return arcs.stream().collect(Collectors.toMap(a -> a.getToElement().getId(), a -> a));
    }

    private static String genArcId(Element from, Element to) {
        return from.getId() + "_" + to.getId();
    }
}
