package com.demo.changeskin.x;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.io.File;
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
    private Context context;
    private String mOutPkgName;


    /**
     * 加载外部资源包
     *
     * @param path
     */
    public void load(final String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        getOutPkgName(path);
        createResources(path);
    }

    private void getOutPkgName(final String path) {
        PackageManager mPm = context.getPackageManager();
        PackageInfo mInfo = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        mOutPkgName = mInfo.packageName;
    }

    private AssetManager createAssetManager(final String path) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            // 加载资源核心原理
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath");
            addAssetPath.invoke(assetManager, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createResources(final String path) {
        mOutResource = new Resources(createAssetManager(path),
                context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    public int getColor(int resId) {
        try {
            if (mOutResource == null) {
                return resId;
            }
            String resName = mOutResource.getResourceEntryName(resId);
            int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
            if (outResId == 0) {
                return resId;
            }
            return mOutResource.getColor(outResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resId;
    }

    public Drawable getDrawable(int resId) {
        try {
            if (mOutResource == null) {
                return ContextCompat.getDrawable(context, resId);
            }
            String resName = mOutResource.getResourceEntryName(resId);
            int outResId = mOutResource.getIdentifier(resName, "color", mOutPkgName);
            if (outResId == 0) {
                return ContextCompat.getDrawable(context, resId);
            }
            return mOutResource.getDrawable(outResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ContextCompat.getDrawable(context, resId);
    }
}
