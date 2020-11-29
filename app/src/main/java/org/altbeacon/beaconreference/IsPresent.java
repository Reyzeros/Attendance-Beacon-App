package org.altbeacon.beaconreference;

public class IsPresent {
    String isPresentId;
    String userId;

    public IsPresent(){

    }

    public IsPresent(String isPresentId, String userId) {
        this.isPresentId = isPresentId;
        this.userId = userId;
    }

    public String getIsPresentId() {
        return isPresentId;
    }

    public void setIsPresentId(String isPresentId) {
        this.isPresentId = isPresentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
