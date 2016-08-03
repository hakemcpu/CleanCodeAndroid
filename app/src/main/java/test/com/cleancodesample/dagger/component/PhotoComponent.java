package test.com.cleancodesample.dagger.component;

import dagger.Component;
import test.com.cleancodesample.dagger.PerFragment;
import test.com.cleancodesample.dagger.module.PhotoModule;
import test.com.cleancodesample.presentation.ui.fragment.MainFragment;

/**
 * This Component is not used. It is here just to show the functionality of the Dagger framework,
 * where we can depend on other components.
 *
 * The PerActivity is a must here as the ApplicationComponent is a Singleton and we need to define
 * the scope of this component.
 */
@PerFragment
@Component(dependencies = {ApplicationComponent.class}, modules = {PhotoModule.class})
public interface PhotoComponent {
    void inject(MainFragment fragment);

    /**
     Currently we don't need to have any function declaration in this interface as it is not used.
     But if at any point of time the interactors needed to take an extra parameter that is not
     provided by the ApplicationComponent.
     Then we will need to expose the following function:

     AddPhotosInteractorImpl addPhotosInteractor();

     You also need to check the PhotoModule for the implementation of the provider.
     Note: The above function is only added if @Provides for the above interactor is implemented
     in the PhotoModule. And in this case the above function is a must have or the interactor will
     not be exposed to the injected objects.

     "Subcomponents have access to entire objects graph from their parents while Component
     dependency gives access only to those which are exposed in Component interface."
    */
}
