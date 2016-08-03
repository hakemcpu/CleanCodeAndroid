package test.com.cleancodesample.domain.repository;

import java.util.List;

import test.com.cleancodesample.domain.model.Photo;

/**
 * A simple interface the defines the main operations for the photos.
 */
public interface PhotoRepository {
    // When we have content provider this function will not be used as the content provider directly
    // accesses the data model.
    List<Photo> getPhotos();
    void addPhotos(List<Photo> photos);
    void refreshPhotos();
}
