package test.com.cleancodesample.domain.interactor;

import java.util.List;

import test.com.cleancodesample.domain.interactor.base.Interactor;
import test.com.cleancodesample.domain.model.Photo;

public interface GetPhotosInteractor extends Interactor {

    interface CallBack {
        void onPhotosRetrieved(List<Photo> photos);
    }
}
