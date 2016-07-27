package test.com.cleancodesample.domain.interactor.Impl;

import java.util.List;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.GetPhotosInteractor;
import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * Created by hzaied on 7/16/16.
 */
public class GetPhotosInteractorImpl extends AbstractInteractor implements GetPhotosInteractor {
    private PhotoRepository mPhotoRepository;
    private GetPhotosInteractor.CallBack mCallBack;

    public GetPhotosInteractorImpl(MainExecutor mainExecutor, ThreadExecutor threadExecutor,
                                   PhotoRepository photoRepository, CallBack callBack) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
        mCallBack = callBack;
    }

    @Override
    public void run() {
        final List<Photo> photos = mPhotoRepository.getPhotos();
        mMainExecutor.post(new Runnable() {
            @Override
            public void run() {
                mCallBack.onPhotosRetrieved(photos);
            }
        });
    }
}
