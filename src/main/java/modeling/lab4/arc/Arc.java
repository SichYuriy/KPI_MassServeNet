package modeling.lab4.arc;

import lombok.Getter;
import modeling.lab4.Requirement;
import modeling.lab4.block.Block;
import modeling.lab4.element.Element;

@Getter
public class Arc {

    private final String id;

    private Block block;

    private final Element toElement;

    public Arc(String id, Element toElement) {
        this.id = id;
        this.toElement = toElement;
    }

    public void push(Requirement requirement) {
        if (isBlocked()) {
            throw new IllegalStateException("Arc is blocked");
        }
        toElement.doInAction(requirement);
    }

    public boolean isBlocked() {
        return block != null && block.isActive();
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
