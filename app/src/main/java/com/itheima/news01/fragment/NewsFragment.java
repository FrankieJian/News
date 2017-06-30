package com.itheima.news01.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.itheima.news01.NewsDetailActivity;
import com.itheima.news01.R;
import com.itheima.news01.adapter.NewsAdapter;
import com.itheima.news01.base.URLManager;
import com.itheima.news01.bean.NewsEntity;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.RotationHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;
/**
 * Created by yls on 2017/6/29.
 */

public class NewsFragment extends BaseFragment {
    /** 新闻类别id */
    private String channelId;
    private ListView listView;
    private NewsAdapter newsAdapter;
    private SpringView springView;
    private View headerView;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView() {
        TextView textView = (TextView) mRootView.findViewById(R.id.tv_01);
        listView = (ListView) mRootView.findViewById(R.id.list_view);
        newsAdapter = new NewsAdapter(null, getContext());
        listView.setAdapter(newsAdapter);

        textView.setText("类别id:"+channelId);   //显示新闻类别id，以作区分

        initSpringView();
    }

    private void initSpringView() {
        springView = (SpringView) mRootView.findViewById(R.id.spring_view);
        // 设置头部和尾部
        springView.setHeader(new RotationHeader(getContext()));
        springView.setFooter(new MeituanFooter(getContext()));

        springView.setType(SpringView.Type.OVERLAP);
        //设置监听器
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {  // 下拉刷新第一页数据
                showToast("Loading...");

                getDataFromServer(true);
            }

            @Override
            public void onLoadmore() { // 上拉，加载下一页数据
                showToast("Loading...");

                getDataFromServer(false);
            }
        });
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int index = position;
//
//                if (listView.getHeaderViewsCount() > 0 ){
//                    index = index - 1;
//                }

                // 用户点击的新闻
                NewsEntity.ResultBean newsBean = (NewsEntity.ResultBean) parent.getItemAtPosition(position);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", newsBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getDataFromServer(true);
    }

    /** 要加载第几页数据 */
    private int pageNo = 1;

    /** 获取服务器新闻数据
     *
     * @param refresh true表示下拉刷新，false表示加载下一页数据
     * */
    private void getDataFromServer(final boolean refresh) {
        if (refresh) // 如果是下拉刷新
            pageNo = 1;

        String url = URLManager.getUrl(channelId, pageNo);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
//                Log.d("test","----服务器返回的json数据：" + json);
                System.out.print("----服务器返回的json数据：" + json);

                json = json.replace(channelId, "result");
                Gson gson = new Gson();
                NewsEntity newsDatas = gson.fromJson(json, NewsEntity.class);
                System.out.print("----解析json:"+newsDatas.getResult().size());

                // 显示数据到列表中
                if (refresh) { // 下拉刷新
                    showDatas(newsDatas);
                } else {      // 上啦加载下一页
                    newsAdapter.appendDatas(newsDatas);
                }
                pageNo++;    //页码自增1
                // 隐藏SpringView控件
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.print("----error：" + error);
                error.printStackTrace();
            }
        });
    }
    private void showDatas(NewsEntity newsDatas) {
        if (newsDatas == null
                || newsDatas.getResult() == null
                || newsDatas.getResult().size() == 0) {
            System.out.print("----没有获取到服务器的新闻数据");
            return;
        }

        // （1）显示轮播图
        if (listView.getHeaderViewsCount() > 0){
            listView.removeHeaderView(headerView);
        }

        List<NewsEntity.ResultBean.AdsBean> ads = newsDatas.getResult().get(0).getAds();

        //有轮播图广告
        if (ads != null && ads.size() > 0){
            headerView = View.inflate(mActivity, R.layout.list_header, null);
            SliderLayout sliderLayout = (SliderLayout) headerView.findViewById(R.id.slider_layout);

            for (int i = 0; i < ads.size(); i++) {
                //一则广告数据
                NewsEntity.ResultBean.AdsBean adBean = ads.get(i);

                TextSliderView sliderView = new TextSliderView(mActivity);
                sliderView.description(adBean.getTitle()).image(adBean.getImgsrc());
                //添加子界面
                sliderLayout.addSlider(sliderView);
                //设置点击事件
                sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        showToast(slider.getDescription());
                    }
                });
            }

            //添加表头布局
            listView.addHeaderView(headerView);
        }

        // （2）显示新闻列表
        newsAdapter.setDatas(newsDatas);
    }
}
