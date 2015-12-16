package christopherbahn.com.timevortex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TVStoryDAO extends TimeVortexDBDAO {

	private static final String WHERE_ID_EQUALS = DataBaseHelper.COL_STORYID + " =?";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	public TVStoryDAO(Context context) {
		super(context);
	}

	public long save(TVStory TVStory) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.COL_STORYID, TVStory.getStoryID());
		values.put(DataBaseHelper.COL_TITLE, TVStory.getTitle());
		values.put(DataBaseHelper.COL_DOCTOR, TVStory.getDoctor());
		values.put(DataBaseHelper.COL_ERA, TVStory.getEra());
		values.put(DataBaseHelper.COL_SEASON, TVStory.getSeason());
		values.put(DataBaseHelper.COL_SEASONSTORYNUMBER, TVStory.getSeasonStoryNumber());
		values.put(DataBaseHelper.COL_EPISODES, TVStory.getEpisodes());
		values.put(DataBaseHelper.COL_EPISODELENGTH, TVStory.getEpisodeLength());
		values.put(DataBaseHelper.COL_YEARPRODUCED, TVStory.getYearProduced());
		values.put(DataBaseHelper.COL_OTHERCAST, TVStory.getOtherCast());
		values.put(DataBaseHelper.COL_SYNOPSIS, TVStory.getSynopsis());
		values.put(DataBaseHelper.COL_CREW, TVStory.getCrew());
		values.put(DataBaseHelper.COL_SEENIT, TVStory.seenIt());
		values.put(DataBaseHelper.COL_WANTTOSEEIT, TVStory.wantToSeeIt());
		values.put(DataBaseHelper.COL_ASIN, TVStory.getASIN());
		values.put(DataBaseHelper.COL_USERREVIEW, TVStory.getUserReview());
		values.put(DataBaseHelper.COL_USERSTARRATING, TVStory.getUserStarRatingNumber());
		return database.insert(DataBaseHelper.TABLE_TVSTORYS, null, values);
	}

	public long update(TVStory TVStory) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.COL_STORYID, TVStory.getStoryID());
		values.put(DataBaseHelper.COL_TITLE, TVStory.getTitle());
		values.put(DataBaseHelper.COL_DOCTOR, TVStory.getDoctor());
		values.put(DataBaseHelper.COL_ERA, TVStory.getEra());
		values.put(DataBaseHelper.COL_SEASON, TVStory.getSeason());
		values.put(DataBaseHelper.COL_SEASONSTORYNUMBER, TVStory.getSeasonStoryNumber());
		values.put(DataBaseHelper.COL_EPISODES, TVStory.getEpisodes());
		values.put(DataBaseHelper.COL_EPISODELENGTH, TVStory.getEpisodeLength());
		values.put(DataBaseHelper.COL_YEARPRODUCED, TVStory.getYearProduced());
		values.put(DataBaseHelper.COL_OTHERCAST, TVStory.getOtherCast());
		values.put(DataBaseHelper.COL_SYNOPSIS, TVStory.getSynopsis());
		values.put(DataBaseHelper.COL_CREW, TVStory.getCrew());
		values.put(DataBaseHelper.COL_SEENIT, TVStory.seenIt());
		values.put(DataBaseHelper.COL_WANTTOSEEIT, TVStory.wantToSeeIt());
		values.put(DataBaseHelper.COL_ASIN, TVStory.getASIN());
		values.put(DataBaseHelper.COL_USERREVIEW, TVStory.getUserReview());
		values.put(DataBaseHelper.COL_USERSTARRATING, TVStory.getUserStarRatingNumber());

		long result = database.update(DataBaseHelper.TABLE_TVSTORYS, values, WHERE_ID_EQUALS, new String[] { String.valueOf(TVStory.getStoryID()) });
		Log.d("Update Result:", "=" + result);
		return result;

	}

	public int delete(TVStory TVStory) {
		return database.delete(DataBaseHelper.TABLE_TVSTORYS, WHERE_ID_EQUALS, new String[] { TVStory.getStoryID() + "" });
	}

	//Creates ArrayList<TVStory> TVStories from the SQLite DB TABLE_TVSTORYS, USING query() method
	public ArrayList<TVStory> getAllTVStories() {
		ArrayList<TVStory> TVStories = new ArrayList<TVStory>();

		Cursor cursor = database.query(DataBaseHelper.TABLE_TVSTORYS,
				new String[] {
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
				}, null, null, null, null, DataBaseHelper.COL_STORYID + " ASC");

		while (cursor.moveToNext()) {
			TVStory TVStory = new TVStory();

			TVStory.setStoryID(Integer.parseInt(cursor.getString(0)));
			TVStory.setTitle(cursor.getString(1));
			TVStory.setDoctor(Integer.parseInt(cursor.getString(2)));
			TVStory.setEra(cursor.getString(3));
			TVStory.setSeason(cursor.getString(4));
			TVStory.setSeasonStoryNumber(Integer.parseInt(cursor.getString(5)));
			TVStory.setEpisodes(Integer.parseInt(cursor.getString(6)));
			TVStory.setEpisodeLength(Integer.parseInt(cursor.getString(7)));
			TVStory.setYearProduced(Integer.parseInt(cursor.getString(8)));
			TVStory.setOtherCast(cursor.getString(9));
			TVStory.setSynopsis(cursor.getString(10));
			TVStory.setCrew(cursor.getString(11));
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setSeenIt(false);
			} else {
				TVStory.setSeenIt(true);
			}
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setWantToSeeIt(false);
			} else {
				TVStory.setWantToSeeIt(true);
			}
			TVStory.setASIN(cursor.getString(14));
			TVStory.setUserReview(cursor.getString(15));
			TVStory.setUserStarRatingNumber(Float.parseFloat(cursor.getString(16)));
			TVStories.add(TVStory);
		}
		return TVStories;
	}

    //Retrieves a single record with the given id
	public TVStory getTVStory(long id) {
		TVStory TVStory = null;

		String sql = "SELECT * FROM " + DataBaseHelper.TABLE_TVSTORYS + " WHERE " + DataBaseHelper.COL_STORYID + " = ?";

		Cursor cursor = database.rawQuery(sql, new String[] { id + "" });

		if (cursor.moveToNext()) {
			TVStory = new TVStory();
			TVStory.setStoryID(Integer.parseInt(cursor.getString(0)));
			TVStory.setTitle(cursor.getString(1));
			TVStory.setDoctor(Integer.parseInt(cursor.getString(2)));
			TVStory.setEra(cursor.getString(3));
			TVStory.setSeason(cursor.getString(4));
			TVStory.setSeasonStoryNumber(Integer.parseInt(cursor.getString(5)));
			TVStory.setEpisodes(Integer.parseInt(cursor.getString(6)));
			TVStory.setEpisodeLength(Integer.parseInt(cursor.getString(7)));
			TVStory.setYearProduced(Integer.parseInt(cursor.getString(8)));
			TVStory.setOtherCast(cursor.getString(9));
			TVStory.setSynopsis(cursor.getString(10));
			TVStory.setCrew(cursor.getString(11));
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setSeenIt(false);
			} else {
				TVStory.setSeenIt(true);
			}
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setWantToSeeIt(false);
			} else {
				TVStory.setWantToSeeIt(true);
			}
			TVStory.setASIN(cursor.getString(14));
			TVStory.setUserReview(cursor.getString(15));
			TVStory.setUserStarRatingNumber(Float.parseFloat(cursor.getString(16)));
		}
		return TVStory;
	}

	//Retrieves all TVStories that meet the given search criteria
	public ArrayList<TVStory> getSelectedTVStories(SearchTerm searchTerm) {
		ArrayList<TVStory> searchResultTVStories = new ArrayList<TVStory>();
		TVStory TVStory = null;
		String sql = null;
		String sqlSelectFromTVStorys = null;
		String sqlWhere = null;
		String sqlOrderBy = null;
		String searchField;
		String searchParameter;


		sqlWhere = constructSQLWhereString(searchTerm);

		sqlSelectFromTVStorys = "SELECT * FROM " + DataBaseHelper.TABLE_TVSTORYS + " WHERE ";
		sqlOrderBy = "ORDER BY " + DataBaseHelper.COL_STORYID + " ASC";
		sql = sqlSelectFromTVStorys + sqlWhere + sqlOrderBy;

        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
			TVStory = new TVStory();
            TVStory.setStoryID(Integer.parseInt(cursor.getString(0)));
            TVStory.setTitle(cursor.getString(1));
            TVStory.setDoctor(Integer.parseInt(cursor.getString(2)));
            TVStory.setEra(cursor.getString(3));
            TVStory.setSeason(cursor.getString(4));
            TVStory.setSeasonStoryNumber(Integer.parseInt(cursor.getString(5)));
            TVStory.setEpisodes(Integer.parseInt(cursor.getString(6)));
            TVStory.setEpisodeLength(Integer.parseInt(cursor.getString(7)));
            TVStory.setYearProduced(Integer.parseInt(cursor.getString(8)));
            TVStory.setOtherCast(cursor.getString(9));
            TVStory.setSynopsis(cursor.getString(10));
            TVStory.setCrew(cursor.getString(11));
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setSeenIt(false);
			} else {
				TVStory.setSeenIt(true);
			}
			if (Integer.parseInt(cursor.getString(12))==0) {
				TVStory.setWantToSeeIt(false);
			} else {
				TVStory.setWantToSeeIt(true);
			}
			TVStory.setASIN(cursor.getString(14));
			TVStory.setUserReview(cursor.getString(15));
			TVStory.setUserStarRatingNumber(Float.parseFloat(cursor.getString(16)));

			searchResultTVStories.add(TVStory);
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
		if (searchTerm.getSeenIt()=="true") {
			sqlWhere = DataBaseHelper.COL_SEENIT + " IS '" + "1" + "' ";
			andAdder.add(sqlWhere);
		} else if (searchTerm.getSeenIt()=="false") {
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



	//Retrieves all hashtags and compiles them in one ArrayList for display
	public ArrayList<String> getAllHashtags() {
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
