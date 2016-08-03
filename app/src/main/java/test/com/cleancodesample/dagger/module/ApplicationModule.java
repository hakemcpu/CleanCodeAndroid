package test.com.cleancodesample.dagger.module;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test.com.cleancodesample.AndroidApplication;
import test.com.cleancodesample.data.PhotoRepositoryImpl;
import test.com.cleancodesample.data.network.RemotePhotoRepositoryImpl;
import test.com.cleancodesample.data.storage.LocalPhotoRepositoryImpl;
import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.executor.impl.ThreadExecutorImpl;
import test.com.cleancodesample.domain.repository.PhotoRepository;
import test.com.cleancodesample.thread.MainExecutorImpl;

/**
 * This is the Main Module in the application. It provides all the singleton objects in the
 * application.
 */
@Module
public class ApplicationModule {
    private final AndroidApplication mAndroidApplication;

    public ApplicationModule(AndroidApplication androidApplication) {
        mAndroidApplication = androidApplication;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return mAndroidApplication;
    }

    @Provides @Singleton ThreadExecutor provideThreadExecutor() {
        return ThreadExecutorImpl.getInstance();
    }

    @Provides @Singleton MainExecutor provideMainExecutor() {
        return MainExecutorImpl.getInstance();
    }

    @Provides @Singleton PhotoRepository providePhotoRepositoryImpl(@Named("local") PhotoRepository localPhotoRepository,
                                                                    @Named("remote") PhotoRepository remotePhotoRepository) {
        return new PhotoRepositoryImpl(localPhotoRepository, remotePhotoRepository);
    }

    /*
    We will not expose the two functions below in the ApplicationComponent because they are only
    used in to provide Repositories to this Module.
     */
    @Provides @Singleton @Named("local") PhotoRepository provideLocalPhotoRepository() {
        return new LocalPhotoRepositoryImpl(mAndroidApplication);
    }

    @Provides @Singleton @Named("remote") PhotoRepository provideRemotePhotoRepository() {
        return new RemotePhotoRepositoryImpl();
    }
}
