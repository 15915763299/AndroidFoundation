package com.demo.threadandcache.cache.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.demo.threadandcache.R;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2020/2/2 22:16
 * description : 参考：https://www.cnblogs.com/qiuqiuQaQ/p/9375907.html
 *
 * 还有一些BUG
 */
public class PicFallGlideAdapter extends RecyclerView.Adapter<PicFallGlideAdapter.ViewHolder> {

    private Context context;
    private StaggeredGridLayoutManager layoutManager;
    private ArrayList<String> data;
    private LayoutInflater inflater;
    private RequestOptions options;
    private boolean isIdleOrDragging = true;

    PicFallGlideAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_picture, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = data.get(i);
        boolean isLoaded = (boolean) viewHolder.img.getTag(R.id.glide_tag);
        if (isIdleOrDragging || isLoaded) {
            Glide.with(context).pauseRequests();
        } else {
            Glide.with(context).resumeRequests();
        }

        viewHolder.img.setTag(R.id.glide_tag, false);
        Glide.with(context)
                .asDrawable()
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.img.setTag(R.id.glide_tag, true);
                        return false;
                    }
                })
                .into(viewHolder.img);
    }


    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        //当item被隐藏的时候
        ImageView imageView = holder.img;
        if (imageView != null) {
            Glide.with(context).clear(imageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            img.setTag(R.id.glide_tag, false);
        }
    }

    public void setLayoutManager(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public RecyclerViewScrollListener getScrollerListener() {
        return new RecyclerViewScrollListener();
    }

    /**
     * 开始滚动（SCROLL_STATE_FLING）
     * 正在滚动(SCROLL_STATE_TOUCH_SCROLL)
     * 已经停止（SCROLL_STATE_IDLE）
     */
    public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            isIdleOrDragging = newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING;

            //show 手指拖拽和停止均可展示图片
            if (isIdleOrDragging) {
                //通知adapter恢复getview中的图下载
                if (Glide.with(context).isPaused()) {
                    Glide.with(context).resumeRequests();
                }
                // 解决底部滚动到顶部时，顶部item上方偶尔会出现一大片间隔的问题
                if (layoutManager != null) {
                    int[] first = new int[ActPictureFallsGlide.SPAN_COUNT];
                    layoutManager.findFirstCompletelyVisibleItemPositions(first);
                    if (first[0] == 1 || first[1] == 1) {
                        layoutManager.invalidateSpanAssignments();
                    }
                }
            }
        }
    }
}
