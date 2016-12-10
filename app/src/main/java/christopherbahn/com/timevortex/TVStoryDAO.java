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

	// Creates a new TVStory record in SQLite db
	public long save(TVStory TVStory) {
		ContentValues values = new ContentValues();
//		values.put(DataBaseHelper.COL_STORYID, TVStory.getStoryID());
//		values.put(DataBaseHelper.COL_TITLE, TVStory.getTitle());
//		values.put(DataBaseHelper.COL_DOCTOR, TVStory.getDoctor());
//		values.put(DataBaseHelper.COL_ERA, TVStory.getEra());
//		values.put(DataBaseHelper.COL_SEASON, TVStory.getSeason());
//		values.put(DataBaseHelper.COL_SEASONSTORYNUMBER, TVStory.getSeasonStoryNumber());
//		values.put(DataBaseHelper.COL_EPISODES, TVStory.getEpisodes());
//		values.put(DataBaseHelper.COL_EPISODELENGTH, TVStory.getEpisodeLength());
//		values.put(DataBaseHelper.COL_YEARPRODUCED, TVStory.getYearProduced());
//		values.put(DataBaseHelper.COL_OTHERCAST, TVStory.getOtherCast());
//		values.put(DataBaseHelper.COL_SYNOPSIS, TVStory.getSynopsis());
//		values.put(DataBaseHelper.COL_CREW, TVStory.getCrew());
//		values.put(DataBaseHelper.COL_SEENIT, TVStory.seenIt());
//		values.put(DataBaseHelper.COL_WANTTOSEEIT, TVStory.wantToSeeIt());
//		values.put(DataBaseHelper.COL_ASIN, TVStory.getASIN());
//		values.put(DataBaseHelper.COL_USERREVIEW, TVStory.getUserReview());
//		values.put(DataBaseHelper.COL_USERSTARRATING, TVStory.getUserStarRatingNumber());
//		values.put(DataBaseHelper.COL_BESTOFBBCAMERICA, TVStory.getBestOfBBCAmerica());
//		values.put(DataBaseHelper.COL_BESTOFDWM2009, TVStory.getBestOfDWM2009());
//		values.put(DataBaseHelper.COL_BESTOFDWM2014, TVStory.getBestOfDWM2014());
//		values.put(DataBaseHelper.COL_BESTOFAVCTVC10, TVStory.getBestOfAVCTVC10());
//		values.put(DataBaseHelper.COL_BESTOFIO9, TVStory.getBestOfIo9());
//		values.put(DataBaseHelper.COL_BESTOFLMMYLES, TVStory.getBestOfLMMyles());
//		values.put(DataBaseHelper.COL_BESTOFBAHN, TVStory.getBestOfBahn());
//		values.put(DataBaseHelper.COL_TVSTORYIMAGE, TVStory.getTvstoryImage());
//
//		return database.insert(DataBaseHelper.TABLE_TVSTORYS, null, values);
		return '0';
	}

	// Saves changes to a currently existing TVStory record in SQLite db
	public long update(TVStory TVStory) {
//		ContentValues values = new ContentValues();
//		values.put(DataBaseHelper.COL_STORYID, TVStory.getStoryID());
//		values.put(DataBaseHelper.COL_TITLE, TVStory.getTitle());
//		values.put(DataBaseHelper.COL_DOCTOR, TVStory.getDoctor());
//		values.put(DataBaseHelper.COL_ERA, TVStory.getEra());
//		values.put(DataBaseHelper.COL_SEASON, TVStory.getSeason());
//		values.put(DataBaseHelper.COL_SEASONSTORYNUMBER, TVStory.getSeasonStoryNumber());
//		values.put(DataBaseHelper.COL_EPISODES, TVStory.getEpisodes());
//		values.put(DataBaseHelper.COL_EPISODELENGTH, TVStory.getEpisodeLength());
//		values.put(DataBaseHelper.COL_YEARPRODUCED, TVStory.getYearProduced());
//		values.put(DataBaseHelper.COL_OTHERCAST, TVStory.getOtherCast());
//		values.put(DataBaseHelper.COL_SYNOPSIS, TVStory.getSynopsis());
//		values.put(DataBaseHelper.COL_CREW, TVStory.getCrew());
//		values.put(DataBaseHelper.COL_SEENIT, TVStory.seenIt());
//		values.put(DataBaseHelper.COL_WANTTOSEEIT, TVStory.wantToSeeIt());
//		values.put(DataBaseHelper.COL_ASIN, TVStory.getASIN());
//		values.put(DataBaseHelper.COL_USERREVIEW, TVStory.getUserReview());
//		values.put(DataBaseHelper.COL_USERSTARRATING, TVStory.getUserStarRatingNumber());
//		values.put(DataBaseHelper.COL_BESTOFBBCAMERICA, TVStory.getBestOfBBCAmerica());
//		values.put(DataBaseHelper.COL_BESTOFDWM2009, TVStory.getBestOfDWM2009());
//		values.put(DataBaseHelper.COL_BESTOFDWM2014, TVStory.getBestOfDWM2014());
//		values.put(DataBaseHelper.COL_BESTOFAVCTVC10, TVStory.getBestOfAVCTVC10());
//		values.put(DataBaseHelper.COL_BESTOFIO9, TVStory.getBestOfIo9());
//		values.put(DataBaseHelper.COL_BESTOFLMMYLES, TVStory.getBestOfLMMyles());
//		values.put(DataBaseHelper.COL_BESTOFBAHN, TVStory.getBestOfBahn());
//		values.put(DataBaseHelper.COL_TVSTORYIMAGE, TVStory.getTvstoryImage());
//
//		long result = database.update(DataBaseHelper.TABLE_TVSTORYS, values, WHERE_ID_EQUALS, new String[] { String.valueOf(TVStory.getStoryID()) });
//		Log.d("Update Result:", "=" + result);
//		return result;
		return '0';
	}

	// deletes a TVStory record from SQLite db
	public int delete(TVStory TVStory) {
		return database.delete(DataBaseHelper.TABLE_TVSTORYS, WHERE_ID_EQUALS, new String[]{TVStory.getStoryID() + ""});
	}

	//Creates ArrayList<TVStory> TVStories from the SQLite DB TABLE_TVSTORYS, USING query() method
	public ArrayList<TVStory> getAllTVStories() {
		ArrayList<TVStory> TVStories = new ArrayList<TVStory>();

//		Cursor cursor = database.query(DataBaseHelper.TABLE_TVSTORYS,
//				new String[] {
//						DataBaseHelper.COL_STORYID,
//		                DataBaseHelper.COL_TITLE,
//                		DataBaseHelper.COL_DOCTOR,
//		                DataBaseHelper.COL_ERA,
//		                DataBaseHelper.COL_SEASON,
//		                DataBaseHelper.COL_SEASONSTORYNUMBER,
//		                DataBaseHelper.COL_EPISODES,
//		                DataBaseHelper.COL_EPISODELENGTH,
//		                DataBaseHelper.COL_YEARPRODUCED,
//		                DataBaseHelper.COL_OTHERCAST,
//		                DataBaseHelper.COL_SYNOPSIS,
//		                DataBaseHelper.COL_CREW,
//		                DataBaseHelper.COL_SEENIT,
//		                DataBaseHelper.COL_WANTTOSEEIT,
//						DataBaseHelper.COL_ASIN,
//						DataBaseHelper.COL_USERREVIEW,
//						DataBaseHelper.COL_USERSTARRATING,
//						DataBaseHelper.COL_BESTOFBBCAMERICA,
//						DataBaseHelper.COL_BESTOFDWM2009,
//						DataBaseHelper.COL_BESTOFDWM2014,
//						DataBaseHelper.COL_BESTOFAVCTVC10,
//						DataBaseHelper.COL_BESTOFIO9,
//						DataBaseHelper.COL_BESTOFLMMYLES,
//						DataBaseHelper.COL_BESTOFBAHN,
//						DataBaseHelper.COL_TVSTORYIMAGE
//				}, null, null, null, null, DataBaseHelper.COL_STORYID + " ASC");
//		while (cursor.moveToNext()) {
//			TVStory TVStory = new TVStory();
//			TVStory.setStoryID(Integer.parseInt(cursor.getString(0)));
//			TVStory.setTitle(cursor.getString(1));
//			TVStory.setDoctor(Integer.parseInt(cursor.getString(2)));
//			TVStory.setEra(cursor.getString(3));
//			TVStory.setSeason(cursor.getString(4));
//			TVStory.setSeasonStoryNumber(Integer.parseInt(cursor.getString(5)));
//			TVStory.setEpisodes(Integer.parseInt(cursor.getString(6)));
//			TVStory.setEpisodeLength(Integer.parseInt(cursor.getString(7)));
//			TVStory.setYearProduced(Integer.parseInt(cursor.getString(8)));
//			TVStory.setOtherCast(cursor.getString(9));
//			TVStory.setSynopsis(cursor.getString(10));
//			TVStory.setCrew(cursor.getString(11));
//			if (Integer.parseInt(cursor.getString(12))==0) {
//				TVStory.setSeenIt(false);
//			} else {
//				TVStory.setSeenIt(true);
//			}
//			if (Integer.parseInt(cursor.getString(13))==0) {
//				TVStory.setWantToSeeIt(false);
//			} else {
//				TVStory.setWantToSeeIt(true);
//			}
//			TVStory.setASIN(cursor.getString(14));
//			TVStory.setUserReview(cursor.getString(15));
//			TVStory.setUserStarRatingNumber(Float.parseFloat(cursor.getString(16)));
//			TVStory.setBestOfBBCAmerica(Integer.parseInt(cursor.getString(17)));
//			TVStory.setBestOfDWM2009(Integer.parseInt(cursor.getString(18)));
//			TVStory.setBestOfDWM2014(Integer.parseInt(cursor.getString(19)));
//			TVStory.setBestOfAVCTVC10(Integer.parseInt(cursor.getString(20)));
//			TVStory.setBestOfIo9(Integer.parseInt(cursor.getString(21)));
//			TVStory.setBestOfLMMyles(Integer.parseInt(cursor.getString(22)));
//			TVStory.setBestOfBahn(Integer.parseInt(cursor.getString(23)));
//			TVStory.setTvstoryImage(cursor.getString(24));
//			TVStories.add(TVStory);
//			// TODO the following code would aid in sort/ordering within the Java environment, without using either SQL (which is being replaced) or Firebase (which has awful search capability).
////			if (TVStory.getDoctor()==12) {
////				TVStories.add(TVStory);
////			}
//		}
		return TVStories;
	}

	//Retrieves a single record with the given id -- only used with the Randomizer, could be subsumed into getSelectedTVStories().
	public TVStory getTVStory(long id) {
		TVStory TVStory = null;

		String sql = "SELECT * FROM " + DataBaseHelper.TABLE_TVSTORYS + " WHERE " + DataBaseHelper.COL_STORYID + " = ?";

//		Cursor cursor = database.rawQuery(sql, new String[] { id + "" });
//
//		if (cursor.moveToNext()) {
//			TVStory = new TVStory();
//			TVStory.setStoryID(Integer.parseInt(cursor.getString(0)));
//			TVStory.setTitle(cursor.getString(1));
//			TVStory.setDoctor(Integer.parseInt(cursor.getString(2)));
//			TVStory.setEra(cursor.getString(3));
//			TVStory.setSeason(cursor.getString(4));
//			TVStory.setSeasonStoryNumber(Integer.parseInt(cursor.getString(5)));
//			TVStory.setEpisodes(Integer.parseInt(cursor.getString(6)));
//			TVStory.setEpisodeLength(Integer.parseInt(cursor.getString(7)));
//			TVStory.setYearProduced(Integer.parseInt(cursor.getString(8)));
//			TVStory.setOtherCast(cursor.getString(9));
//			TVStory.setSynopsis(cursor.getString(10));
//			TVStory.setCrew(cursor.getString(11));
//			if (Integer.parseInt(cursor.getString(12))==0) {
//				TVStory.setSeenIt(false);
//			} else {
//				TVStory.setSeenIt(true);
//			}
//			if (Integer.parseInt(cursor.getString(13))==0) {
//				TVStory.setWantToSeeIt(false);
//			} else {
//				TVStory.setWantToSeeIt(true);
//			}
//			TVStory.setASIN(cursor.getString(14));
//			TVStory.setUserReview(cursor.getString(15));
//			TVStory.setUserStarRatingNumber(Float.parseFloat(cursor.getString(16)));
//			TVStory.setBestOfBBCAmerica(Integer.parseInt(cursor.getString(17)));
//			TVStory.setBestOfDWM2009(Integer.parseInt(cursor.getString(18)));
//			TVStory.setBestOfDWM2014(Integer.parseInt(cursor.getString(19)));
//			TVStory.setBestOfAVCTVC10(Integer.parseInt(cursor.getString(20)));
//			TVStory.setBestOfIo9(Integer.parseInt(cursor.getString(21)));
//			TVStory.setBestOfLMMyles(Integer.parseInt(cursor.getString(22)));
//			TVStory.setBestOfBahn(Integer.parseInt(cursor.getString(23)));
//			TVStory.setTvstoryImage(cursor.getString(24));
//		}
		return TVStory;
	}

	//Retrieves all TVStories that meet the given search criteria. TODO This is going to be the main way the TVStories list is filtered, but by using Java and maaaybe Firebase's ordering system. You will need to pass in the ArrayList<TVStory> TVStories made by the Firebase listener in MainActivity as a parameter here. TVStories is then filtered into searchResultTVStories. This should work for all search types including a list-click selection, which is really a search-by-ID that will return one and only one record. Probably useful to keep both single and multiple-return methods since they return Object and ArrayList respectively and will be dealt with differently when called.
	// PLAN: 1) allTVStories is checked in here. 2) Iterate through searchTerm and for any positive match, add that TVStory to searchResultTVStories. 3. Check searchResultTVStories into Firebase under node searchResultTVStories. 4) Listener is triggered which checks searchResultTVStories back in from Firebase, which also orders the data asc/desc by chosen sub-node. 5. New ArrayList<TVStory> searchResultTVStories is sent to TVStoryListFragment

	public ArrayList<TVStory> getSelectedTVStories(SearchTerm searchTerm, ArrayList<TVStory> allTVStories) {
		ArrayList<TVStory> searchResultTVStories = new ArrayList<TVStory>();
		// populate searchResultTVStories, which will be filtered
		for (TVStory tvStory : allTVStories) {
			searchResultTVStories.add(tvStory);
		}

		// todo DONE needs a way to skip filtering if you clicked on "see all episodes"
		// todo needs a search by storyID in order to select a single episode.
		// todo: basic search criteria: title, doctor, othercast. Full implementation requires partial text search for Title (so you can pull up both Peladon episodes, etc.) and an ArrayList<DWCharacter> search for Doctor and Othercast. Tackling title first.
		// todo: User search/filter criteria: wanttoseeit, seenit, userstarrating. These should all be implemented with Firebase search in mind
		// todo how to implement search on multiple criteria. Is iterating through categories sufficient? If not, searchTerm could have a new parameter added that would +1 every time a search criterion was chosen elsewhere, and -1 for every search criterion found and filtered here. Or is there a way to make it work without a new parameter? Yes, I think so: Add the ++ flag in the for loop below to each if instance, and only add the story to the ArrayList if it gets to the end and flag != 0.
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
			searchResultTVStories = orderTVStoriesBy(searchTerm, allTVStories);
		}
		return searchResultTVStories;
	}

	public String constructSQLWhereString(SearchTerm searchTerm) {
		String sqlWhere = "";
		ArrayList<String> andAdder = new ArrayList<String>();

		if (searchTerm.getTitle() != null) {
			sqlWhere = DataBaseHelper.COL_TITLE + " LIKE '%" + searchTerm.getTitle() + "%' ";
			andAdder.add(sqlWhere);
		}
		if (searchTerm.getDoctor() != 0) { // 0 means no Doctor was selected
			sqlWhere = DataBaseHelper.COL_DOCTOR + " IS '" + searchTerm.getDoctor() + "' ";
			andAdder.add(sqlWhere);
		}
		if (searchTerm.getOtherCast() != null) {
			sqlWhere = DataBaseHelper.COL_OTHERCAST + " LIKE '%" + searchTerm.getOtherCast() + "%' ";
			System.out.println(searchTerm.getOtherCast());
			andAdder.add(sqlWhere);
		}
		if (searchTerm.isWantToSeeIt()) {
			sqlWhere = DataBaseHelper.COL_WANTTOSEEIT + " IS '" + "1" + "' ";
			andAdder.add(sqlWhere);
		}
		//  SeenIt/Haven'tSeenIt/Both ("both" simply doesn't add a limiting search term)
		if (searchTerm.getSeenIt() == "true") {
			sqlWhere = DataBaseHelper.COL_SEENIT + " IS '" + "1" + "' ";
			andAdder.add(sqlWhere);
		} else if (searchTerm.getSeenIt() == "false") {
			sqlWhere = DataBaseHelper.COL_SEENIT + " IS '" + "0" + "' ";
			andAdder.add(sqlWhere);
		}
		if (searchTerm.getUserStarRatingNumber() != 0) {
			sqlWhere = DataBaseHelper.COL_USERSTARRATING + " is '" + searchTerm.getUserStarRatingNumber() + "' ";
			andAdder.add(sqlWhere);
		}

		for (int i = 0; i < andAdder.size(); i++) {
			if (i == 0) {
				sqlWhere = andAdder.get(i);
			} else
				sqlWhere = sqlWhere + " AND " + andAdder.get(i);
		}

		// DOESN'T WORK This removes the first "AND" so that the search string doesn't start with one.
//		sqlWhere.replace(" AND STRINGENDER", " ");

		return sqlWhere;
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


		//Retrieves all hashtags and compiles them in one ArrayList for display
		public ArrayList<String> getAllHashtags () {
			ArrayList<String> allHashtags = new ArrayList<String>();

			Cursor cursor = database.query(DataBaseHelper.TABLE_TVSTORYS,
					new String[]{
							DataBaseHelper.COL_STORYID,
							DataBaseHelper.COL_TITLE,
							DataBaseHelper.COL_DOCTOR,
							DataBaseHelper.COL_ERA,
							DataBaseHelper.COL_SEASON,
							DataBaseHelper.COL_SEASONSTORYNUMBER,
							DataBaseHelper.COL_EPISODES,
							DataBaseHelper.COL_EPISODELENGTH,
							DataBaseHelper.COL_YEARPRODUCED,
							DataBaseHelper.COL_OTHERCAST,
							DataBaseHelper.COL_SYNOPSIS,
							DataBaseHelper.COL_CREW,
							DataBaseHelper.COL_SEENIT,
							DataBaseHelper.COL_WANTTOSEEIT,
							DataBaseHelper.COL_ASIN,
							DataBaseHelper.COL_USERREVIEW,
							DataBaseHelper.COL_USERSTARRATING
					}, null, null, null, null, DataBaseHelper.COL_STORYID + " DESC");

			while (cursor.moveToNext()) {
				allHashtags.add(cursor.getString(6)); // todo this is left over from previous iteration of program, but it might be useful later on so I'm leaving it for now, rather than deleting
			}
			return allHashtags;
		}

	}


