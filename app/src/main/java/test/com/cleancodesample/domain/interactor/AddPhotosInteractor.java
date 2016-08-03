package test.com.cleancodesample.domain.interactor;

import test.com.cleancodesample.domain.interactor.base.Interactor;


public interface AddPhotosInteractor extends Interactor {

    interface CallBack {
        void onPhotosAdded();
    }
}
