package Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import api.MyProfileInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import youtube_models.Item;
import youtube_models.YoutubePrimeObject;


public class SearchDataLoader extends Loader<List<Item>> {

    private List<Item> searchRows = new ArrayList<>();
    private Bundle mBundle;
    private String mQuery;
//    private static final String appId = "58f07270685892493fe98bc8";
    private static final String appId = "59a3db1dcc04dac1fe27a4f0"; // ("Movies,Documentary,Music,Short Film")
    public static final String TAG = SearchDataLoader.class.getSimpleName();


    public SearchDataLoader(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }


    @Override
    protected void onStartLoading() {
        if (searchRows != null) {
            forceLoad();
        } else {
            forceLoad();
        }
    }

        @Override
        protected void onForceLoad() {
            mQuery = mBundle.getString("query");
            try {
                new Retrofit.Builder()
                        .baseUrl("https://www.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(MyProfileInterface.class)
                        .getYoutubeFeeds("snippet","50", mQuery, "AIzaSyD09fkr2tGoND9seUYEvs-hc-vnFf18_hQ")
                        .enqueue(new Callback<YoutubePrimeObject>() {
                            @Override
                            public void onResponse(Call<YoutubePrimeObject> call, Response<YoutubePrimeObject> response) {
                                Log.d(TAG, "onResponse: "+response.code()+" "+response.body()+" " + response.body().getItems().size());
                                if(response.code() == 200)
                                {
                                    searchRows = response.body().getItems();
                                    deliverResult(searchRows);
                                }
                            }

                            @Override
                            public void onFailure(Call<YoutubePrimeObject> call, Throwable t) {
                                Log.d(TAG, "onFailure: "+t.getMessage());
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    @Override
    public void stopLoading() {
        super.stopLoading();
    }


    @Override
    protected void onReset() {
        //cancel api call.
        super.onReset();
    }

    @Override
    public void deliverResult(List<Item> data) {
        if (isStarted()) {
            super.deliverResult(data);
        }

    }
}
