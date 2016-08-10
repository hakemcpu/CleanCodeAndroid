package test.com.cleancodesample.data.storage.converter;

import java.util.ArrayList;
import java.util.List;

import test.com.cleancodesample.data.storage.model.Photo;

/**
 * Created by hzaied on 7/16/16.
 */
public class StorageModelConverter {
    public static Photo convertToStorageModel(test.com.cleancodesample.domain.model.Photo photo) {
        Photo storagePhoto = new Photo(photo.getTitle(), photo.getUrl());
        return storagePhoto;
    }

    public static test.com.cleancodesample.domain.model.Photo convertToDomainModel(Photo photo) {
        test.com.cleancodesample.domain.model.Photo domainPhoto = new
                test.com.cleancodesample.domain.model.Photo(photo.getTitle(), photo.getUrl());
        return domainPhoto;
    }

    public static List<Photo> convertToStorageModel(List<test.com.cleancodesample.domain.model.Photo> photos) {
        List<Photo> modelPhotos = new ArrayList<>();
        for(test.com.cleancodesample.domain.model.Photo photo : photos) {
            modelPhotos.add(convertToStorageModel(photo));
        }

        // TODO: NEED TO CHECK THE BELOW CODE
//        photos.clear();
//        photos = null;

        return modelPhotos;
    }

    public static List<test.com.cleancodesample.domain.model.Photo> convertToDomainModel(List<Photo> photos) {
        List<test.com.cleancodesample.domain.model.Photo> domainPhotos = new ArrayList<>();
        for(Photo photo : photos) {
            domainPhotos.add(convertToDomainModel(photo));
        }

        // TODO: NEED TO CHECK THE BELOW CODE
//        photos.clear();
//        photos = null;

        return domainPhotos;
    }
}
