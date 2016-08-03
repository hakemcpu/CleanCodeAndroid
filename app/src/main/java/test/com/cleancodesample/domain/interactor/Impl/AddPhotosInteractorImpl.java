package test.com.cleancodesample.domain.interactor.Impl;

import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.AddPhotosInteractor;
import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * This is an implementation for an interactor that Adds Photos through the repository injected.
 * In this class we inject the objects using Dagger 2.
 */
public class AddPhotosInteractorImpl extends AbstractInteractor implements AddPhotosInteractor {
    public PhotoRepository mPhotoRepository;
    private AddPhotosInteractor.CallBack mCallBack;
    private List<Photo> mPhotoList;

    @Inject public AddPhotosInteractorImpl(MainExecutor mainExecutor,
                                           ThreadExecutor threadExecutor,
                                           PhotoRepository photoRepository) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
    public void setPhotoList(List<Photo> photoList) {
        mPhotoList = photoList;
    }

    @Override
    public void run(RequestValues requestValues) {
        mPhotoRepository.addPhotos(mPhotoList);
        mMainExecutor.post(new Runnable() {
            @Override
            public void run() {
                mCallBack.onPhotosAdded();
            }
        });
    }
}
