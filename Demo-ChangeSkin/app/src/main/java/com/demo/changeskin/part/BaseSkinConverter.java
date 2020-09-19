package com.demo.changeskin.part;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.changeskin.App;

/**
 * @author apptest4
 * @date 2020/4/7
 * <p>
 * 使用SharePreference保存启用状态
 */
public abstract class BaseSkinConverter implements SkinConverter {

    private static final String DEFAULT_FILE = "default";
    protected boolean isEnable;
    private SharedPreferences sp;

    BaseSkinConverter() {
        sp = App.getApp().getSharedPreferences(DEFAULT_FILE, Context.MODE_PRIVATE);
        if (sp != null) {
            isEnable = sp.getBoolean(getSpName(), false);
        }
    }

    protected abstract String getSpName();

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(DEFAULT_FILE + getSpName(), enable);
            editor.apply();
        }
    }

}
