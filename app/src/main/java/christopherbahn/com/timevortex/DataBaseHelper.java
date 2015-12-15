package christopherbahn.com.timevortex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// TODO COMPLETE CONVERSION TO TIMEVORTEX
// DONE??? full implementation for TABLE_TVSTORYS (that is, with no further changes to the structure of the TVStory object)
// todo full implementation for TABLE_DWCHARACTERS
// todo full implementation for TABLE_DWCREW

public class DataBaseHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "TimeVortex";
	private static final int DATABASE_VERSION = 6;

	static final String KEY_ROWID = "_id";
	protected static final String TABLE_DWCHARACTERS = "dwcharacters_table";
	protected static final String TABLE_DWCREW = "dwcrew_table";
	protected static final String TABLE_TVSTORYS = "tvstorys_table";

	// these are shared by both Notes and Photos tables
	protected static final String COL_STORYID = "storyid";
	protected static final String COL_TITLE = "title";
	protected static final String COL_DOCTOR = "doctor";
    protected static final String COL_ERA = "era";
    protected static final String COL_SEASON = "season";
    protected static final String COL_SEASONSTORYNUMBER = "seasonstorynumber";
    protected static final String COL_EPISODES = "episodes";
    protected static final String COL_EPISODELENGTH = "episodelength";
    protected static final String COL_YEARPRODUCED = "yearproduced";
    protected static final String COL_OTHERCAST = "othercast"; // TODO Implement ArrayList functionality
    protected static final String COL_SYNOPSIS = "synopsis";
    protected static final String COL_CREW = "crew"; // TODO Implement ArrayList functionality
    protected static final String COL_SEENIT = "seenit";
	protected static final String COL_WANTTOSEEIT = "wanttoseeit";
	protected static final String COL_ASIN = "asin";



    private static final String DBTAG = "DatabaseManager" ;
	private static final String SQLTAG = "SQLHelper" ;

	private static DataBaseHelper instance;

	public static synchronized DataBaseHelper getHelper(Context context) {
		if (instance == null)
			instance = new DataBaseHelper(context);
		return instance;
	}

	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTVStoryTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TVSTORYS + " (" +
                COL_STORYID +" INTEGER PRIMARY KEY, " +
				COL_TITLE + " TEXT, " +
                COL_DOCTOR + " INTEGER, " +
                COL_ERA + " TEXT, " +
                COL_SEASON + " TEXT, " +
                COL_SEASONSTORYNUMBER + " INTEGER, " +
                COL_EPISODES + " INTEGER, " +
                COL_EPISODELENGTH + " INTEGER, " +
                COL_YEARPRODUCED + " INTEGER, " +
                COL_OTHERCAST + " TEXT, " +
                COL_SYNOPSIS + " TEXT, " +
                COL_CREW + " TEXT, " +
                COL_SEENIT + " TEXT, " +
				COL_WANTTOSEEIT + " TEXT, " +
				COL_ASIN + " TEXT);";
		String createDWCharactersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_DWCHARACTERS + " (" +
                COL_STORYID +" INTEGER PRIMARY KEY, " +
                COL_TITLE + " TEXT);";
		String createDWCrewTable = "CREATE TABLE IF NOT EXISTS " + TABLE_DWCREW + " (" +
                COL_STORYID +" INTEGER PRIMARY KEY, " +
                COL_TITLE + " TEXT);";

		db.execSQL(createTVStoryTable);
		db.execSQL(createDWCharactersTable);
		db.execSQL(createDWCrewTable);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSTORYS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DWCHARACTERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DWCREW);
		onCreate(db);
		Log.w(SQLTAG, "Upgrade table - drop and recreate it");
	}





}
