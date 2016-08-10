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
        // If the request is not dirty then we get the data from the local database
        // In case of not having data in the local database, we will retrieve the data from the
        // remote repository and save it to the databse.
        if(!mIsDirty) {
            List<Photo> photoList = mLocalPhotoRepository.getPhotos();
            if (photoList != null)
                return photoList;
        }

        mIsDirty = false;
        // Retrieve from the network and the add the photos.
        List<Photo> photos = mRemotePhotoRepository.getPhotos();
        mLocalPhotoRepository.addPhotos(photos);
        return photos;
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
