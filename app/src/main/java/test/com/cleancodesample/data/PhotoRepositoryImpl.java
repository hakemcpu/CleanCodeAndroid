package test.com.cleancodesample.data;


import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.data.network.RemotePhotoRepositoryImpl;
import test.com.cleancodesample.data.storage.LocalPhotoRepositoryImpl;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 *
 */
public class PhotoRepositoryImpl implements PhotoRepository {
    private PhotoRepository mLocalPhotoRepository;
    private PhotoRepository mRemotePhotoRepository;

    private boolean mIsDirty;

    @Inject public PhotoRepositoryImpl(PhotoRepository localPhotoRepository,
                                       PhotoRepository remotePhotoRepository) {
        mLocalPhotoRepository = localPhotoRepository;
        mRemotePhotoRepository = remotePhotoRepository;
    }

    @Override
    public List<Photo> getPhotos() {
        if(mIsDirty) {
            mIsDirty = false;
            return mRemotePhotoRepository.getPhotos();
        } else {
            // TODO: This part is not working as request - updating in the first run-
            // because of the cursor loader. Where we need to add the photos to the database
            // after retrieving them at the Presenter level. This is not done at the moment.
            List<Photo> photoList = mLocalPhotoRepository.getPhotos();
            if (photoList != null)
                return photoList;
            else return mRemotePhotoRepository.getPhotos();
        }
    }

    @Override
    public void addPhotos(List<Photo> photos) {
        mLocalPhotoRepository.addPhotos(photos);
    }

    @Override
    public void refreshPhotos() {
        mIsDirty = true;
    }
}
