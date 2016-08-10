package test.com.cleancodesample.presentation.ui.customviews;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import test.com.cleancodesample.R;
import test.com.cleancodesample.data.network.imageloader.ThreadPoolImageLoader;
import test.com.cleancodesample.data.storage.model.PhotosContract;
import test.com.cleancodesample.domain.model.Photo;

/**
 * The Adapter used by the recycler view.
 */
public class FlickrAdapter extends RecyclerView.Adapter<FlickrAdapter.ViewHolder> {

    private List<Photo> mPhotoList;
    private WeakReference<ThreadPoolImageLoader<ViewHolder>> mImageLoaderWeakReference;

    public FlickrAdapter(List<Photo> photoList, ThreadPoolImageLoader<ViewHolder> imageLoader) {
        mPhotoList = photoList;
        mImageLoaderWeakReference = new WeakReference<>(imageLoader);
    }

    public void setPhotoList(List<Photo> photoList) {
        mPhotoList = photoList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotoList.get(position);
        String url = photo.getUrl();
        holder.bindDrawable(holder.mImageView.getResources().getDrawable(R.mipmap.ic_launcher));

        // Lazy load the images.
        ThreadPoolImageLoader<ViewHolder> imageLoader = mImageLoaderWeakReference.get();
        if(imageLoader != null) imageLoader.queueImage(holder, url);
        holder.mImageView.setTag(url);
    }

    @Override
    public int getItemCount() {
        return mPhotoList!=null?mPhotoList.size():0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view;
        }

        public void bindDrawable(Drawable d) {
            mImageView.setImageDrawable(d);
        }


        public void setTag(String tag) {
            mImageView.setTag(tag);
        }
        public Object getTag() {
           return mImageView.getTag();
        }

    }

}
