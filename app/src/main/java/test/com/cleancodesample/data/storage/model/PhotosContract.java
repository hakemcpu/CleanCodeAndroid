package test.com.cleancodesample.data.storage.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;


public class PhotosContract {
    public final static String CONTENT_AUTHORITY = "com.test.test.content";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public final static String PATH_PHOTO = "photo";

    public static class PhotoTable {
        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTO).build();

        public final static String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHOTO;
        public final static String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHOTO;

        public final static String TABLE_NAME = "Photo";

        public final static String COL_ID = "id";
        public final static String COL_URL = "url";
        public final static String COL_TITLE = "title";

        /*
        Queries
         */
        public static String getCreateQuery() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (");
            builder.append(COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            builder.append(COL_URL+" TEXT NOT NULL, ");
            builder.append(COL_TITLE +" TEXT NOT NULL ");
            builder.append(");");
            return builder.toString();
        }

        public static String getPhotosQuery() {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT * FROM "+TABLE_NAME+" ");
            return builder.toString();
        }

        /*
        Content values.
         */
        public static ContentValues getPhotoContentValues(Photo photo) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PhotoTable.COL_URL, photo.getUrl());
            contentValues.put(PhotoTable.COL_TITLE, photo.getTitle());

            return contentValues;
        }

        /*
        Uris
         */
        public static Uri buildPhotoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPhotoUri() {
            return CONTENT_URI;
        }

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }
}
