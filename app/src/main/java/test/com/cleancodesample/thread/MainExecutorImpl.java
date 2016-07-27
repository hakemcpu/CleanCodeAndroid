package test.com.cleancodesample.thread;

import android.os.Handler;
import android.os.Looper;

import test.com.cleancodesample.domain.executor.MainExecutor;

/**
 * Created by hzaied on 7/16/16.
 */
public class MainExecutorImpl implements MainExecutor {
    private static MainExecutorImpl sMainExecutor = new MainExecutorImpl();

    private Handler mHandler;

    public static MainExecutor getInstance() {
        return sMainExecutor;
    }

    public MainExecutorImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}
