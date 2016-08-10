package test.com.cleancodesample.presentation.presenter.impl;

import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.data.PhotoRepositoryImpl;
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
 * The main presenter of the system. It contains the interactors in the system since we only have
 * one fragment in the activity. This is the MVP pattern.
 */
public class MainPresenterImpl extends AbstractPresenter implements MainPresenter, AddPhotosInteractor.CallBack,
        GetPhotosInteractor.CallBack {
    private MainPresenter.View mView;
    private PhotoRepositoryImpl mPhotoRepository;
    private AddPhotosInteractorImpl mAddPhotosInteractor;
    private GetPhotosInteractorImpl mGetPhotosInteractor;

    @Inject public MainPresenterImpl(MainExecutor mainExecutor,
                                     ThreadExecutor threadExecutor,
                                     PhotoRepositoryImpl photoRepository,
                                     AddPhotosInteractorImpl addPhotosInteractor,
                                     GetPhotosInteractorImpl getPhotosInteractor) {
        super(mainExecutor, threadExecutor);
        mPhotoRepository = photoRepository;
        mAddPhotosInteractor = addPhotosInteractor;
        mGetPhotosInteractor = getPhotosInteractor;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public void addPhotos(List<Photo> photos) {
        mAddPhotosInteractor.setCallBack(this);
        mAddPhotosInteractor.setPhotoList(photos);
        mAddPhotosInteractor.execute();
    }

    @Override
    public void getPhotos() {
        mGetPhotosInteractor.setCallBack(this);
        mGetPhotosInteractor.execute();
    }

    @Override
    public void getNetworkPhotos() {
        mGetPhotosInteractor.setRequestValues(new GetPhotosInteractorImpl.RequestValues(true));
        mGetPhotosInteractor.setCallBack(this);
        mGetPhotosInteractor.execute();
    }

    // Interactor callbacks
    @Override
    public void onPhotosAdded() {
        mView.onPhotosAdded();
    }
    @Override
    public void onPhotosRetrieved(List<Photo> photos) {
        mView.onPhotosRetrieved(photos);
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
