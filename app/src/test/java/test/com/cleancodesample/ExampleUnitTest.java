package test.com.cleancodesample;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.interactor.GetPhotosInteractor;
import test.com.cleancodesample.domain.interactor.Impl.GetPhotosInteractorImpl;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private MainExecutor mMainExecutor;
    @Mock ThreadExecutor mThreadExecutor;
    @Mock PhotoRepository mPhotoRepository;
    @Mock GetPhotosInteractor.CallBack mCallBack;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mMainExecutor = new TestMainThread();
    }

    @Test
    public void testAddPhotosInteractor() {
        List<Photo> photos = new ArrayList<>();
        photos.add(new Photo("hakem", "http://hakem"));
        photos.add(new Photo("zaied", "http://zaied"));

        Mockito.when(mPhotoRepository.getPhotos()).thenReturn(photos);

        GetPhotosInteractorImpl getPhotosInteractor = new GetPhotosInteractorImpl(mMainExecutor,
                mThreadExecutor, mPhotoRepository, mCallBack);
        getPhotosInteractor.run();

        Mockito.verify(mPhotoRepository).getPhotos();
        Mockito.verifyNoMoreInteractions(mPhotoRepository);
        Mockito.verify(mCallBack).onPhotosRetrieved(photos);
    }
}