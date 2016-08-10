package test.com.cleancodesample.data.storage.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import test.com.cleancodesample.data.storage.model.Photo;
import test.com.cleancodesample.data.storage.model.PhotosContract;

public class PhotosDatabaseHandler extends DatabaseHelper.DatabaseHandler {

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PhotosContract.PhotoTable.getCreateQuery());
    }

    /*
    Retrieving cursors
     */
    public Cursor getPhotosCursor(DatabaseHelper helper, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder) {
        SQLiteDatabase database = helper.getDatabase();
        return database.query(PhotosContract.PhotoTable.TABLE_NAME, projection, selection, selectionArgs, groupBy, having, sortOrder);
    }

    public long addPhoto(DatabaseHelper helper, ContentValues values) {
        SQLiteDatabase database = helper.getDatabase();
        return database.insert(PhotosContract.PhotoTable.TABLE_NAME, null, values);
    }

    public int addPhotos(DatabaseHelper helper, ContentValues[] values) {
        SQLiteDatabase database = helper.getDatabase();
        int count = 0;
        try {
            database.beginTransaction();
            for(ContentValues value : values) {
                long id = database.insert(PhotosContract.PhotoTable.TABLE_NAME, null, value);
                if(id != -1)
                    count++;
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return count;
    }

    public int deletePhoto(DatabaseHelper helper, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getDatabase();
        return database.delete(PhotosContract.PhotoTable.TABLE_NAME, selection, selectionArgs);
    }

    public int updatePhoto(DatabaseHelper helper, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getDatabase();
        return database.update(PhotosContract.PhotoTable.TABLE_NAME, values, selection, selectionArgs);
    }

    public static class PhotosCursorWrapper extends CursorWrapper {
        public PhotosCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Photo getPhoto() {
            long id = getLong(getColumnIndex(PhotosContract.PhotoTable.COL_ID));
            String url = getString(getColumnIndex(PhotosContract.PhotoTable.COL_URL));
            String title = getString(getColumnIndex(PhotosContract.PhotoTable.COL_TITLE));
            return new Photo(id, title, url);
        }
    }

}
