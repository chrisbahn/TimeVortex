package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christopherbahn on 12/15/15.
 */
public class SearchTerm implements Parcelable {

    private int storyID;
    private String title;
    private int doctor;
    private String era;
    private String season;
    private int seasonStoryNumber;
    private int yearProduced;
    private String otherCast;
    private String synopsis;
    private String crew;
    private String seenIt; // NOT boolean so I can get three states: true/false/both
    private boolean wantToSeeIt;
    private String userReview; // A space for the app user to write an opinion on the story
    private float userStarRatingNumber; // app user's rating of the episode


    public SearchTerm() {
        super();
    }

    private SearchTerm(Parcel in) {
        super();

        this.storyID = Integer.parseInt(in.readString());
        this.title = in.readString();
        this.doctor = Integer.parseInt(in.readString());
        this.era = in.readString();
        this.season = in.readString();
        this.seasonStoryNumber = in.readInt();
        this.yearProduced = Integer.parseInt(in.readString());
        this.otherCast = in.readString();
        this.synopsis = in.readString();
        this.crew = in.readString();
        this.seenIt = (in.readString());
        this.wantToSeeIt = Boolean.parseBoolean(in.readString());
        this.userReview = in.readString();
        this.userStarRatingNumber = Float.parseFloat(in.readString());

    }

    public SearchTerm(int storyID, String title, int doctor, String era, String season, int seasonStoryNumber, int episodes, int episodeLength, int yearProduced) {
        this.storyID = storyID;
        this.title = title;
        this.doctor = doctor;
        this.era = era;
        this.season = season;
        this.seasonStoryNumber = seasonStoryNumber;
        this.yearProduced = yearProduced;
        this.otherCast = null;
        this.synopsis = null;
        this.crew = null;
        this.seenIt = seenIt;
        this.wantToSeeIt = wantToSeeIt;
        this.userReview = userReview;
        this.userStarRatingNumber = userStarRatingNumber;
    }

    public int getStoryID() {
        return storyID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getSeasonStoryNumber() {
        return seasonStoryNumber;
    }

    public void setSeasonStoryNumber(int seasonStoryNumber) {
        this.seasonStoryNumber = seasonStoryNumber;
    }

    public int getYearProduced() {
        return yearProduced;
    }

    public void setYearProduced(int yearProduced) {
        this.yearProduced = yearProduced;
    }

    public String getOtherCast() {
        return otherCast;
    }

    public void setOtherCast(String otherCast) {
        this.otherCast = otherCast;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getSeenIt() {
        return seenIt;
    }

    public void setSeenIt(String seenIt) {
        this.seenIt = seenIt;
    }

    public boolean isWantToSeeIt() {
        return wantToSeeIt;
    }

    public void setWantToSeeIt(boolean wantToSeeIt) {
        this.wantToSeeIt = wantToSeeIt;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public float getUserStarRatingNumber() {
        return userStarRatingNumber;
    }

    public void setUserStarRatingNumber(float userStarRatingNumber) {
        this.userStarRatingNumber = userStarRatingNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // todo fix this - what is this for, exactly?
//		parcel.writeString(getId().toString());
//        new ParcelUuid(getId()).writeToParcel(parcel, flags);
//        parcel.writeString(getTitle());
//        parcel.writeLong(getDateCreated().getTime());
//        parcel.writeString(String.valueOf(mHasPhoto));
//        parcel.writeString(mTextField);
//        parcel.writeString(mHashtags);
    }

    public static final Creator<SearchTerm> CREATOR = new Creator<SearchTerm>() {
        public SearchTerm createFromParcel(Parcel in) {
            return new SearchTerm(in);
        }

        public SearchTerm[] newArray(int size) {
            return new SearchTerm[size];
        }
    };

}
