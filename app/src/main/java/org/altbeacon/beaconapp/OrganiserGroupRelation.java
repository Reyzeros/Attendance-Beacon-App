package org.altbeacon.beaconapp;

public class OrganiserGroupRelation {
    String relationId;
    String organiserId;
    String groupId;


    public OrganiserGroupRelation(){

    }

    public OrganiserGroupRelation(String relationId, String organiserId, String groupId) {
        this.relationId=relationId;
        this.organiserId = organiserId;
        this.groupId = groupId;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
}
