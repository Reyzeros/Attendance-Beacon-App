package org.altbeacon.beaconapp;

public class IsPresent {
    String isPresentId;
    String userId;
    String userName;

    public IsPresent(){

    }

    public IsPresent(String isPresentId, String userId, String userName) {
        this.isPresentId = isPresentId;
        this.userId = userId;
        this.userName=userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
