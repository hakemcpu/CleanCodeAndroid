package test.com.cleancodesample.domain.interactor.Impl;

import java.util.List;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.AddPhotosInteractor;
import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * Created by hzaied on 7/16/16.
 */
public class AddPhotosInteractorImpl extends AbstractInteractor implements AddPhotosInteractor {
    public PhotoRepository mPhotoRepository;
    private AddPhotosInteractor.CallBack mCallBack;
    private List<Photo> mPhotoList;

    public AddPhotosInteractorImpl(MainExecutor mainExecutor, ThreadExecutor threadExecutor,
                                   PhotoRepository photoRepository, CallBack callBack, List<Photo> photoList) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
        mCallBack = callBack;
        mPhotoList = photoList;
    }

    @Override
    public void run() {
        mPhotoRepository.addPhotos(mPhotoList);
        mMainExecutor.post(new Runnable() {
            @Override
            public void run() {
                mCallBack.onPhotosAdded();
            }
        });
    }
}
