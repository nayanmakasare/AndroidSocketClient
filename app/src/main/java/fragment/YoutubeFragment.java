package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import Utils.RecyclerViewAdapter;
import Utils.SearchDataLoader;
import api.MyProfileInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.cloudwalker.androidsocketclient.R;
import youtube_models.Item;
import youtube_models.YoutubePrimeObject;

public class YoutubeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Item>>
{

    public static final String TAG = YoutubeFragment.class.getSimpleName();
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    boolean isLoading = false;
    private RecyclerView recyclerView;
    private static final int SEARCH_VIDEOS_LOADER = 1;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_youtube, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        populateData();
        initScrollListener();
        ((SearchView) view.findViewById(R.id.searchView)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                getLoaderManager().restartLoader(SEARCH_VIDEOS_LOADER, bundle, YoutubeFragment.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void populateData() {
        new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MyProfileInterface.class)
                .getYoutubeFeeds("snippet","25", "lord krishna", "AIzaSyD09fkr2tGoND9seUYEvs-hc-vnFf18_hQ")
                .enqueue(new Callback<YoutubePrimeObject>() {
                    @Override
                    public void onResponse(Call<YoutubePrimeObject> call, Response<YoutubePrimeObject> response) {
                        Log.d(TAG, "onResponse: "+response.code()+" "+response.body());
                        if(response.code() == 200)
                        {
                            for(Item item : response.body().getItems()) {
                                rowsArrayList.add(item.getSnippet().getThumbnails().getMedium().getUrl());
                            }
                            initAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubePrimeObject> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }


    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
//                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    @NonNull
    @Override
    public Loader<List<Item>> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == SEARCH_VIDEOS_LOADER) {
            return new SearchDataLoader(getActivity(), bundle);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Item>> loader, List<Item> items) {
        Log.d(TAG, "onLoadFinished: "+items.size());
        if (loader.getId() == SEARCH_VIDEOS_LOADER) {

            for(int i = 0 ; i < items.size() ; i++)
            {
                rowsArrayList.remove(i);
                recyclerView.removeViews(0,i);
                recyclerViewAdapter.notifyItemRemoved(i);
                rowsArrayList.add(i,items.get(i).getSnippet().getThumbnails().getMedium().getUrl());
                recyclerViewAdapter.notifyItemInserted(i);
            }
            recyclerViewAdapter.notifyItemRangeChanged(0, rowsArrayList.size());
            recyclerViewAdapter.notifyDataSetChanged();

//            ArrayList<String> refreshString = new ArrayList<>();
//            for(Item item : items){
//                refreshString.add(item.getSnippet().getThumbnails().getMedium().getUrl());
//            }
//
//            recyclerViewAdapter.notifyDataSetChanged();
//            if (recyclerViewAdapter.getItemCount() > 0 || recyclerViewAdapter.getItemCount() == 0) {
//                Log.d(TAG, "onLoadFinished: clear");
//                recyclerViewAdapter.clear();
//            }
//            if (items != null && items.size() > 0) {
//                Log.d(TAG, "onLoadFinished: add All");
//                recyclerViewAdapter.addAll(items);
//            }
            getLoaderManager().destroyLoader(SEARCH_VIDEOS_LOADER);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Item>> loader) {

    }
}
