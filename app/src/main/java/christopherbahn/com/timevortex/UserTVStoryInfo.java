package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 12/10/16.
 */

public class UserTVStoryInfo implements Parcelable {
    // This should sync up with a corresponding TVStory object. UserTVStoryInfo.getStoryID() == TVStory.getStoryID()

    public Integer storyID;
    public boolean iveSeenIt;
//    public Date whenISawIt;
    public boolean iOwnIt;
    public boolean iWantToSeeIt;
    private String userReview; // A space for the app user to write an opinion on the story
    private Integer userAtoF; // app user's rating of the episode. Using 0-13 allows AtoF grades
    private Integer numberRanking; // app user's ranking of the episode. Number between 1 and 288 (or more as new episodes are added)


    public UserTVStoryInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserTVStoryInfo(Integer storyID, boolean iveSeenIt, boolean iOwnIt, boolean iWantToSeeIt, String userReview, Integer userAtoF, Integer numberRanking) {
        this.storyID = storyID;
        this.iveSeenIt = iveSeenIt;
//        this.whenISawIt = whenISawIt;
        this.iOwnIt = iOwnIt;
        this.iWantToSeeIt = iWantToSeeIt;
        this.userReview = userReview;
        this.userAtoF = userAtoF;
        this.numberRanking = numberRanking;
    }

    protected UserTVStoryInfo(Parcel in) {
        iveSeenIt = in.readByte() != 0;
        iOwnIt = in.readByte() != 0;
        iWantToSeeIt = in.readByte() != 0;
        userReview = in.readString();
    }

    public static final Creator<UserTVStoryInfo> CREATOR = new Creator<UserTVStoryInfo>() {
        @Override
        public UserTVStoryInfo createFromParcel(Parcel in) {
            return new UserTVStoryInfo(in);
        }

        @Override
        public UserTVStoryInfo[] newArray(int size) {
            return new UserTVStoryInfo[size];
        }
    };

    public Integer getStoryID() {
        return storyID;
    }

    public void setStoryID(Integer storyID) {
        this.storyID = storyID;
    }

    public boolean haveISeenIt() {
        return iveSeenIt;
    }

    public void setIveSeenIt(boolean iveSeenIt) {
        this.iveSeenIt = iveSeenIt;
    }

//    public Date getWhenISawIt() {
//        return whenISawIt;
//    }
//
//    public void setWhenISawIt(Date whenISawIt) {
//        this.whenISawIt = whenISawIt;
//    }

    public boolean doiOwnIt() {
        return iOwnIt;
    }

    public void setiOwnIt(boolean iOwnIt) {
        this.iOwnIt = iOwnIt;
    }

    public boolean doiWantToSeeIt() {
        return iWantToSeeIt;
    }

    public void setiWantToSeeIt(boolean iWantToSeeIt) {
        this.iWantToSeeIt = iWantToSeeIt;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public Integer getUserAtoF() {
        return userAtoF;
    }

    public void setUserAtoF(Integer userAtoF) {
        this.userAtoF = userAtoF;
    }

    public Integer getNumberRanking() {
        return numberRanking;
    }

    public void setNumberRanking(Integer numberRanking) {
        this.numberRanking = numberRanking;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByte((byte) (iveSeenIt ? 1 : 0));
        parcel.writeByte((byte) (iOwnIt ? 1 : 0));
        parcel.writeByte((byte) (iWantToSeeIt ? 1 : 0));
        parcel.writeString(userReview);
    }
}
