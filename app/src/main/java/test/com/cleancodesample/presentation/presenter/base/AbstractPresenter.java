package test.com.cleancodesample.presentation.presenter.base;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;

/**
 * Created by hzaied on 7/16/16.
 */
public abstract class AbstractPresenter {
    protected MainExecutor mMainExecutor;
    protected ThreadExecutor mThreadExecutor;

    public AbstractPresenter(MainExecutor mainExecutor, ThreadExecutor threadExecutor) {
        mThreadExecutor = threadExecutor;
        mMainExecutor = mainExecutor;
    }
}
