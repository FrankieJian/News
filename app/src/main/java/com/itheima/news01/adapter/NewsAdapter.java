package com.itheima.news01.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.news01.R;
import com.itheima.news01.bean.NewsEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yls on 2017/6/28.
 */

public class NewsAdapter extends BaseAdapter {
    private Context context;

    /**
     * 列表显示的新闻数据
     */
    private List<NewsEntity.ResultBean> listDatas;

    public NewsAdapter(List<NewsEntity.ResultBean> listDatas, Context context) {
        this.context = context;
        this.listDatas = listDatas;
    }

    @Override
    public int getCount() {
        return (listDatas == null) ? 0 : listDatas.size();
    }

    @Override
    public NewsEntity.ResultBean getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //列表项数据
        NewsEntity.ResultBean info = (NewsEntity.ResultBean) getItem(position);

        int itemViewType = getItemViewType(position);
        
        if (itemViewType == ITEM_TYPE_WITH_1_IMAGE) {  // 第一种类型的列表项
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_1, null);
            }

            //查找列表item中的控件
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvSource = (TextView) convertView.findViewById(R.id.tv_source);
            TextView tvComment = (TextView) convertView.findViewById(R.id.tv_comment);

            //显示列表item中的子控件
            tvTitle.setText(info.getTitle());
            tvSource.setText(info.getSource());
            tvComment.setText(info.getReplyCount() + "跟帖");
            Picasso.with(context).load(info.getImgsrc()).into(ivIcon);
        }else if (itemViewType == ITEM_TYPE_WITH_3_IMAGE) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_2, null);
            }

            //查找列表item中的子控件
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView  tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            ImageView iv02 = (ImageView) convertView.findViewById(R.id.iv_02);
            ImageView iv03 = (ImageView) convertView.findViewById(R.id.iv_03);

            // 显示列表item中的子控件
            tvTitle.setText(info.getTitle());
            tvComment.setText(info.getReplyCount() + "跟帖");

            try {
                Picasso.with(context).load(info.getImgsrc()).into(iv01);;
                Picasso.with(context).load(info.getImgextra().get(0).getImgsrc()).into(iv02);
                Picasso.with(context).load(info.getImgextra().get(1).getImgsrc()).into(iv03);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return convertView;
    }

    //================多种item的列表显示(begin)=======================
    private static final int ITEM_TYPE_WITH_1_IMAGE = 0;
    private static final int ITEM_TYPE_WITH_3_IMAGE = 1;

    @Override
    public int getItemViewType(int position) {
        NewsEntity.ResultBean item = getItem(position);
        if (item.getImgextra() == null || item.getImgextra().size() == 0){
            //只有一张图的item
            return  ITEM_TYPE_WITH_1_IMAGE;
        }else {
            //item有中三张图片
            return ITEM_TYPE_WITH_3_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //================多种item的列表显示(end)=======================

    /**
     * 重置列表的所有的数据，并刷新列表显示
     * @param newsDatas
     */
    public void setDatas(NewsEntity newsDatas){
        this.listDatas = newsDatas.getResult();
        notifyDataSetChanged();
    }

    /** 追加数据，并刷新列表显示 */
    public void appendDatas(NewsEntity newsDatas){
        this.listDatas.addAll(newsDatas.getResult());
        notifyDataSetChanged();
    }
}
