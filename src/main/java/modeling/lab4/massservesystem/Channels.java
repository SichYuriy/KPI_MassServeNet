package modeling.lab4.massservesystem;

import modeling.lab4.numbergeneration.NumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class Channels {

    public static List<Chanel> createChannels(NumberGenerator delayGenerator, String id, int count) {
        List<Chanel> result = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            result.add(new Chanel(id + i, delayGenerator));
        }
        return result;
    }

}
