package org.altbeacon.beaconapp;

public class User {
   private String userId;
   private String userName;
   private String userEmail;
   private String  groupId;

   public User(){

   }

    public User(String userId,String userName, String userEmail,String groupId) {
        this.userId = userId;
        this.userName=userName;
        this.userEmail = userEmail;
        this.groupId=groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
