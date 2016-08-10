package test.com.cleancodesample.presentation.presenter;

import java.util.List;

import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.presentation.presenter.base.Presenter;

/**
 * An interface that defines the actions and the callback that should be used by the presenter
 */
public interface MainPresenter extends Presenter {
    interface View {
        void onPhotosAdded();
        void onPhotosRetrieved(List<Photo> photos);
    }

    void addPhotos(List<Photo> photos);
    void getNetworkPhotos();
    void getPhotos(); // Content provider handles the retrieval.
}
