package test.com.cleancodesample.data.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.data.storage.converter.StorageModelConverter;
import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * Created by hzaied on 7/16/16.
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
        if (cursor.getCount() == 0)
            return null;
        else
            return new ArrayList<>();
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
