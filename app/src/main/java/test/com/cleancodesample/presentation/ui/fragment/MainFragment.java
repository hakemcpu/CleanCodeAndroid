package test.com.cleancodesample.presentation.ui.fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import test.com.cleancodesample.AndroidApplication;
import test.com.cleancodesample.R;
import test.com.cleancodesample.dagger.component.ApplicationComponent;
import test.com.cleancodesample.dagger.component.DaggerApplicationComponent;
import test.com.cleancodesample.dagger.component.DaggerPhotoComponent;
import test.com.cleancodesample.dagger.component.PhotoComponent;
import test.com.cleancodesample.data.PhotoRepositoryImpl;
import test.com.cleancodesample.data.network.RemotePhotoRepositoryImpl;
import test.com.cleancodesample.data.network.imageloader.ThreadPoolImageLoader;
import test.com.cleancodesample.data.storage.LocalPhotoRepositoryImpl;
import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.domain.executor.impl.ThreadExecutorImpl;
import test.com.cleancodesample.presentation.presenter.MainPresenter;
import test.com.cleancodesample.presentation.presenter.impl.MainPresenterImpl;
import test.com.cleancodesample.presentation.ui.customviews.CursorRecyclerViewAdapter;
import test.com.cleancodesample.thread.MainExecutorImpl;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,MainPresenter.View {
    private final static int QUERY_LOADER_ID = 10;

    // Views
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ThreadPoolImageLoader<ViewHolder> mImageLoader;

    // This has to be public for Dagger to be able to inject it.
    public @Inject MainPresenterImpl mMainPresenter;

    private PhotoComponent mPhotoComponent;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = new ThreadPoolImageLoader<>(new ThreadPoolImageLoader.OnDownloadListener<ViewHolder>() {
            @Override
            public void onImageDownloaded(ViewHolder target, String url, Bitmap bitmap) {
                Log.e("test", "Image Downloaded");
                if(getActivity() != null) {
                    if (url.equals(target.mImageView.getTag())) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        target.bindDrawable(drawable);
                    }
                }
            }
        });


        setRetainInstance(true);

        // Initialize the dagger injector.
        ApplicationComponent applicationComponent =
                ((AndroidApplication)getActivity().getApplication()).getApplicationComponent();
        // In this application we can either inject using the PhotoComponent or the ApplicationComponent
        // applicationComponent.inject(this);
        mPhotoComponent = DaggerPhotoComponent.builder().applicationComponent(applicationComponent).build();
        mPhotoComponent.inject(this);

        // Set the callback of the view.
        mMainPresenter.setView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageLoader.clearQueue();
        mPhotoComponent = null; // Clean the component since it's scope is @PerFragment
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ImageAdapter(null, mImageLoader);
        mRecyclerView.setAdapter(mAdapter);

        Button button = (Button) view.findViewById(R.id.update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        return view;
    }

    private void updateData() {
        mMainPresenter.getNetworkPhotos();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(QUERY_LOADER_ID, null, this);
        mMainPresenter.getPhotos();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == QUERY_LOADER_ID) {
            Uri uri = PhotosContract.PhotoTable.buildPhotoUri();
            return new CursorLoader(getActivity(), uri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int id = loader.getId();
        if (id == QUERY_LOADER_ID) {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        if (id == QUERY_LOADER_ID) {
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPhotosAdded() {
        // The cursor will updated automatically.
    }

    @Override
    public void onPhotosRetrieved() {
        // Do nothing the cursor loader will handle every thing.
    }

    static class ImageAdapter extends CursorRecyclerViewAdapter<ViewHolder> {
        private WeakReference<ThreadPoolImageLoader<ViewHolder>> mImageLoaderWeakReference;

        public ImageAdapter(Cursor cursor, ThreadPoolImageLoader<ViewHolder> imageLoader) {
            super(cursor);
            mImageLoaderWeakReference = new WeakReference<>(imageLoader);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
            String url = cursor.getString(cursor.getColumnIndex(PhotosContract.PhotoTable.COL_URL));
            holder.bindDrawable(holder.mImageView.getResources().getDrawable(R.mipmap.ic_launcher));
            ThreadPoolImageLoader<ViewHolder> imageLoader = mImageLoaderWeakReference.get();
            if(imageLoader != null)
                imageLoader.queueImage(holder, url);
            holder.mImageView.setTag(url);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view;
        }

        public void bindDrawable(Drawable d) {
            mImageView.setImageDrawable(d);
        }
    }
}
