package test.com.cleancodesample.domain.interactor;

import test.com.cleancodesample.domain.interactor.base.Interactor;

/**
 * Created by hzaied on 7/16/16.
 */
public interface AddPhotosInteractor extends Interactor {

    interface CallBack {
        void onPhotosAdded();
    }
}
