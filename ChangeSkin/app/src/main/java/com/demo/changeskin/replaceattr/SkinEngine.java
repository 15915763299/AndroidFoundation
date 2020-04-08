package com.demo.changeskin.replaceattr;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author 尉迟涛
 * create time : 2020/4/7 1:02
 * description :
 */
public class SkinEngine {

    private static class Instance {
        private static final SkinEngine INSTANCE = new SkinEngine();
    }

    public static SkinEngine getInstance() {
        return Instance.INSTANCE;
    }

    private Resources mOutResource;
    private WeakReference<Context> contextWf;
    private String mOutPkgName;

    /**
     * 加载外部资源包
     */
    public void load(final String path, Context context) {
        this.contextWf = new WeakReference<>(context);
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        getOutPkgName(path);
        createResources(path);
    }

    private void getOutPkgName(final String path) {
        if (contextWf.get() != null) {
            PackageManager mPm = contextWf.get().getPackageManager();
            PackageInfo mInfo = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            mOutPkgName = mInfo.packageName;
        }
    }

    private AssetManager createAssetManager(final String path) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            // 加载资源核心原理
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createResources(final String path) {
        if (contextWf.get() != null) {
            Context context = contextWf.get();
            mOutResource = new Resources(
                    createAssetManager(path),
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration()
            );
        }
    }

    public int getColor(int resId) {
        if (contextWf.get() != null) {
            Context context = contextWf.get();
            if (mOutResource == null) {
                return resId;
            }

            try {
                //通过原Resource获取资源名称
                String resName = context.getResources().getResourceEntryName(resId);
                //根据名称与类型获取插件Resource编号
                int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
                if (outResId == 0) {
                    return resId;
                }
                return mOutResource.getColor(outResId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resId;
    }

    public Drawable getDrawable(int resId) {
        return getDrawableOrMipmap(resId, "drawable");
    }

    public Drawable getMipmap(int resId) {
        return getDrawableOrMipmap(resId, "mipmap");
    }

    private Drawable getDrawableOrMipmap(int resId, String defType) {
        if (contextWf.get() != null) {
            Context context = contextWf.get();
            if (mOutResource == null) {
                return ContextCompat.getDrawable(context, resId);
            }

            try {
                String resName = context.getResources().getResourceEntryName(resId);
                int outResId = mOutResource.getIdentifier(resName, defType, mOutPkgName);
                if (outResId == 0) {
                    return ContextCompat.getDrawable(context, resId);
                }
                return mOutResource.getDrawable(outResId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ContextCompat.getDrawable(context, resId);
        }
        return null;
    }

}
