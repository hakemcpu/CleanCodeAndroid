package test.com.cleancodesample.data;


import test.com.cleancodesample.data.network.RemotePhotoRepositoryImpl;
import test.com.cleancodesample.data.storage.LocalPhotoRepositoryImpl;

/**
 * Created by hzaied on 7/18/16.
 */
public class PhotoRepositoryWrapper {
    private LocalPhotoRepositoryImpl mLocalRepository;
    private RemotePhotoRepositoryImpl mRemotePhotRepository;

    public PhotoRepositoryWrapper(LocalPhotoRepositoryImpl localRepository,
                                  RemotePhotoRepositoryImpl remotePhotoRepository) {
        mLocalRepository = localRepository;
        mRemotePhotRepository = remotePhotoRepository;
    }

    public LocalPhotoRepositoryImpl getLocalRepository() {
        return mLocalRepository;
    }

    public RemotePhotoRepositoryImpl getRemotePhotRepository() {
        return mRemotePhotRepository;
    }
}
