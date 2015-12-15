package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 12/9/15.
 */

// TODO COMPLETE CONVERSION TO TIMEVORTEX

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
//	private ArrayList<DWCrew> crew; TODO Implement ArrayList and connection to DWCharacter Class; for now, this is being dealt with as a String
    private String crew;
	private boolean seenIt;
	private boolean wantToSeeIt;
	private String ASIN; // A proprietary ID number for Amazon products, in this case individual DVD titles


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
		this.seasonStoryNumber = Integer.parseInt(in.readString());
		this.episodes = Integer.parseInt(in.readString());
		this.episodeLength = Integer.parseInt(in.readString());
		this.yearProduced = Integer.parseInt(in.readString());
		this.otherCast = in.readString();
		this.synopsis = in.readString();
		this.crew = in.readString();
        this.seenIt = Boolean.parseBoolean(in.readString());
        this.wantToSeeIt = Boolean.parseBoolean(in.readString());
		this.ASIN = in.readString();

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
        this.seenIt = false;
		this.wantToSeeIt = false;
		this.ASIN = ASIN;
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

	@Override
	public String toString() {
		// todo fix this
		return "[" + storyID + ": " + title + "]";
//        return "Note [id=" + mId +
//                ", title=" + mTitle +
//                ", textfield=" + mTextField +
//                ", datecreated=" + mDateCreated +
//                ", hasphoto=" + mHasPhoto +
//                ", hashtags=" + mHashtags +
//                "]";
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
		// todo fix this
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
