package test.com.cleancodesample.dagger.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import test.com.cleancodesample.dagger.module.ApplicationModule;
import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;
import test.com.cleancodesample.domain.repository.PhotoRepository;
import test.com.cleancodesample.presentation.ui.fragment.MainFragment;

/**
 * This is the Main Component in the application. It injects all the singleton objects in the
 * application.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    /**
     * This is the injection function. It provides the injector object to the interface.
     * Note: The inject function should take the Instance that contains the @Inject annotation in it
     * So we will not be able to pass a generic type to this function unless the generic type
     * contains @Inject annotation.
     * TODO: We need to create a base fragment, or we can have multiple inject functions for each fragment.
     */
    void inject(MainFragment fragment);

    /*
     * All the below function declarations are a MUST have to expose the provided functions to the
     * sub-graphs like PhotoComponent. If we use @Provides in the ApplicationModule but we don't
     * expose it here it will not be injected.
     * NOTE: if we are only using the ApplicationComponent the below functions will not be needed.
     */
    Context context();

    ThreadExecutor threadExecutor();
    MainExecutor mainExecutor();

    PhotoRepository photoRepository();
}
