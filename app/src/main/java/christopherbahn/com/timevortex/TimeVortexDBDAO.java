package christopherbahn.com.timevortex;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// DONE???? COMPLETE CONVERSION TO TIMEVORTEX
public class TimeVortexDBDAO {

	protected SQLiteDatabase database;
	private DataBaseHelper dbHelper;
	private Context mContext;

	public TimeVortexDBDAO(Context context) {
		this.mContext = context;
		dbHelper = DataBaseHelper.getHelper(mContext);
		open();
		
	}

	public void open() throws SQLException {
		if(dbHelper == null)
			dbHelper = DataBaseHelper.getHelper(mContext);
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close(); //Closes the database - very important!
		database = null;
	}
}
