package modeling.lab4.block;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class Block {

    private boolean active;

    private final Map<String, UnblockAction> blockedElements = new HashMap<>();

    private final BlockCondition blockCondition;

    public Block(BlockCondition blockCondition) {
        this.blockCondition = blockCondition;
        refresh();
    }

    public void refresh() {
        boolean newActiveValue = blockCondition.checkCondition();
        if (newActiveValue != active) {
            active = newActiveValue;
            if (!active) {
                fireUnblockElements();
            } else {

            }
        }
    }

    public void putBlockedElement(String id, UnblockAction listener) {
        blockedElements.put(id, listener);
    }

    public void fireUnblockElements() {
        ArrayList<String> elementsToUnblock = new ArrayList<>(blockedElements.keySet());
        elementsToUnblock.stream()
                .map(blockedElements::remove)
                .forEach(UnblockAction::processUnblock);
    }

}
