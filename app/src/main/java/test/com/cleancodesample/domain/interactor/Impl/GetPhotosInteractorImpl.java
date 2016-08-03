package test.com.cleancodesample.domain.interactor.Impl;

import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.GetPhotosInteractor;
import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * This is an implementation for an interactor that Gets Photos from different source using the repository
 * injected.
 * In this class we inject the objects using Dagger 2.
 */
public class GetPhotosInteractorImpl extends AbstractInteractor<GetPhotosInteractorImpl.RequestValues> implements GetPhotosInteractor {
    private PhotoRepository mPhotoRepository;
    private GetPhotosInteractor.CallBack mCallBack;

    @Inject public GetPhotosInteractorImpl(MainExecutor mainExecutor,
                                           ThreadExecutor threadExecutor,
                                           PhotoRepository photoRepository) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void run(RequestValues requestValues) {
        // Incase the requested values needs to force update the content. We ask the repository
        // to refresh it's content.
        // This is used to be able to call the Remote repository instead of getting the local data.
        if (requestValues != null && requestValues.isForceUpdate())
            mPhotoRepository.refreshPhotos();

        final List<Photo> photos = mPhotoRepository.getPhotos();
        mMainExecutor.post(new Runnable() {
            @Override
            public void run() {
                mCallBack.onPhotosRetrieved(photos);
            }
        });
    }

    /**
     * The definition of RequestValues to be able to ask the repository to force refresh of the
     * content.
     */
    public static final class RequestValues implements AbstractInteractor.RequestValues {
        private boolean mForceUpdate;

        public RequestValues(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }
    }
}
