package christopherbahn.com.timevortex;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 11/17/16.
 */

@IgnoreExtraProperties
public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String userDisplayName;
    private String userPhotoUrl;
    public ArrayList<User> usersFollowedBy;
    public ArrayList<User> usersFollowing;
//    public Date lastLogin;
//    public Date dateSignedUp;
    public int age;
    public String gender;
    public String location; // i.e. what city or state are you from
    public int favoriteDoctor;
    public int favoriteCompanion;
    public int favoriteEpisode;
    public ArrayList<UserTVStoryInfo> userTVStoryInfo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
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

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public ArrayList<User> getUsersFollowedBy() {
        return usersFollowedBy;
    }

    public void setUsersFollowedBy(ArrayList<User> usersFollowedBy) {
        this.usersFollowedBy = usersFollowedBy;
    }

    public ArrayList<User> getUsersFollowing() {
        return usersFollowing;
    }

    public void setUsersFollowing(ArrayList<User> usersFollowing) {
        this.usersFollowing = usersFollowing;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getFavoriteDoctor() {
        return favoriteDoctor;
    }

    public void setFavoriteDoctor(int favoriteDoctor) {
        this.favoriteDoctor = favoriteDoctor;
    }

    public int getFavoriteCompanion() {
        return favoriteCompanion;
    }

    public void setFavoriteCompanion(int favoriteCompanion) {
        this.favoriteCompanion = favoriteCompanion;
    }

    public int getFavoriteEpisode() {
        return favoriteEpisode;
    }

    public void setFavoriteEpisode(int favoriteEpisode) {
        this.favoriteEpisode = favoriteEpisode;
    }

    public ArrayList<UserTVStoryInfo> getUserTVStoryInfo() {
        return userTVStoryInfo;
    }

    public void setUserTVStoryInfo(ArrayList<UserTVStoryInfo> userTVStoryInfo) {
        this.userTVStoryInfo = userTVStoryInfo;
    }
}
