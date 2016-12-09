package com.example.wyz.videoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.yolanda.nohttp.NoHttp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<VideoBean> videoBeenList=new ArrayList<>();
    RecyclerAdapter mRecyclerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MyHandler mMyHandler=new MyHandler();
    int mCurrentPage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoHttp.initialize(this);
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //有网情况
                if(!NetWorkUtil.isNetworkErrThenShowMsg()){
                    new Thread(){
                        @Override
                        public void run() {
                                mCurrentPage=0;
                                refreshData();
                                Message message=Message.obtain();
                                message.what=3;
                                mMyHandler.sendMessage(message);

                        }
                    }.start();
                }else {
                   mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        //有网情况
        if(!NetWorkUtil.isNetworkErrThenShowMsg()) {
             MyThread myThread = new MyThread();
             myThread.start();
        }
    }



    private void initRecyclerView() {

        mRecyclerAdapter=new RecyclerAdapter(this,videoBeenList);
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //有网情况
                if(!NetWorkUtil.isNetworkErrThenShowMsg()){
                    if(mCurrentPage==currentPage){
                        Message message=Message.obtain();
                        message.what=4;
                        mMyHandler.sendMessage(message);
                    }else{
                        mCurrentPage=currentPage;
                        new Thread(){
                            @Override
                            public void run() {
                                loadMoreData(mCurrentPage);
                            }
                        }.start();
                    }
                }
                else{
                    //mRecyclerAdapter.notifyItemRemoved(mRecyclerAdapter.getItemCount()-1);
                    //mRecyclerAdapter.setNoNetFooterView();
                }

            }
        });
        setFooterView(mRecyclerView);
    }

    private void setFooterView(RecyclerView recyclerView) {
        View footer= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_footer,recyclerView,false);
        mRecyclerAdapter.setFooterView(footer);


    }

    private void loadData() {

        mCurrentPage=0;
        String url=ApiConstants.VIDEO_BEGIN_URL+mCurrentPage+ApiConstants.VIDEO_END_URL;
        JSONObject jsonObject=NohttpUtil.getRequest(url);
        videoBeenList=NetWorkUtil.StringToVideoBean(jsonObject);

    }
    private  void initData(){
        loadData();
        Message message=Message.obtain();
        message.what=1;
        mMyHandler.sendMessage(message);
    }
    private  void refreshData(){
        loadData();
    }
    private void loadMoreData(int currentPage) {
        mCurrentPage=currentPage;
        int startIndex=mCurrentPage*10;
        String url=ApiConstants.VIDEO_BEGIN_URL+startIndex+ApiConstants.VIDEO_END_URL;
        JSONObject jsonObject=NohttpUtil.getRequest(url);
        List<VideoBean> videoBeen=NetWorkUtil.StringToVideoBean(jsonObject);
        videoBeenList.addAll(videoBeen);

        Message message=Message.obtain();
        message.what=2;
        mMyHandler.sendMessage(message);



    }

    private   class MyThread  extends  Thread{
        @Override
        public void run() {
            initData();
        }
    }
    public class  MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                //加载更多数据
                case  2:
                    if(mRecyclerAdapter!=null){
                        mRecyclerAdapter.itemInfos=videoBeenList;
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                //初始化
                case  1:
                    initRecyclerView();
                    break;
                //下拉刷新
                case  3:
                    if(mRecyclerAdapter==null){
                        initRecyclerView();
                    }else {
                        mRecyclerAdapter.itemInfos = videoBeenList;
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                //加载数据没有更多
                case  4:
                    if(mRecyclerAdapter!=null) {
                        mRecyclerAdapter.setNoMoreDataView();
                    }

                    SnackbarManager.show(Snackbar.with(MainActivity.this).text("已经没有更多了"));


            }
        }
    }

}
