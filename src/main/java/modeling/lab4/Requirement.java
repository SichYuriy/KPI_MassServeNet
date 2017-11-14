package modeling.lab4;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Requirement {

    private final String requirementType;

    private double creationTime;
    private double disposeTime;

    public Requirement(String requirementType) {
        this.requirementType = requirementType;
    }

    public Requirement() {
        this("defaultReqType");
    }

    public String getRequirementType() {
        return requirementType;
    }

    public double getLifeTime(double currentTime) {
        if (disposeTime < creationTime) {
            return currentTime - creationTime;
        }
        return disposeTime - creationTime;
    }
}
