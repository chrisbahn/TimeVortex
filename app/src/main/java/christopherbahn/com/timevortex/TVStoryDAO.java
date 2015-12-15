package christopherbahn.com.timevortex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

// TODO DONE??? COMPLETE CONVERSION TO TIMEVORTEX

public class TVStoryDAO extends TimeVortexDBDAO {
    // Adapted/refactored from EmployeeDAO

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
						DataBaseHelper.COL_ASIN
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
			TVStory.setSeenIt(Boolean.parseBoolean(cursor.getString(12)));
			TVStory.setWantToSeeIt(Boolean.parseBoolean(cursor.getString(13)));
			TVStory.setASIN(cursor.getString(14));
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
			TVStory.setSeenIt(Boolean.parseBoolean(cursor.getString(12)));
			TVStory.setWantToSeeIt(Boolean.parseBoolean(cursor.getString(13)));
			TVStory.setASIN(cursor.getString(14));
		}
		return TVStory;
	}

	//Retrieves all TVStories that meet the given search criteria for TVStory Textfields and hashtags
	public ArrayList<TVStory> getSelectedTVStories(String searchField, String searchParameter) {
		ArrayList<TVStory> searchResultTVStories = new ArrayList<TVStory>();
		TVStory TVStory = null;
		String sql = null;
        // TODO Neither of these searches really works - functionality is based on Inspirer and not yet built to TimeVortex specs
		if (searchField == "searchByTitle") {
			sql = "SELECT * FROM " + DataBaseHelper.TABLE_TVSTORYS + " WHERE " + DataBaseHelper.COL_TITLE + " LIKE '%" + searchParameter + "%' ORDER BY " + DataBaseHelper.COL_STORYID + " ASC";
		} else if (searchField == "searchByDoctor") {
			sql = "SELECT * FROM " + DataBaseHelper.TABLE_TVSTORYS + " WHERE " + DataBaseHelper.COL_DOCTOR + " IS '" + searchParameter + "' ORDER BY " + DataBaseHelper.COL_STORYID + " ASC";
		}


		Log.d("searchField:", "=" + searchField);
		Log.d("searchParameter:", "=" + searchParameter);

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
            TVStory.setSeenIt(Boolean.parseBoolean(cursor.getString(12)));
            TVStory.setWantToSeeIt(Boolean.parseBoolean(cursor.getString(13)));
			TVStory.setASIN(cursor.getString(14));

			searchResultTVStories.add(TVStory);
		}
		return searchResultTVStories;
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
						DataBaseHelper.COL_ASIN
				}, null, null, null, null, DataBaseHelper.COL_STORYID + " DESC");

		while (cursor.moveToNext()) {
			allHashtags.add(cursor.getString(6)); // todo this is left over from previous iteration of program, but it might be useful later on so I'm leaving it for now, rather than deleting
		}
		return allHashtags;
	}



}
