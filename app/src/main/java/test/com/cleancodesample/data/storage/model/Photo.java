package test.com.cleancodesample.data.storage.model;

/**
 * Created by hzaied on 7/16/16.
 */
public class Photo {
    private long mId;
    private String mTitle;
    private String mUrl;

    public Photo(String title, String url) {
        mTitle = title;
        mUrl = url;
    }

    public Photo(long id, String title, String url) {
        mId = id;
        mTitle = title;
        mUrl = url;
    }

    public long getId() {
        return mId;
    }
    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Photo) {
            Photo p = (Photo) o;
            return mUrl.equals(p.getUrl());
        }
        return false;
    }

}
