package com.demo.threadandcache.cache.loader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.demo.threadandcache.R;
import com.demo.threadandcache.Utils;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2020/2/2 22:16
 * description :
 */
public class PicFallAdapter extends RecyclerView.Adapter<PicFallAdapter.ViewHolder> {

    private static final int PIC_WIDTH = Utils.getWindowDisplayMetrics().widthPixels / 2;
    private static final int PIC_HEIGHT = Integer.MAX_VALUE;

    private ImageLoader imageLoader;
    private StaggeredGridLayoutManager layoutManager;
    private ArrayList<String> data;
    private LayoutInflater inflater;
    private Drawable defaultPic;
    private boolean isIdle = true;

    PicFallAdapter(Context context, ArrayList<String> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        defaultPic = context.getResources().getDrawable(R.mipmap.image_default);
        imageLoader = ImageLoader.build();
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
        final String tag = (String) viewHolder.img.getTag();
        if (!url.equals(tag)) {
            viewHolder.img.setImageDrawable(defaultPic);
        }
        if (isIdle) {
            viewHolder.img.setTag(url);
            imageLoader.bindBitmap(url, viewHolder.img, PIC_WIDTH, PIC_HEIGHT);
        }
    }


    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
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
            isIdle = newState == RecyclerView.SCROLL_STATE_IDLE;
            if (isIdle) {
                // 继续加载
                PicFallAdapter.this.notifyItemRangeChanged(0, getItemCount());

                // 解决底部滚动到顶部时，顶部item上方偶尔会出现一大片间隔的问题
                if (layoutManager != null) {
                    int[] first = new int[ActPictureFalls.SPAN_COUNT];
                    layoutManager.findFirstCompletelyVisibleItemPositions(first);
                    if (first[0] == 1 || first[1] == 1) {
                        layoutManager.invalidateSpanAssignments();
                    }
                }
            }
        }
    }

//    public void reset(Collection<String> collection) {
//        data.clear();
//        data.addAll(collection);
//    }
}
