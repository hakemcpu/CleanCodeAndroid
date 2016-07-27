package test.com.cleancodesample.presentation.presenter;

import java.util.List;

import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.presentation.presenter.base.Presenter;

/**
 * Created by hzaied on 7/16/16.
 */
public interface MainPresenter extends Presenter {
    interface View {
        void onPhotosAdded();
        void onPhotosRetrieved();
    }

    void addPhotos(List<Photo> photos);
    void getNetworkPhotos();
    void getPhotos(); // Content provider handles the retrieval.
}
