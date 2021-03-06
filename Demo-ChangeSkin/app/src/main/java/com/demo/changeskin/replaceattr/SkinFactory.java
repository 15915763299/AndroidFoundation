package com.demo.changeskin.replaceattr;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;

import com.demo.changeskin.R;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/4/7 0:26
 * description :TODO setBackgroundDrawable,流程图
 */
public class SkinFactory implements LayoutInflater.Factory2 {

    private AppCompatDelegate mDelegate;
    private List<SkinView> cacheSkinView = new ArrayList<>();

    public void setDelegate(AppCompatDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class
    };
    private final Object[] mConstructorArgs = new Object[2];
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();
    private static final String[] prefixs = new String[]{
            "android.widget.",
            "android.view",
            "android.webkit"
    };

    private static class SkinView {
        View view;
        HashMap<String, String> attrsMap;

        void changeSkin() {
            //TODO 可以定义更详细的换肤规则，字体、背景、大小。、、、、
            if (!TextUtils.isEmpty(attrsMap.get("background"))) {

                int id = Integer.parseInt(attrsMap.get("background").substring(1));
                String attrType = view.getResources().getResourceTypeName(id);
                if (TextUtils.equals(attrType, "drawable")) {
                    view.setBackground(SkinEngine.getInstance().getDrawable(id));
                } else if (TextUtils.equals(attrType, "color")) {
                    view.setBackgroundColor(SkinEngine.getInstance().getColor(id));
                }

            } else if (!TextUtils.isEmpty(attrsMap.get("src"))) {

                int id = Integer.parseInt(attrsMap.get("src").substring(1));
                String attrType = view.getResources().getResourceTypeName(id);
                if (TextUtils.equals(attrType, "mipmap")) {
                    ((ImageView) view).setImageDrawable(SkinEngine.getInstance().getMipmap(id));
                }

            }
        }
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = mDelegate.createView(parent, name, context, attrs);
        if (view != null) {
            collectSkinView(context, attrs, view);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public final View createView(Context context, String name, String[] prefixs, AttributeSet attrs) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz = null;

        if (constructor == null) {
            try {
                if (prefixs != null && prefixs.length > 0) {
                    for (String prefix : prefixs) {
                        clazz = context.getClassLoader().loadClass(
                                prefix != null ? (prefix + name) : name
                        ).asSubclass(View.class);
                        if (clazz != null) {
                            break;
                        }
                    }
                } else {
                    if (clazz == null) {
                        clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                    }
                }
                if (clazz == null) {
                    return null;
                }
                constructor = clazz.getConstructor(mConstructorSignature);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            constructor.setAccessible(true);
            sConstructorMap.put(name, constructor);
        }
        Object[] args = mConstructorArgs;
        args[1] = attrs;
        try {
            final View view = constructor.newInstance(args);
            return view;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void collectSkinView(Context context, AttributeSet attrs, View view) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Shin);
        boolean isSupport = a.getBoolean(R.styleable.Shin_isSupportSkinChange, false);
        if (isSupport) {
            final int Len = attrs.getAttributeCount();
            HashMap<String, String> attrMap = new HashMap<>();
            for (int i = 0; i < Len; i++) {
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);
                attrMap.put(attrName, attrValue);
            }
            SkinView skinView = new SkinView();
            skinView.view = view;
            skinView.attrsMap = attrMap;
            cacheSkinView.add(skinView);
        }
        a.recycle();
    }


    public void changeSkin() {
        for (SkinView skinView : cacheSkinView) {
            skinView.changeSkin();
        }
    }
}
