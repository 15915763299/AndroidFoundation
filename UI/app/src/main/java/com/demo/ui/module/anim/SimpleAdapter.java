package com.demo.ui.module.anim;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.ui.R;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2019/12/5 21:21
 * description :
 */
public class SimpleAdapter extends BaseAdapter {

    private List<String> data;
    private LayoutInflater inflater;

    public SimpleAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_simple_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tx.setText(data.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView tx;

        ViewHolder(View view) {
            this.tx = view.findViewById(R.id.tx);
        }
    }
}
