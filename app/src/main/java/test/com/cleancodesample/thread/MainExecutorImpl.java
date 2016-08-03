package test.com.cleancodesample.thread;

import android.os.Handler;
import android.os.Looper;

import test.com.cleancodesample.domain.executor.MainExecutor;

/**
 * The implementation of the UI executor using Android Handler
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
