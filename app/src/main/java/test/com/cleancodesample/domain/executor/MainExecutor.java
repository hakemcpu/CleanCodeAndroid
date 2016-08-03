package test.com.cleancodesample.domain.executor;

/**
 * This interface will define a class that will enable interactors to run certain operations on the main (UI) thread.
 * For example,
 * if an interactor needs to show an object to the UI this can be used to make sure the show method is called on the UI
 * thread.
 * <p/>
 */
public interface MainExecutor {

    /**
     * Run runnable operations in the UI thread.
     *
     * @param runnable
     */
    void post(final Runnable runnable);
}
