package test.com.cleancodesample.domain.model;

/**
 * The main entity data holder in the application.
 */
public class Photo {
    private String mTitle;
    private String mUrl;

    public Photo(String title, String url) {
        mTitle = title;
        mUrl = url;
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
