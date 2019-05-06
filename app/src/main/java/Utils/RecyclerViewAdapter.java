package Utils;

import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import tv.cloudwalker.androidsocketclient.R;
import youtube_models.Item;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    public List<String> mItemList;

    public RecyclerViewAdapter(List<String> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(true);

            }
        });
    }


    public void clear(){
        if(mItemList.size() > 0) {
            int size = mItemList.size();
            Log.d(TAG, "clear: "+size);
            mItemList.clear();
            Log.d(TAG, "clear: "+mItemList.size());
            notifyItemRangeRemoved(0, size);
            Log.d(TAG, "clear: notfiy 1");
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Item> items) {
        if(mItemList.size() > 0){
            Log.d(TAG, "addAll: "+mItemList.size());
            for(int i = 0; i < items.size() ; i++){
                mItemList.add(i, items.get(i).getSnippet().getThumbnails().getMedium().getUrl());
            }
            notifyItemRangeChanged(0, items.size());
            Log.d(TAG, "addAll: notfy");
            notifyDataSetChanged();
        }
    }

    public void  refreshData(List<String> items)
    {
        this.mItemList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView tvItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String item = mItemList.get(position);
        Log.d(TAG, "populateItemRows: "+item);
        Glide.with(viewHolder.tvItem.getContext())
                .load(item)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(viewHolder.tvItem);

    }

}
