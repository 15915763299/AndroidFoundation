package com.demo.lazyload.lazyfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/2/24 0:53
 * description :
 * 如何解决嵌套Fragment问题？
 * 模仿点击事件分发，定义显示、隐藏事件的dispatch方法
 * 判断子Fragment是否为懒加载Fragment，如果是，就分发显示、隐藏事件
 *
 * 显示 --> onFragmentResume，加载资源
 * 隐藏 --> onFragmentPause，释放资源
 */
public abstract class LazyFragment extends Fragment {

    // fragment 生命周期：
    // onAttach -> onCreate -> onCreatedView -> onActivityCreated ->
    // onStart -> onResume -> onPause -> onStop ->
    // onDestroyView -> onDestroy -> onDetach

    // 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有：
    // onCreatedView + onActivityCreated + onResume + onPause + onDestroyView

    private int behavior = FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;

    /**
     * View 是否创建
     */
    private boolean isViewCreated = false;
    /**
     * 当前 Fragment 是否可见
     */
    private boolean isFragmentVisible = false;
    /**
     * 是否初次可见
     */
    private boolean isFirstVisible = true;
    /**
     * 缓存 rootView
     */
    private View rootView;

    /**
     * 获取 layout 的 id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化 View
     */
    protected abstract void initView(View view);

    /**
     * Fragment 首次可见
     */
    protected abstract void onFragmentFirstVisible();

    /**
     * Fragment Resume，这里进行各种资源加载
     */
    protected abstract void onFragmentResume();

    /**
     * Fragment Pause，这里释放给中资源
     */
    protected abstract void onFragmentPause();


    private void log(String info) {
        Log.d("LazyFragment", info);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            log("load root view");
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        initView(rootView);
        isViewCreated = true;

        //初始化的时候，判断当前 Fragment 可见状态
        if (!isHidden()) {
            if (behavior == FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
                if (getUserVisibleHint()) {
                    dispatchUserVisibleHint(true);
                }
            }
        }
        return rootView;
    }

    /**
     * setUserVisibleHint 调用时机：
     * 1、在 ViewPager 切换的时候，先于 Fragment 所有生命周期调用
     * 2、对于之前已经调用过 setUserVisibleHint 方法的 Fragment 后，让 Fragment 从可见到不可见之间状态的变化
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 只处理第二种情况
        if (isViewCreated) {
            // isFragmentVisible 作为状态缓存
            if (isVisibleToUser && !isFragmentVisible) {
                // 从不可见变为可见 false --> true
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && isFragmentVisible) {
                // 从可见变为不可见 true --> false
                dispatchUserVisibleHint(false);
            }
        }
    }

    /**
     * 统一处理用户可见信息分发
     *
     * @param isVisible 当前是否可见
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        // 当前可见，并且父 Fragment 可见
        if (isVisible && isParentInvisible()) {
            return;
        }
        if (isFragmentVisible == isVisible) {
            return;
        }
        isFragmentVisible = isVisible;

        if (isVisible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            // 在双重ViewPager嵌套的情况下，第一次滑到包含 ViewPager 的 Fragment 的时候
            // 只会加载当前 Fragment，不会加载内嵌 viewPager 中的 Fragment
            // 因此，当外层 Fragment 可见的时候，需要分发可见事件给子 Fragment
            dispatchChildVisibleState(true);
        } else {
            onFragmentPause();
            dispatchChildVisibleState(false);
        }

    }

    /**
     * 父 Fragment 是否可见
     */
    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof LazyFragment) {
            LazyFragment parent = (LazyFragment) parentFragment;
            return !parent.isFragmentVisible();
        }
        return false;
    }

    /**
     * 当前 Fragment 是否可见
     */
    private boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * 给子 Fragment 分发可见事件
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof LazyFragment && !fragment.isHidden()) {
                if (behavior == FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
                    if (fragment.getUserVisibleHint()) {
                        ((LazyFragment) fragment).dispatchUserVisibleHint(visible);
                    }
                }
            }
        }
    }

    /**
     * 用 FragmentTransaction 来控制 Fragment 的 hide 和 show 时，该方法就会被调用。
     * 每当你对某个 Fragment 使用 hide 或者是 show 的时候，该方法就会被自动调用。
     * https://blog.csdn.net/u013278099/article/details/72869175
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        log("onHiddenChanged: " + hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (behavior == FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
            // 在滑动或者跳转的过程中，第一次创建 Fragment 的时候均会调用 onResume 方法，
            // 类似于在 tab1 滑到 tab2，此时 tab3 会缓存，这个时候会调用 tab3 fragment 的 onResume，
            // 所以，此时是不需要去调用 dispatchUserVisibleHint(true) 的
            if (!isFirstVisible) {
                // Activity1 中如果有多个 Fragment，然后从 Activity1 跳转到 Activity2，
                // 此时会有多个 Fragment 会在 Activity1 中缓存，此时，如果再从 Activity2 跳转回 Activity1，
                // 会将所有的缓存的 Fragment 进行 onResume 生命周期的重复，
                // 这个时候我们无需对所有缓存的 Fragment 调用 dispatchUserVisibleHint(true)
                // 我们只需要对可见的 Fragment 进行加载
                if (!isHidden() && !isFragmentVisible && getUserVisibleHint()) {
                    dispatchUserVisibleHint(true);
                }
            }
        } else if(getUserVisibleHint()){
            dispatchUserVisibleHint(true);
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint(false)
     * isFragmentVisible && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     * 子 fragment 走到这里的时候自身又会调用一遍
     */
    @Override
    public void onPause() {
        super.onPause();
        if (behavior == FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT) {
            if (isFragmentVisible && getUserVisibleHint()) {
                dispatchUserVisibleHint(false);
            }
        } else {
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        isFirstVisible = true;
    }

    public void setBehavior(int behavior) {
        if (behavior != 0 && behavior != 1) {
            return;
        }
        this.behavior = behavior;
    }
}
