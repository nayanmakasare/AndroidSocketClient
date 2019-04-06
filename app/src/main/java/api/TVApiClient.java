package api;

import android.content.Context;

import Utils.TvCustomHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.cloudwalker.androidsocketclient.BuildConfig;

/**
 * Created by cognoscis on 14/4/18.
 */

public class TVApiClient {
//    public static final String BASE_URL = BuildConfig.SERVER_URL;
    public static final String BASE_URL = "http://192.168.1.245:5080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(TvCustomHttpClient.getHttpClient(context, BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
