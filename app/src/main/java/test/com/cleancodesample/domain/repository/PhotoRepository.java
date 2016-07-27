package test.com.cleancodesample.domain.repository;

import java.util.List;

import test.com.cleancodesample.domain.model.Photo;

/**
 * Created by hzaied on 7/16/16.
 */
public interface PhotoRepository {
    // When we have content provider this function will not be used as the content provider directly
    // accesses the data model.
    List<Photo> getPhotos();
    void addPhotos(List<Photo> photos);
}
