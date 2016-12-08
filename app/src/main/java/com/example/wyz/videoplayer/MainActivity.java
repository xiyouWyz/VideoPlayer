package com.example.wyz.videoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.yolanda.nohttp.NoHttp;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<VideoBean> videoBeenList;
    RecyclerAdapter mRecyclerAdapter;
    MyHandler mMyHandler=new MyHandler();
    int mCurrentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyThread myThread=new MyThread();
        myThread.start();

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
                mCurrentPage=currentPage;
                new Thread(){
                    @Override
                    public void run() {
                        loadMoreData(mCurrentPage);
                    }
                }.start();

            }
        });
        
        setFooterView(mRecyclerView);
    }

    private void setFooterView(RecyclerView recyclerView) {
        View footer= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_footer,recyclerView,false);
        mRecyclerAdapter.setFooterView(footer);


    }

    private void loadData() {
        NoHttp.initialize(this);
        JSONObject jsonObject=NohttpUtil.getRequest("http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/0-10.html");
        videoBeenList=NetWorkUtil.StringToVideoBean(jsonObject);
        mMyHandler.sendEmptyMessage(1);
    }
    private void loadMoreData(int currentPage) {
        int startIndex=currentPage*10;
        String url="http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/"+startIndex+"-10.html";
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
            loadData();
        }
    }
    public class  MyHandler extends  Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case  2:
                    //initRecyclerView();
                    mRecyclerAdapter.notifyDataSetChanged();
                    break;
                case  1:
                    initRecyclerView();
                    break;
            }
        }
    }

}
