package test.com.cleancodesample.data.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "photos.db";//Environment.getExternalStorageDirectory()+"/photos.db";
    private final static int VERSION = 1;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private DatabaseHandler[] mHandlers = new DatabaseHandler[] {new PhotosDatabaseHandler()};
    private final static int PHOTO_DB_HANDLER_INDEX = 0;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context.getApplicationContext();
        mDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for(DatabaseHandler handler : mHandlers) {
            handler.onCreate(sqLiteDatabase);
        }
        // TODO: close sqLiteDatabase instance
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public PhotosDatabaseHandler getPhotosDatabaseHandler() {
        return (PhotosDatabaseHandler) mHandlers[PHOTO_DB_HANDLER_INDEX];
    }

    public static abstract class DatabaseHandler {
        abstract public void onCreate(SQLiteDatabase sqLiteDatabase);
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}
    }
}
