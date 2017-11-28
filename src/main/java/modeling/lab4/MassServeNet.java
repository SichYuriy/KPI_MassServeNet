package modeling.lab4;

import lombok.Getter;
import lombok.Setter;
import modeling.lab4.element.Element;
import modeling.lab4.element.state.ElementState;
import modeling.lab4.element.state.MassServeNetState;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class MassServeNet {

    private double timeCurrent;

    boolean collectState = true;

    private final List<Element> elements = new ArrayList<>();

    private final List<Double> timeList = new ArrayList<>();
    private final Map<Double, MassServeNetState> timeStateMap = new HashMap<>();
    private final Map<Double, ElementState> timeNextElementMap = new HashMap<>();

    public MassServeNet(List<Element> elements) {
        this.elements.addAll(elements);
    }

    public void simulate(double time) {
        while (timeCurrent < time) {
            Element nextElement = elements.stream().min(Comparator.comparingDouble(Element::getTimeNext)).orElse(null);
            double timeNext = nextElement.getTimeNext();

            elements.forEach(e -> e.updateStatistics(timeNext - timeCurrent));

            timeCurrent = timeNext;

            elements.forEach(e -> e.setTimeCurrent(timeCurrent));

            nextElement.doOutAction();

            elements.stream()
                    .filter(e -> e.getTimeNext() == timeCurrent)
                    .forEach(Element::doOutAction);

            if (collectState) {
                noteElementsState(timeNext, nextElement);
            }
        }
    }

    private void noteElementsState(double timeNext, Element nextElement) {
        timeList.add(timeNext);
        timeStateMap.put(timeNext, getState());
        timeNextElementMap.put(timeNext, nextElement.getState());
    }

    public MassServeNetState getState() {
        return new MassServeNetState(elements.stream().collect(Collectors.toMap(Element::getId, Element::getState)));
    }

}
