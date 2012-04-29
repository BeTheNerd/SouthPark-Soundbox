package stephane.castrec.spbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseSQLite extends SQLiteOpenHelper {

	private static final String TABLE_FAVORITE = "favorite_product";
	private static final String COL_ID = "id";
	private static final String COL_PATH = "path";
	private static final String COL_PERS = "pers";

	
	private static final String CREATE_BDD = "CREATE TABLE " 
		+ TABLE_FAVORITE 
		+ " ("
		+ COL_ID 
		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
		+ COL_PERS
		+ " TEXT NOT NULL, "
		+ COL_PATH
		+ " TEXT NOT NULL"
		+");";
	
	public BaseSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("spbox", "BaseSQLite onCreate starting " + CREATE_BDD);
		db.execSQL(CREATE_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		Log.d("spbox", "BaseSQLite onUpgrade starting");
		db.execSQL("DROP TABLE " + TABLE_FAVORITE + ";");
		onCreate(db);
	}
}
