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
import java.util.List;

import javax.inject.Inject;

import test.com.cleancodesample.AndroidApplication;
import test.com.cleancodesample.R;
import test.com.cleancodesample.dagger.component.ApplicationComponent;
import test.com.cleancodesample.dagger.component.DaggerPhotoComponent;
import test.com.cleancodesample.dagger.component.PhotoComponent;
import test.com.cleancodesample.data.network.imageloader.ThreadPoolImageLoader;
import test.com.cleancodesample.domain.model.Photo;
import test.com.cleancodesample.presentation.presenter.MainPresenter;
import test.com.cleancodesample.presentation.presenter.impl.MainPresenterImpl;
import test.com.cleancodesample.presentation.ui.customviews.FlickrAdapter;

public class MainFragment extends Fragment implements MainPresenter.View {
//    private final static int QUERY_LOADER_ID = 10;

    // Views
    private RecyclerView mRecyclerView;
    private FlickrAdapter mAdapter;
    private ThreadPoolImageLoader<FlickrAdapter.ViewHolder> mImageLoader;

    // This has to be public for Dagger to be able to inject it.
    public @Inject MainPresenterImpl mMainPresenter;

    private PhotoComponent mPhotoComponent;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = new ThreadPoolImageLoader<>(new ThreadPoolImageLoader.OnDownloadListener<FlickrAdapter.ViewHolder>() {
            @Override
            public void onImageDownloaded(FlickrAdapter.ViewHolder target, String url, Bitmap bitmap) {
                Log.e("test", "Image Downloaded");
                if(getActivity() != null) {
                    if (url.equals(target.getTag())) {
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
        mAdapter = new FlickrAdapter(null, mImageLoader);
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
        mMainPresenter.getPhotos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPhotosAdded() {
        // Do nothing.
    }

    @Override
    public void onPhotosRetrieved(List<Photo> photos) {
        mAdapter.setPhotoList(photos);
    }
}
