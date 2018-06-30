package com.wesker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wesker.R;
import com.wesker.com.wesker.model.ListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hello，Mr.Zhang on 2017/7/19.
 */

public class MlistViewAdapter extends BaseAdapter {
    private List<ListData> mListData;
    private Context context;
    public List<ListData> getModel(){
        return this.mListData;
    }
    public MlistViewAdapter(Context appContext){
        this.context = appContext;
        this.mListData = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //convertView = inflater.inflate(R.layout.list,parent,false);
        ViewHolder holder;
        View view;
        // 判断convertView的状态，来达到复用效果
        if(convertView == null){
            // 如果convertView为空，则表示第一次显示该条目，需要创建一个view
            view = inflater.inflate(R.layout.list,parent,false);
            //新建一个viewholder对象
            holder = new ViewHolder();
            //将findviewbyID的结果赋值给holder对应的成员变量
            holder.text = (TextView) view.findViewById(R.id.textlist);
            // 将holder与view进行绑定
            view.setTag(R.id.holder,holder);

            Log.d("zzk","holder:"+(ViewHolder)view.getTag(R.id.holder));
        } else{
            // 否则表示可以复用convertView
            view = convertView;
            holder = (ViewHolder) view.getTag(R.id.holder);
        }
        // 直接操作holder中的成员变量即可，不需要每次都findViewById
        holder.text.setText(mListData.get(position).getCommand());
        view.setTag(R.id.data,mListData.get(position));
        //view.setTag(mListData.get(position));
        return view;
    }
    public static class ViewHolder{
        private TextView text;
    }
}
