package christopherbahn.com.timevortex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFAVC10;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFBAHN;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFBBCAMERICA;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFDWM2009;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFDWM2014;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFIO9;
import static christopherbahn.com.timevortex.TVStoryComparator.COMPARE_BY_BESTOFLMMYLES;

public class TVStoryDAO extends TimeVortexDBDAO {

	private static final String WHERE_ID_EQUALS = DataBaseHelper.COL_STORYID + " =?";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	public TVStoryDAO(Context context) {
		super(context);
	}

	public ArrayList<TVStory> getSelectedTVStories(SearchTerm searchTerm, ArrayList<TVStory> allTVStories) {
		ArrayList<TVStory> searchResultTVStories = new ArrayList<TVStory>();
		// populate searchResultTVStories, which will be filtered
		for (TVStory tvStory : allTVStories) {
			searchResultTVStories.add(tvStory);
		}
		// todo: basic search criteria: title, doctor, othercast. Full implementation requires partial text search for Title (so you can pull up both Peladon episodes, etc.) and an ArrayList<DWCharacter> search for Doctor and Othercast.
		// todo: User search/filter criteria: wanttoseeit, seenit, userstarrating. These should all be implemented with Firebase search in mind
		if (searchTerm.cameFromSearchResult() == true) {
			Iterator<TVStory> itr = allTVStories.iterator();
			while (itr.hasNext()) {
			TVStory tvStory = itr.next();
				if (searchTerm.getTitle() != null) {
					int isTitleSearchTermFound = tvStory.getTitle().toLowerCase().indexOf(searchTerm.getTitle().toLowerCase());
					if (isTitleSearchTermFound == - 1) {
						searchResultTVStories.remove(tvStory); // if title searchterm is NOT found
					}
				}
				if (searchTerm.getDoctor() != 0) {
					if (searchTerm.getDoctor() != tvStory.getDoctor()) {
						searchResultTVStories.remove(tvStory);
					}
				}
				if (searchTerm.getOtherCast() != null) {
					int isTitleSearchTermFound = tvStory.getOtherCast().toLowerCase().indexOf(searchTerm.getOtherCast().toLowerCase());
					if (isTitleSearchTermFound == - 1) {
						searchResultTVStories.remove(tvStory);
					}
				}
			}
			// This will order by storyID, BestOf, or whatever criteria
			searchResultTVStories = orderTVStoriesBy(searchTerm, searchResultTVStories);
		} else {
			if (searchTerm.getStoryID() != 0) {
				Iterator<TVStory> itr = allTVStories.iterator();
				while (itr.hasNext()) {
					TVStory tvStory = itr.next();
					if (searchTerm.getStoryID() != 0) {
						if (searchTerm.getStoryID() != tvStory.getStoryID()) {
							searchResultTVStories.remove(tvStory);
						}
					}
				}
			}
			searchResultTVStories = orderTVStoriesBy(searchTerm, allTVStories);
		}
		return searchResultTVStories;
	}

	// TODO Implement user-ranked best-of list and sort.
	public ArrayList<TVStory> orderTVStoriesBy(SearchTerm searchTerm, ArrayList<TVStory> searchResultTVStories) {
		// The ArrayList can be reordered after filtering with Collections.sort(), as per http://stackoverflow.com/questions/16751540/sorting-an-object-arraylist-by-an-attribute-value-in-java. This works by comparing an attribute of one TVStory against the same attribute of another TVStory, and so on down the list. You could do this by Title to sort alphabetically, by Doctor, or by a particular best-of column.
		ArrayList<TVStory> orderedTVStories = new ArrayList<TVStory>();

		// The following if-filter looks for sorting searchTerm, then filters out any nulls or zeroes in that category (meaning, story wasn't ranked in list) into new ArrayList, then sort by chosen category
		if (searchTerm.getBestOfLists() != null) {
			if (searchTerm.getBestOfLists() == "BBCAmerica") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfBBCAmerica() != 0) {
						orderedTVStories.add(TVStory);
						}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFBBCAMERICA));
					}
				return orderedTVStories;
				}
			if (searchTerm.getBestOfLists() == "DWM2009") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfDWM2009() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFDWM2009));
				}
				return orderedTVStories;
			}
			if (searchTerm.getBestOfLists() == "DWM2014") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfDWM2014() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFDWM2014));
				}
				return orderedTVStories;
			}
			if (searchTerm.getBestOfLists() == "AVCTVC10") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfAVCTVC10() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFAVC10));
				}
				return orderedTVStories;
			}
			if (searchTerm.getBestOfLists() == "Io9") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfIo9() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFIO9));
				}
				return orderedTVStories;
			}
			if (searchTerm.getBestOfLists() == "LMMyles") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfLMMyles() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFLMMYLES));
				}
				return orderedTVStories;
			}
			if (searchTerm.getBestOfLists() == "Bahn") {
				for (TVStory TVStory : searchResultTVStories) {
					if (TVStory.getBestOfBahn() != 0) {
						orderedTVStories.add(TVStory);
					}
					Collections.sort(orderedTVStories, new TVStoryComparator(COMPARE_BY_BESTOFBAHN));
				}
				return orderedTVStories;
			}
			return searchResultTVStories;
		}
		return searchResultTVStories;
	}


	// Saves changes to the user's flags/ratings/reviews on a particular TVStory
	public long update(UserTVStoryInfo userTVStoryInfo) {

		return '0';
	}


}


