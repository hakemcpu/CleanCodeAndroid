package test.com.cleancodesample.domain.executor;

import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;

/**
 * This executor is responsible for running interactors on background threads.
 * <p/>
 */
public interface ThreadExecutor {
    /**
     * This method should call the interactor's run method and thus start the interactor. This should be called
     * on a background thread as interactors might do lengthy operations.
     *
     * @param interactor The interactor to run.
     * @param requestValues Passed parameters to be used while execution of the interactor.
     */
    void execute(final AbstractInteractor interactor, final AbstractInteractor.RequestValues requestValues);
}
