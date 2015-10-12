package net.suool.igdufe.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by SuooL on 15/10/9.
 */
public class MySimpleAdapter extends SimpleAdapter {
        /**
         * 构造方法，对实例域进行引用复制
         *
         * @param context
         * @param data
         * @param resource
         * @param from
         * @param to
         * @see android.widget.SimpleAdapter
         */
        public MySimpleAdapter(Context context,
                                 List<? extends Map<String, ?>> data, int resource,
                                 String[] from, int[] to) {

            super(context, data, resource, from, to);
        }


        /**
         * @see android.widget.Adapter#getView(int, View, ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView != null) {
                view = convertView;
                // 使用缓存的view,节约内存
                // 当listview的item过多时，拖动会遮住一部分item，被遮住的item的view就是convertView保存着。
                // 当滚动条回到之前被遮住的item时，直接使用convertView，而不必再去new view()

            } else {
                view = super.getView(position, convertView, parent);

            }

            int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色

            int pos = position%4 + 1;
//            view.setBackground();

            view.setBackgroundColor(colors[position % 2]);// 每隔item之间颜色不同

            return super.getView(position, view, parent);
        }


}