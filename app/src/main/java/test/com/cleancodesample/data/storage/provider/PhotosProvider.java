package test.com.cleancodesample.data.storage.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.Nullable;

import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.data.storage.database.DatabaseHelper;

/**
 * Created by hzaied on 7/9/16.
 */
public class PhotosProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int PHOTO = 100;
    private static final int PHOTO_WITH_ID = 101;

    private DatabaseHelper mDatabaseHelper;


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = PhotosContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, PhotosContract.PATH_PHOTO, PHOTO);
        matcher.addURI(authority, PhotosContract.PATH_PHOTO + "/#", PHOTO_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int type = sUriMatcher.match(uri);

        switch (type) {
            case PHOTO:
                return PhotosContract.PhotoTable.CONTENT_TYPE;
            case PHOTO_WITH_ID:
                return PhotosContract.PhotoTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case PHOTO:
                returnCursor = mDatabaseHelper.getPhotosDatabaseHandler().getPhotosCursor(mDatabaseHelper, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PHOTO_WITH_ID: {
                long id = PhotosContract.PhotoTable.getIdFromUri(uri);
                returnCursor = mDatabaseHelper.getPhotosDatabaseHandler().getPhotosCursor(mDatabaseHelper, projection, PhotosContract.PhotoTable.TABLE_NAME+"."+PhotosContract.PhotoTable.COL_ID+"= ?",
                        new String[]{id+""}, null, null, sortOrder);
                break;
            }
            default:
                returnCursor = null;
                break;
        }

        if(returnCursor != null)
            returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri returnUri = null;
        switch (sUriMatcher.match(uri)) {
            case PHOTO: {
                long id = mDatabaseHelper.getPhotosDatabaseHandler().addPhoto(mDatabaseHelper, contentValues);
                if(id == -1) {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                returnUri = PhotosContract.PhotoTable.buildPhotoUri(id);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        if ( null == selection ) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case PHOTO: {
                rowsDeleted = mDatabaseHelper.getPhotosDatabaseHandler().deletePhoto(mDatabaseHelper, selection, selectionArgs);
                break;
            }
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;

        switch (sUriMatcher.match(uri)) {
            case PHOTO: {
                rowsUpdated = mDatabaseHelper.getPhotosDatabaseHandler().updatePhoto(mDatabaseHelper, contentValues, selection, selectionArgs);
                break;
            }
        }

        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int insertedRows = 0;
        switch (sUriMatcher.match(uri)) {
            case PHOTO: {
                insertedRows = mDatabaseHelper.getPhotosDatabaseHandler().addPhotos(mDatabaseHelper, values);
                break;
            }
        }

        if (insertedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return insertedRows;
    }
}
