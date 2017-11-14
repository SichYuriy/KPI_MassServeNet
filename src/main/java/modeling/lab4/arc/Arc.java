package modeling.lab4.arc;

import lombok.Data;
import modeling.lab4.element.Element;
import modeling.lab4.Requirement;
import modeling.lab4.block.Block;

@Data
public class Arc {

    private final String id;

    private boolean blocked;

    private Block block;

    private final Element toElement;

    public void push(Requirement requirement) {
        if (blocked) {
            throw new IllegalStateException("Arc is blocked");
        }
        toElement.doInAction(requirement);
    }


}
