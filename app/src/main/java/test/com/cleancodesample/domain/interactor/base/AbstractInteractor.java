package test.com.cleancodesample.domain.interactor.base;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;

/**
 * Created by hzaied on 7/16/16.
 */
public abstract class AbstractInteractor implements Interactor {
    protected MainExecutor mMainExecutor;
    protected  ThreadExecutor mThreadExecutor;

    public AbstractInteractor(MainExecutor mainExecutor, ThreadExecutor threadExecutor) {
        mMainExecutor = mainExecutor;
        mThreadExecutor = threadExecutor;
    }

    public abstract void run();

    public void onFinished() {

    }

    @Override
    public void execute() {
        mThreadExecutor.execute(this);
    }
}
