package test.com.cleancodesample.data.network.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ImageLoader<T> extends HandlerThread {
    private final static int MSG_DOWNLOAD = 10;
    private final static int CACHE_SIZE = 50*1024*1024;

    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ConcurrentMap<T, String> mConcurrentMap = new ConcurrentHashMap<>();
    private OnDownloadListener<T> mListener;
    private LruCache<String, Bitmap> mCache;

    public ImageLoader(Handler response) {
        super("ImageLoader");
        mResponseHandler = response;
        mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void setListener(OnDownloadListener<T> listener) {
        mListener = listener;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(message.what == MSG_DOWNLOAD) {
                    T target = (T) message.obj;
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target) {
        final String url = mConcurrentMap.get(target);
        if (url == null) return;
        try {
            if(mCache.get(url) != null) {
                final Bitmap bitmap = mCache.get(url);
                mResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConcurrentMap.remove(target);
                        if(mListener != null) {
                            mListener.onImageDownloaded(target, url, bitmap);
                        }
                    }
                });
            } else {
                byte[] image = download(url);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                if(bitmap == null) return;
                mCache.put(url, bitmap);
                mResponseHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConcurrentMap.remove(target);
                        if (mListener != null) {
                            mListener.onImageDownloaded(target, url, bitmap);
                        }
                    }
                });
            }
        } catch (IOException e) {}
    }

    public void queueImage(T view, String url) {
        if(url == null) {
            mConcurrentMap.remove(view);
        } else {
            mConcurrentMap.put(view, url);
            mRequestHandler.obtainMessage(MSG_DOWNLOAD, view).sendToTarget();
        }
    }


    public byte[] download(String url) throws IOException {
        Log.e("test", "downloading :"+url);
        URL request = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) request.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            byte[] arr = new byte[3*1024];
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

    public void clearQueue() {
        mRequestHandler.removeMessages(MSG_DOWNLOAD);
        mConcurrentMap.clear();
    }

    public interface OnDownloadListener<T> {
        void onImageDownloaded(T target, String url, Bitmap bitmap);
    }
}
