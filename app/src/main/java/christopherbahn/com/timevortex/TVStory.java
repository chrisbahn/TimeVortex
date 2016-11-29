package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.widget.RatingBar;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 12/9/15.
 */


public class TVStory implements Parcelable {
	// Adapted/refactored from Note object in Project 2

	private int storyID;
	private String title;
	private int doctor;
	private String era;
	private String season;
	private int seasonStoryNumber;
	private int episodes;
	private int episodeLength;
	private int yearProduced;
//	private ArrayList<DWCharacter> otherCast; TODO Implement ArrayList and connection to DWCharacter Class; for now, this is being dealt with as a String
    private String otherCast;
	private String synopsis;
//	private ArrayList<DWCrew> crew; TODO Implement ArrayList and connection to DWCrew Class; for now, this is being dealt with as a String
    private String crew;
	private boolean seenIt;
	private boolean wantToSeeIt;
	private String ASIN; // A proprietary ID number for Amazon products, in this case individual DVD titles
	private String userReview; // A space for the app user to write an opinion on the story
	private float userStarRatingNumber; // app user's rating of the episode
	private int bestOfBBCAmerica;
	private int bestOfDWM2009;
	private int bestOfDWM2014;
	private int bestOfAVCTVC10;
	private int bestOfIo9;
	private int bestOfLMMyles;
	private int bestOfBahn;
	private String tvstoryImage;


	public TVStory() {
		super();
	}

	private TVStory(Parcel in) {
		super();

		this.storyID = Integer.parseInt(in.readString());
		this.title = in.readString();
		this.doctor = Integer.parseInt(in.readString());
		this.era = in.readString();
		this.season = in.readString();
		this.seasonStoryNumber = in.readInt();
		this.episodes = Integer.parseInt(in.readString());
		this.episodeLength = Integer.parseInt(in.readString());
		this.yearProduced = Integer.parseInt(in.readString());
		this.otherCast = in.readString();
		this.synopsis = in.readString();
		this.crew = in.readString();
        this.seenIt = Boolean.parseBoolean(in.readString());
        this.wantToSeeIt = Boolean.parseBoolean(in.readString());
		this.ASIN = in.readString();
		this.userReview = in.readString();
		this.userStarRatingNumber = Float.parseFloat(in.readString());
		this.bestOfBBCAmerica = Integer.parseInt(in.readString());
		this.bestOfDWM2009 = Integer.parseInt(in.readString());
		this.bestOfDWM2014 = Integer.parseInt(in.readString());
		this.bestOfAVCTVC10 = Integer.parseInt(in.readString());
		this.bestOfIo9 = Integer.parseInt(in.readString());
		this.bestOfLMMyles = Integer.parseInt(in.readString());
		this.bestOfBahn = Integer.parseInt(in.readString());
		this.tvstoryImage = in.readString();

	}

    public TVStory(int storyID, String title, int doctor, String era, String season, int seasonStoryNumber, int episodes, int episodeLength, int yearProduced, String ASIN) {
        this.storyID = storyID;
        this.title = title;
        this.doctor = doctor;
        this.era = era;
        this.season = season;
        this.seasonStoryNumber = seasonStoryNumber;
        this.episodes = episodes;
        this.episodeLength = episodeLength;
        this.yearProduced = yearProduced;
        this.otherCast = null;
        this.synopsis = null;
        this.crew = null;
        this.seenIt = seenIt;
		this.wantToSeeIt = wantToSeeIt;
		this.ASIN = ASIN;
		this.userReview = userReview;
		this.userStarRatingNumber = userStarRatingNumber;
		this.bestOfBBCAmerica = bestOfBBCAmerica;
		this.bestOfDWM2009 = bestOfDWM2009;
		this.bestOfDWM2014 = bestOfDWM2014;
		this.bestOfAVCTVC10 = bestOfAVCTVC10;
		this.bestOfIo9 = bestOfIo9;
		this.bestOfLMMyles = bestOfLMMyles;
		this.bestOfBahn = bestOfBahn;
		this.tvstoryImage = tvstoryImage;
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

	public int getEpisodes() {
		return episodes;
	}

	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}

	public int getEpisodeLength() {
		return episodeLength;
	}

	public void setEpisodeLength(int episodeLength) {
		this.episodeLength = episodeLength;
	}

	public int getYearProduced() {
		return yearProduced;
	}

	public void setYearProduced(int yearProduced) {
		this.yearProduced = yearProduced;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getOtherCast() {
		return otherCast;
	}

	public void setOtherCast(String otherCast) {
		this.otherCast = otherCast;
	}

	public String getCrew() {
		return crew;
	}

	public void setCrew(String crew) {
		this.crew = crew;
	}

	public boolean seenIt() {
		return seenIt;
	}

	public void setSeenIt(boolean seenIt) {
		this.seenIt = seenIt;
	}

	public boolean wantToSeeIt() {
		return wantToSeeIt;
	}

	public void setWantToSeeIt(boolean wantToSeeIt) {
		this.wantToSeeIt = wantToSeeIt;
	}

	public String getASIN() {
		return ASIN;
	}

	public void setASIN(String ASIN) {
		this.ASIN = ASIN;
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

	public int getBestOfBBCAmerica() {
		return bestOfBBCAmerica;
	}

	public void setBestOfBBCAmerica(int bestOfBBCAmerica) {
		this.bestOfBBCAmerica = bestOfBBCAmerica;
	}

	public int getBestOfDWM2009() {
		return bestOfDWM2009;
	}

	public void setBestOfDWM2009(int bestOfDWM2009) {
		this.bestOfDWM2009 = bestOfDWM2009;
	}

	public int getBestOfDWM2014() {
		return bestOfDWM2014;
	}

	public void setBestOfDWM2014(int bestOfDWM2014) {
		this.bestOfDWM2014 = bestOfDWM2014;
	}

	public int getBestOfAVCTVC10() {
		return bestOfAVCTVC10;
	}

	public void setBestOfAVCTVC10(int bestOfAVCTVC10) {
		this.bestOfAVCTVC10 = bestOfAVCTVC10;
	}

	public int getBestOfIo9() {
		return bestOfIo9;
	}

	public void setBestOfIo9(int bestOfIo9) {
		this.bestOfIo9 = bestOfIo9;
	}

	public int getBestOfLMMyles() {
		return bestOfLMMyles;
	}

	public void setBestOfLMMyles(int bestOfLMMyles) {
		this.bestOfLMMyles = bestOfLMMyles;
	}

	public int getBestOfBahn() {
		return bestOfBahn;
	}

	public void setBestOfBahn(int bestOfBahn) {
		this.bestOfBahn = bestOfBahn;
	}

	public String getTvstoryImage() {
				return tvstoryImage;
	}

	public void setTvstoryImage(String tvstoryImage) {
		this.tvstoryImage = tvstoryImage;
	}

	@Override
	public String toString() {
		return "[" + storyID + ": " + title + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.valueOf(String.valueOf(storyID));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TVStory other = (TVStory) obj;
		if (storyID != other.storyID)
			return false;
		return true;
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

	public static final Creator<TVStory> CREATOR = new Creator<TVStory>() {
		public TVStory createFromParcel(Parcel in) {
			return new TVStory(in);
		}

		public TVStory[] newArray(int size) {
			return new TVStory[size];
		}
	};

}
