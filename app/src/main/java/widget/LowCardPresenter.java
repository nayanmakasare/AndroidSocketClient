package widget;

import android.content.Context;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import model.MovieTile;
import youtube_models.Medium;

public class LowCardPresenter extends Presenter
{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        ImageCardView imageCardView = new ImageCardView(parent.getContext());
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setCardType(BaseCardView.CARD_TYPE_MAIN_ONLY);
        return new ViewHolder(imageCardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        if(item instanceof Medium)
        {
            Glide.with(viewHolder.view.getContext())
                    .load(((Medium) item).getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(((Medium) item).getWidth(), ((Medium) item).getHeight())
                    .skipMemoryCache(true)
                    .into(((ImageCardView)viewHolder.view).getMainImageView());
        }
        else if(item instanceof MovieTile)
        {
            Glide.with(viewHolder.view.getContext())
                    .load(((MovieTile) item).getPortrait())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(300, 200)
                    .skipMemoryCache(true)
                    .into(((ImageCardView)viewHolder.view).getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        (((ImageCardView)viewHolder.view).getMainImageView()).setImageDrawable(null);
    }

    private int dpToPx(Context ctx , int dp) {
        float density = ctx.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
