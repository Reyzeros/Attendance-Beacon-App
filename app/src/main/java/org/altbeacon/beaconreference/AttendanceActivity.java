package org.altbeacon.beaconreference;

public class AttendanceActivity {
    String activityId;
    String activityRelation;
    String activityDate;

    public AttendanceActivity(){

    }

    public AttendanceActivity(String activityId, String activityRelation, String activityDate) {
        this.activityId = activityId;
        this.activityRelation = activityRelation;
        this.activityDate = activityDate;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityRelation() {
        return activityRelation;
    }

    public void setActivityRelation(String activityRelation) {
        this.activityRelation = activityRelation;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
}
