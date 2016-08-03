package test.com.cleancodesample.domain.interactor.base;

import test.com.cleancodesample.domain.executor.MainExecutor;
import test.com.cleancodesample.domain.executor.ThreadExecutor;

/**
 * This abstract class implements some common methods for all interactors.
 * TODO: We need to add cancelling mechanism.
 */
public abstract class AbstractInteractor<T extends AbstractInteractor.RequestValues> implements Interactor {
    protected MainExecutor mMainExecutor;
    protected ThreadExecutor mThreadExecutor;
    protected RequestValues mRequestValues;

    public AbstractInteractor(MainExecutor mainExecutor, ThreadExecutor threadExecutor) {
        mMainExecutor = mainExecutor;
        mThreadExecutor = threadExecutor;
    }

    public void setRequestValues(RequestValues requestValues) {
        mRequestValues = requestValues;
    }

    /**
     * This function should never be called directly.
     * @param requestValues
     */
    public abstract void run(T requestValues);

    public void onFinished() {

    }

    @Override
    public void execute() {
        mThreadExecutor.execute(this, mRequestValues);
    }

    /**
     * An interface to define the parameters used with the request.
     */
    public interface RequestValues {

    }
}
