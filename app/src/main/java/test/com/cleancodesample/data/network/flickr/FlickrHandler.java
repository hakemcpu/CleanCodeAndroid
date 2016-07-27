package test.com.cleancodesample.data.network.flickr;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import test.com.cleancodesample.domain.model.Photo;

public class FlickrHandler {

    public byte[] getBytes(String url) throws IOException {
        URL uri = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        InputStream in = connection.getInputStream();

        try{
            byte[] arr = new byte[4*1024];
            int n = 0;
            while((n=in.read(arr))>0) {
                out.write(arr, 0, n);
            }

            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getString(String url) throws IOException {
        return new String(getBytes(url));
    }

    public List<Photo> fetchItems() {
        final String URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=5f412b6f21aa9b6978c979c1ca806375&format=json&nojsoncallback=1";
        try {
            List<Photo> photos = parsePhotos(getString(URL));

            // NOTE: Just for the sake of using Eventbus, we already have a callback mechanism.
            EventBus.getDefault().post("Finished updating the data");

            return photos;
        } catch (Exception e) {
            Log.e("test", "Exception ", e);
        }
        return new ArrayList<>();
    }

    public List<Photo> parsePhotos(String json) throws JSONException {
        List<Photo> items = new ArrayList<>();
        JSONObject photosJsonObject = new JSONObject(json).getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoObject = photoJsonArray.getJSONObject(i);
            String photoURLFormate = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

            String farmId = photoObject.getString("farm");
            String serverId = photoObject.getString("server");
            String photoId = photoObject.getString("id");
            String secret = photoObject.getString("secret");
            String photoUrl = String.format(photoURLFormate, farmId, serverId, photoId, secret);

            Photo item = new Photo("", photoUrl);
            items.add(item);
        }
        return items;
    }
}
