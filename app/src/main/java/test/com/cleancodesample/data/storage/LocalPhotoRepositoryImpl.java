package test.com.cleancodesample.data.storage;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import test.com.cleancodesample.data.storage.converter.StorageModelConverter;
import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * Created by hzaied on 7/16/16.
 */
public class LocalPhotoRepositoryImpl implements PhotoRepository {

    private Context mContext;

    public LocalPhotoRepositoryImpl(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public List<Photo> getPhotos() {
        return null;
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
}
