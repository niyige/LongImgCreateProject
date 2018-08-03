package com.oyy.longimgcreate.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyy.longimgcreate.R;

import java.util.List;

/**
 * create by 893007592@qq.com
 * on 2018/8/2 14:32
 * Description: 适配器
 */
public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> data;

    private Activity mActivity;

    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;
    private final static int TYPE_HEADER = 3;

    public DataAdapter(List<String> data, Activity mActivity) {

        this.data = data;
        this.mActivity = mActivity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == data.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.header_layout, parent, false);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_CONTENT) {
            View contentView = mActivity.getLayoutInflater().inflate(R.layout.content_layout, null);
            return new ContentHolder(contentView);
        } else {
            View footerView = mActivity.getLayoutInflater().inflate(R.layout.footer_layout, parent, false);
            return new FooterHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderHolder) {
            //头部

        } else if (holder instanceof ContentHolder) { //内容从第二个数据开始 ，第一个是头部
            ((ContentHolder) holder).title.setText(data.get(position - 1));
        } else {
            //尾部

        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 2;   //加了头跟尾部
    }


    /**
     * 头部布局
     */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        private ImageView headerImg;

        public HeaderHolder(View itemView) {
            super(itemView);
            headerImg = itemView.findViewById(R.id.headerImg);
        }

    }

    /**
     * 普通item
     */
    public class ContentHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ContentHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    /**
     * 尾部布局
     */
    public class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }
}
