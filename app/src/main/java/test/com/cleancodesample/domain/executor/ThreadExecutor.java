package test.com.cleancodesample.domain.executor;

import test.com.cleancodesample.domain.interactor.base.AbstractInteractor;

/**
 * Created by hzaied on 7/16/16.
 */
public interface ThreadExecutor {
    void execute(final AbstractInteractor interactor);
}
