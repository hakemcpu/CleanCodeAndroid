package test.com.cleancodesample.data.network.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolImageLoader<T> {
    private final static int CACHE_SIZE = 50*1024*1024;

    private final static int CORE_POOL_SIZE = 3;
    private final static int MAX_POOL_SIZE = 5;
    private final static int KEEP_ALIVE_TIME = 120;
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private final static BlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<>();


    private ThreadPoolExecutor mThreadPoolExecutor;
    private Handler mHandler;
    private LruCache<String, Bitmap> mCache;
    private OnDownloadListener<T> mCallback;


    public ThreadPoolImageLoader(OnDownloadListener<T> callback) {
        mHandler = new Handler(Looper.getMainLooper());
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TIME_UNIT,
                BLOCKING_QUEUE);
        mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        mCallback = callback;
    }

    public void queueImage(final T target, final String url) {
        if(mCache.get(url) != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onImageDownloaded(target, url, mCache.get(url));
                }
            });
        } else {
            mThreadPoolExecutor.submit(new ImageDownload<>(url, target, mHandler, mCallback, mCache));
        }
    }

    public void clearQueue() {
        mThreadPoolExecutor.shutdownNow();
    }

    public interface OnDownloadListener<T> {
        void onImageDownloaded(T target, String url, Bitmap bitmap);
    }

    static class ImageDownload<T> implements Runnable {
        private WeakReference<T> mTargetWeekReference;
        private WeakReference<String> mUrl;
        private WeakReference<Handler> mHandler;
        private WeakReference<OnDownloadListener<T>> mCallback;
        private WeakReference<LruCache<String, Bitmap>> mCache;

        public ImageDownload(String url, T target, Handler handler,
                             OnDownloadListener<T> callback, LruCache<String, Bitmap> cache) {
            mUrl = new WeakReference<>(url);
            mTargetWeekReference = new WeakReference<T>(target);
            mHandler = new WeakReference<>(handler);
            mCallback = new WeakReference<>(callback);
            mCache = new WeakReference<>(cache);
        }

        @Override
        public void run() {
            try {
                final String url = mUrl.get();
                if(url == null) return;

                byte[] imageBytes = download(url);
                if(imageBytes != null) {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    final Handler handler = mHandler.get();
                    if (handler == null) return;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            T target = mTargetWeekReference.get();
                            if(target != null) {
                                final OnDownloadListener<T> callback = mCallback.get();
                                if (callback == null) return;
                                callback.onImageDownloaded(target, url, bitmap);

                                LruCache<String, Bitmap> cache = mCache.get();
                                if (cache == null) return;
                                cache.put(url, bitmap);
                            }
                        }
                    });
                }
            } catch (IOException e) {}
        }

        public byte[] download(String url) throws IOException {
            Log.e("test", "downloading :"+url);
            URL request = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();

            try{
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();

                byte[] arr = new byte[4*1024];
                int n;
                while((n = in.read(arr))> 0) {
                    out.write(arr, 0, n);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }

    }
}
