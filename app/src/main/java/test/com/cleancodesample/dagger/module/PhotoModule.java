package test.com.cleancodesample.dagger.module;


import dagger.Module;

/**
 * This Module is not used. It is here just to show the functionality of the Dagger framework,
 */
@Module
public class PhotoModule {
    /**
     In this module we don't need any @Provides since all the parameters needed to create
     the interactors are already provided by the ApplicationComponent.
     But if at any point of time the interactors needed to take an extra parameter that is not
     provided by the ApplicationComponent. Then we will need to implement some logic to provide
     the injection of the new parameter, an example is listed below:

     // Providing the interactor when it required to have an extra integer value.
     @Provides AddPhotosInteractorImpl providesAddPhotosInteractor(MainExecutor mainExecutor,
                                                                   ThreadExecutor threadExecutor,
                                                                   PhotoRepository photoRepository,
                                                                   int x) {
         return new AddPhotosInteractorImpl(mainExecutor, threadExecutor, photoRepository, x);
     }

     // Providing the integer value that the above provided function needed to create the interactor
     @Provides int provideX() {
         return 0;
     }
     */
}
