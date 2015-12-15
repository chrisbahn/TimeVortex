package christopherbahn.com.timevortex;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by christopherbahn on 12/9/15.
 */
public class DWCharacter implements Parcelable {
// This class handles fictional characters appearing in individual TVStory objects, which will be important in implementing searches and in improving the informational and readability aspects of the app.
    private int characterID;
    private String name; // The character's full name, i.e. "Ian Chesterton"
    private String shortName; // Shorthand for the character's name, i.e. "Ian," to be used where necessary to improve brevity
    private int characterType; // (0 = Doctor, 1 = Companion, 2 = Master, 3 = Antagonist, 4 = Group, 5 = Other Cast). Uses for this variable include sorting to determine proper display order of cast lists. "Group" is a mass term, intended to cover groups that deserve their own listing, i.e. aliens like the Daleks or Sontarans, and organizations like UNIT or the Paternoster Gang.
    private String actor; // This should probably be a Crew object instead of String, but String works for now. There may be issues related to characters played by several actors, i.e. Davros. Special case: Each incarnation of the Master should probably have his/her own DWCharacter.
    private String bio;
    private ArrayList<DWCharacter> affiliations; // anyone that this character has a particular link to.



    public DWCharacter() {
        super();
    }

    private DWCharacter(Parcel in) {
        super();

        this.characterID = Integer.parseInt(in.readString());
        this.name = in.readString();
        this.shortName = in.readString();
        this.characterType = Integer.parseInt(in.readString());
        this.actor = new String();
        this.bio = new String();
        this.affiliations = new ArrayList<DWCharacter>();

    }


    @Override
    public String toString() {
        // todo fix this
        return "[" + characterID + ": " + name + "]";
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
        result = prime * result + Integer.valueOf(String.valueOf(characterID));
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
        DWCharacter other = (DWCharacter) obj;
        if (characterID != other.characterID)
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

    public static final Creator<DWCharacter> CREATOR = new Creator<DWCharacter>() {
        public DWCharacter createFromParcel(Parcel in) {
            return new DWCharacter(in);
        }

        public DWCharacter[] newArray(int size) {
            return new DWCharacter[size];
        }
    };

}
