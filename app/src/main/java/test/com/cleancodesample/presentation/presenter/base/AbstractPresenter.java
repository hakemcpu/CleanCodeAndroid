package test.com.cleancodesample.presentation.presenter.base;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;

/**
 * This is an abstract presenter that defines the main building block to have a presenter
 * implemented.
 * In our case we only need to keep track of Executors to be used by the interactors later on.
 */
public abstract class AbstractPresenter {
    protected MainExecutor mMainExecutor;
    protected ThreadExecutor mThreadExecutor;

    public AbstractPresenter(MainExecutor mainExecutor, ThreadExecutor threadExecutor) {
        mThreadExecutor = threadExecutor;
        mMainExecutor = mainExecutor;
    }
}
