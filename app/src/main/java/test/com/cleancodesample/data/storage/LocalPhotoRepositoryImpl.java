package test.com.cleancodesample.data.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.data.storage.converter.StorageModelConverter;
import test.com.cleancodesample.data.storage.database.PhotosDatabaseHandler;
import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * The implementation of the Local database repository for the photos.
 */
public class LocalPhotoRepositoryImpl implements PhotoRepository {

    private Context mContext;

    @Inject public LocalPhotoRepositoryImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public List<Photo> getPhotos() {
        Uri uri = PhotosContract.PhotoTable.buildPhotoUri();
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0)
            return null;
        else {
            List<Photo> photos = new ArrayList<>();

            PhotosDatabaseHandler.PhotosCursorWrapper photosCursorWrapper = null;
            try {
                photosCursorWrapper = new PhotosDatabaseHandler.PhotosCursorWrapper(cursor);
                // Use the cursor wrapper to get the Storage entity right away.
                photosCursorWrapper.moveToFirst();
                while (!photosCursorWrapper.isAfterLast()) {
                    Photo photo = StorageModelConverter.convertToDomainModel(photosCursorWrapper.getPhoto());
                    photos.add(photo);
                    photosCursorWrapper.moveToNext();
                }
            } finally {
                if(photosCursorWrapper != null)
                    photosCursorWrapper.close();
            }

            return photos;
        }
    }

    @Override
    public void addPhotos(List<Photo> photos) {
        // Delete all before retrieving.
        Uri uri = PhotosContract.PhotoTable.buildPhotoUri();
        mContext.getContentResolver().delete(uri, null, null);

        ContentValues[] values = new ContentValues[photos.size()];

        // Convert to the storage model from the domain model.
        List<test.com.cleancodesample.data.storage.model.Photo> storagePhotos =
                StorageModelConverter.convertToStorageModel(photos);
        int index = 0;
        for(test.com.cleancodesample.data.storage.model.Photo photo : storagePhotos) {
            values[index++] = PhotosContract.PhotoTable.getPhotoContentValues(photo);
        }
        mContext.getContentResolver().bulkInsert(uri, values);
    }

    @Override
    public void refreshPhotos() { }
}
