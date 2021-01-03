package org.altbeacon.beaconapp;

public class AttendanceActivity {
    String activityId;
    String activityDate;
    Boolean activityIsChecking;

    public AttendanceActivity(){

    }

    public AttendanceActivity(String activityId, String activityDate) {
        this.activityId = activityId;
        this.activityDate = activityDate;
        this.activityIsChecking=false;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }


    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }

    public Boolean getActivityIsChecking() {
        return activityIsChecking;
    }

    public void setActivityIsChecking(Boolean activityIsChecking) {
        this.activityIsChecking = activityIsChecking;
    }
}
