package com.example.camille.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaCodecList;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.camille.loding.LodingView;
import com.example.camille.loding.R;

import java.util.List;

/**
 * Created by camille on 2020/2/18.
 */

public class recyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> mList;

    private Context mContext;

    private static int NORMAL = 0;

    private static int FOOTER = 1;

    private LinearLayout mFooterView;

    public recyclerViewAdapter(List<String> list, Context context){
        mList = list;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NomalItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NomalItemViewHolder){
            ((NomalItemViewHolder) holder).itemView.setText(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return FOOTER;
        } else {
            return NORMAL;
        }
    }

    public void addFooterView(){
        mFooterView.addView(new LodingView(mContext));
    }

    public void removeFooterView(){
        mFooterView.removeAllViews();
    }

    public class NomalItemViewHolder extends RecyclerView.ViewHolder{

        private TextView itemView;

        public NomalItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.item_text);
            Resources resources = mContext.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width3 = dm.widthPixels;
            this.itemView.setWidth(width3);
        }
    }

    public class FooterItemViewHolder extends RecyclerView.ViewHolder{

        public FooterItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
