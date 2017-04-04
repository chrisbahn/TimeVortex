package christopherbahn.com.timevortex;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 11/17/16.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public ArrayList<UserTVStoryInfo> userTVStoryInfo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<UserTVStoryInfo> getUserTVStoryInfo() {
        return userTVStoryInfo;
    }

    public void setUserTVStoryInfo(ArrayList<UserTVStoryInfo> userTVStoryInfo) {
        this.userTVStoryInfo = userTVStoryInfo;
    }
}
