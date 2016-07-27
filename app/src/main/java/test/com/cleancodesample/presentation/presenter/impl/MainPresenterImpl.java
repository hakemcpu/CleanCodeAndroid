package test.com.cleancodesample.presentation.presenter.impl;

import java.util.List;

import test.com.cleancodesample.data.PhotoRepositoryWrapper;
import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.AddPhotosInteractor;
import test.com.cleancodesample.domain.interactor.GetPhotosInteractor;
import test.com.cleancodesample.domain.interactor.Impl.AddPhotosInteractorImpl;
import test.com.cleancodesample.domain.interactor.Impl.GetPhotosInteractorImpl;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.presentation.presenter.MainPresenter;
import test.com.cleancodesample.presentation.presenter.base.AbstractPresenter;

/**
 * Created by hzaied on 7/16/16.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter, AddPhotosInteractor.CallBack,
        GetPhotosInteractor.CallBack {
    private PhotoRepositoryWrapper mPhotoRepository;
    private MainPresenter.View mView;

    public MainPresenterImpl(MainExecutor mainExecutor, ThreadExecutor threadExecutor,
                             PhotoRepositoryWrapper photoRepository, View view) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
        mView = view;
    }

    @Override
    public void addPhotos(List<Photo> photos) {
        AddPhotosInteractorImpl addPhotosInteractor = new AddPhotosInteractorImpl(mMainExecutor,
                mThreadExecutor, mPhotoRepository.getLocalRepository(), this, photos);
        addPhotosInteractor.execute();
    }

    @Override
    public void getPhotos() {
        GetPhotosInteractorImpl getPhotosInteractor = new GetPhotosInteractorImpl(mMainExecutor,
                mThreadExecutor, mPhotoRepository.getLocalRepository(), this);
        getPhotosInteractor.execute();
    }

    @Override
    public void getNetworkPhotos() {
        GetPhotosInteractorImpl getPhotosInteractor = new GetPhotosInteractorImpl(mMainExecutor,
                mThreadExecutor, mPhotoRepository.getRemotePhotRepository(), new GetPhotosInteractor.CallBack() {
            @Override
            public void onPhotosRetrieved(List<Photo> photos) {
                AddPhotosInteractorImpl addPhotosInteractor = new AddPhotosInteractorImpl(mMainExecutor,
                        mThreadExecutor, mPhotoRepository.getLocalRepository(), MainPresenterImpl.this, photos);
                addPhotosInteractor.execute();
            }
        });
        getPhotosInteractor.execute();
    }

    // Interactor callbacks
    @Override
    public void onPhotosAdded() {
        mView.onPhotosAdded();
    }
    @Override
    public void onPhotosRetrieved(List<Photo> photos) {
        mView.onPhotosRetrieved();
    }

    // Presenters interfaces.
    @Override
    public void resume() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void stop() {

    }
    @Override
    public void destory() {

    }
}
