package test.com.cleancodesample;

import android.app.Application;

import test.com.cleancodesample.dagger.component.ApplicationComponent;
import test.com.cleancodesample.dagger.component.DaggerApplicationComponent;
import test.com.cleancodesample.dagger.module.ApplicationModule;

public class AndroidApplication extends Application {
    /*
        Block Diagram
        -----------------
                             Domain               				           Data
                  ———————————————————————               —————————————————————————————
           ———>	|  ———  Interactor        |       ———> |	   Network	      	     |
          |	    | |			              |      |     |	    - Rest Apis	         |
          |	    |  ———> Executor          |      |     |                             |
          |	    |	    Repository   ———— | ————————>  |	   Storage  	   	     |
          |	    |	    Model             |            |	    - Content Providers  |
          |	    |			              |            |	    - Database	         |
          |	      ———————————————————————               —————————————————————————————
          |
          | (Presenters calling interactors)
          |
          |
          |	           Presentation
          |	     ———————————————————————
          |	    |     UI   ———————————— | ——
          |	    |       - Activities	|   |
          |	    |       - Fragments	    |   | (UI calling the presenters)
          |	    |       - Adapters	    |   |
          |	    |       - Views		    |   |
           ————	|———— Presenters <————— |———
                 ———————————————————————
     */

    // The main and Singleton Dagger component.
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
