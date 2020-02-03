package com.demo.threadandcache.cache.loader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.demo.threadandcache.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author 尉迟涛
 * create time : 2020/2/2 20:38
 * description : RecycleView 参考 https://www.jianshu.com/p/4e142909b824
 *
 * 还有需要优化的地方
 */
public class ActPictureFalls extends Activity {

    public static final int SPAN_COUNT = 2;
    private PicFallAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_picture_falls);

        adapter = new PicFallAdapter(this, getData());
        RecyclerView rv = findViewById(R.id.rv);
        rv.setAdapter(adapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL
        );
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        adapter.setLayoutManager(layoutManager);

        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(null);
        rv.addItemDecoration(new StaggeredDividerItemDecoration(this, 5));
        rv.addOnScrollListener(adapter.getScrollerListener());

        SmartRefreshLayout srl = findViewById(R.id.srl);
        //设置下拉刷新和上拉加载监听
        srl.setOnRefreshListener((@NonNull final RefreshLayout refreshLayout) -> {
            new Handler().postDelayed(() -> {
                //adapter.reset(getData());
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                refreshLayout.finishRefresh();
            }, 2000);
        });
    }

    private ArrayList<String> getData() {
        String[] imageUrls = {
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371978&di=5a5ae5fc72cdae7510f23ef25b936c0b&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180518%2Fb06b0251a4cc4e2cae3c24a21f05fc65.png",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371978&di=0b805737fac671af770c0aaccba4a4c8&imgtype=0&src=http%3A%2F%2Fwww.kedo.gov.cn%2Fphoto%2Fbjkpw%2Fupload%2FImage%2Fkhsj%2F1_2653723866.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371978&di=60c45120b78784e8f067696ceb27fad7&imgtype=0&src=http%3A%2F%2Fdesk.fd.zol-img.com.cn%2Fg5%2FM00%2F00%2F07%2FChMkJ1ZqMb2IWITEAAbRDaofaNIAAGBHwO3hh0ABtEl380.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=d3a04263f6a0a69c3e7e64ff5a5d4017&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Feabb51286bd921e7bdfbe3517f67985dc7bc2cd74dce2-Z3eXMc_fw658",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=9f719fa3b348f306efcfee1ca04eaf8a&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9fo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fd058ccbf6c81800abba4bb4cb33533fa828b472a.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=a358f8b781ce7d2960387faf3a3ae21c&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180512%2Fdff3d587b8cb415eb52bcfe5f3ff9d8f.jpeg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3182917774,3799603340&fm=26&gp=0.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=bb5df876764e3de60776ae067d7c2432&imgtype=0&src=http%3A%2F%2Fdik.img.kttpdq.com%2Fpic%2F46%2F32133%2Fd699fa629db877a6_640x960.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=a24e181218af00b5d8a8c4e858dd7307&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw870h489%2F20180312%2F7e2e-fyscsmu5535195.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580670371977&di=677b0bab65ef6a77ae195624e13a29f7&imgtype=0&src=http%3A%2F%2Fimg2.zol.com.cn%2Fproduct%2F19_500x2000%2F217%2Fce7QyaixEhMsw.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2954554996,3566213544&fm=26&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3364607806,1706241344&fm=26&gp=0.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580672669231&di=623ec09fdda23665c2d78de744269e41&imgtype=0&src=http%3A%2F%2Fupload.iceo.com.cn%2F4%2F2f%2F1288946872497.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580672708078&di=1e0ae40f693cc4cd70fe58a58e74d12d&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3511d234a4c30e9e7988a2377a7aa88d95761e743cf8e-kQf4ZO_fw658",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1580672708078&di=a67ca8017d6bb266b4046d21bd9bb7cb&imgtype=0&src=http%3A%2F%2Fi-7.vcimg.com%2Ftrim%2Fd22345c20b4f60f6ead694fea28b4327874495%2Ftrim.jpg"
        };
        ArrayList<String> urList = new ArrayList<>();
        Collections.addAll(urList, imageUrls);
        return urList;
    }


}
