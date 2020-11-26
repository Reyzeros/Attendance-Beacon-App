package org.altbeacon.beaconreference;

public class Organiser {
    String organiserId;
    String organiserName;
    String organiserBeaconId;

    public Organiser(){
    }

    public Organiser(String organiserId, String organiserName, String organiserBeaconId) {
        this.organiserId = organiserId;
        this.organiserName = organiserName;
        this.organiserBeaconId = organiserBeaconId;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getOrganiserName() {
        return organiserName;
    }

    public void setOrganiserName(String organiserName) {
        this.organiserName = organiserName;
    }

    public String getOrganiserBeaconId() {
        return organiserBeaconId;
    }

    public void setOrganiserBeaconId(String organiserBeaconId) {
        this.organiserBeaconId = organiserBeaconId;
    }
}
