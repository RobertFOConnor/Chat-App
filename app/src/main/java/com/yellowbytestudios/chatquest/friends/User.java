package com.yellowbytestudios.chatquest.friends;

/**
 * Created by Robert on 07-Dec-17.
 */

public class User {

    private String uid;
    private String name;
    private String profilePicture;

    public User(String uid, String name, String profilePicture) {
        this.uid = uid;
        this.name = name;
        this.profilePicture = profilePicture;
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
