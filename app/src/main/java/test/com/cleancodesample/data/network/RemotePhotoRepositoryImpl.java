package test.com.cleancodesample.data.network;

import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.data.network.flickr.FlickrHandler;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.domain.repository.PhotoRepository;

/**
 * Created by hzaied on 7/18/16.
 */
public class RemotePhotoRepositoryImpl implements PhotoRepository {

    @Inject public RemotePhotoRepositoryImpl() { }

    @Override
    public List<Photo> getPhotos() {
        List<Photo> photos = new FlickrHandler().fetchItems();
        return photos;
    }

    @Override
    public void addPhotos(List<Photo> photos) {}

    @Override
    public void refreshPhotos() {}
}
