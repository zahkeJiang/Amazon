package amazon.jy.com.amazon.core.okhttp;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangy on 18-5-2.
 */

public class OkHttp {

    private static final String HOST = "http://47.94.227.99/Amazon/";

    private String mUrlPath;

    private Request.Builder mRequestBuilder;

    private Callback mCallback;

    private Map<String, String> mParams;

    private static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public interface Callback<T> {
        void onDataReceived(T result);
    }

    private OkHttp(String path) {
        mRequestBuilder = new Request.Builder();
        mUrlPath = path;
        mParams = new HashMap<>();
    }

    public static OkHttp request(String path) {
        return new OkHttp(path);
    }

    public static Request buildPostRequest(Request.Builder builder, String url, RequestBody body) {
        return builder.url(url).post(body).build();
    }


    public static RequestBody keyValueForm(Map<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        return builder.build();
    }

    /**
     * send request asynchronously. after the data is received and processed by {@link
     * #onProcessResponse(Response)}, {@code callback} will be called on original thread that
     * call this method.
     *
     * @param callback callback used to receive data.
     */
    public void send(OkHttp.Callback<JSONObject> callback) {
        mCallback = callback;
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    RequestBody body = keyValueForm(mParams);
                    Request request = buildPostRequest(mRequestBuilder, HOST + mUrlPath, body);
                    Response response = HTTP_CLIENT.newCall(request).execute();
                    return onProcessResponse(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONObject data) {
                mCallback.onDataReceived(data);
            }
        }.execute();
    }



    protected JSONObject onProcessResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            return null;
        }
        ResponseBody body = response.body();
        JSONObject result = null;
        try {
            result = new JSONObject(body.string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public OkHttp addParameter(String key, String value) {
        mParams.put(key, value);
        return this;
    }
}
