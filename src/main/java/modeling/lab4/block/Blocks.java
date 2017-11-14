package modeling.lab4.block;

import modeling.lab4.arc.Arc;
import modeling.lab4.massservesystem.Chanel;

import java.util.List;

public class Blocks {

    public static Block createAllChannelsBusyBlock(Arc arc, List<Chanel> chanelList) {
        Block block = new Block(() -> chanelList.stream().noneMatch(Chanel::isFree));
        chanelList.forEach(c -> c.addAfterStateChangeAction(block::refresh));
        arc.setBlock(block);
        return block;
    }
}
