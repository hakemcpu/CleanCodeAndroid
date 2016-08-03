# CleanCodeAndroid
This is a simple application that uses the clean code architecture.

Clean architecture
-----------------
![http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/](http://fernandocejas.com/wp-content/uploads/2014/09/clean_architecture1.png)

Architectural approach
-----------------
![http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/](http://fernandocejas.com/wp-content/uploads/2014/09/clean_architecture_android.png)

Block Diagram
-----------------
```java
                    Domain Layer               				   Data Layer
              ———————————————————————               —————————————————————————————
       ——>  |  ———  Interactor        |       ———> |	   Network	      	     |
      |	    | |			              |      |     |	    - Rest Apis	         |
      |	    |  ———> Executor          |      |     |                             |
      |	    |	    Repository   ———— | ——————————>|	   Storage  	   	     |
      |	    |	    Model             |            |	    - Content Providers  |
      |	    |	                      |            |	    - Database	         |
      |	      ———————————————————————               —————————————————————————————
      |
      | (Presenters calling interactors)
      |
      |
      |	          Presentation Layer
      |	     ————————————————————————————
      |	    |     UI   ————————————————— | ——
      |	    |       - Activities	     |   |
      |	    |       - Fragments	         |   | (UI calling the presenters)
      |	    |       - Adapters           |   |
      |	    |       - Views              |   |
       ————	|———— Presenters <———————————|———
             ————————————————————————————
```

TODO
-----------------
Need to add more clarification and code commenting to the code.
