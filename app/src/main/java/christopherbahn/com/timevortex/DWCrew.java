package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 12/9/15.
 */
public class DWCrew implements Parcelable {
// This class handles real-world people who worked on the series; actors, writers, directors, etc.
    private int crewID;
    private String name; // The character's full name, i.e. "Ian Chesterton"
    private String shortName; // Shorthand for the character's name, i.e. "Ian," to be used where necessary to improve brevity
    private String job; // i.e. actor, writer, producer, script editor
    private String bio;
    private ArrayList<DWCrew> affiliations; // anyone that this character has a particular link to.


    public DWCrew() {
        super();
    }

    private DWCrew(Parcel in) {
        super();

        this.crewID = Integer.parseInt(in.readString());
        this.name = in.readString();
        this.shortName = in.readString();
        this.job = in.readString();
        this.bio = new String();

    }


    @Override
    public String toString() {
        // todo fix this
        return "[" + crewID + ": " + name + "]";
//        return "TVStory [id=" + mId +
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
        result = prime * result + Integer.valueOf(String.valueOf(crewID));
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
        DWCrew other = (DWCrew) obj;
        if (crewID != other.crewID)
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

    public static final Creator<DWCrew> CREATOR = new Creator<DWCrew>() {
        public DWCrew createFromParcel(Parcel in) {
            return new DWCrew(in);
        }

        public DWCrew[] newArray(int size) {
            return new DWCrew[size];
        }
    };

}
