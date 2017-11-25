package in.sabpaisa.droid.sabpaisa.Model;

/**
 * Created by rajdeeps on 11/23/17.
 */

public class StateGetterSetter {

    int stateId;
    String stateCode;
    String  stateName;

    public StateGetterSetter() {
    }

    public StateGetterSetter(int stateId, String stateCode, String stateName) {
        this.stateId = stateId;
        this.stateCode = stateCode;
        this.stateName = stateName;
    }


    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
