package modeling.lab4;

import modeling.lab4.element.state.ElementState;

public class Main {

    public static void main(String[] args) {
    }

    public static void printLogs(MassServeNet net) {
        net.getTimeList().forEach((t) -> {
            ElementState nextElement = net.getTimeNextElementMap().get(t);
            System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ It's time for event in " + nextElement.getId() + ", time = " + t);
            net.getTimeStateMap().get(t).printState();
            System.out.println();
        });
    }

}
