package com.example.wyz.videoplayer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Wyz on 2016/12/7.
 */
public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;
    //已经加载出来的Item数量
    private  int totalItemCount;
    //当前页，从0开始
    private  int currentPage=0;
    //存储上一个totalItemCount
    private int previousTotal=0;
    //在屏幕可见的item数量
    private int visibleItemCount;
    //在屏幕上可见的item中的第一个
    private int firstVisibleItem;
    //是否正在上拉数据
    private  boolean loading=true;

    public  EndLessOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager=linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount=recyclerView.getChildCount();
        totalItemCount=mLinearLayoutManager.getItemCount();
        firstVisibleItem=mLinearLayoutManager.findFirstVisibleItemPosition();
        if(loading){
            if(totalItemCount>previousTotal){
                //说明数据已经加载结束
                loading=false;
                previousTotal=totalItemCount;
            }
        }
        if(!loading&&totalItemCount-visibleItemCount<=firstVisibleItem){
            currentPage++;
            onLoadMore(currentPage);
            loading=true;
        }
    }
    //加载更多数据接口,在Activity中的recyclerView中监听并实现这个方法
    public abstract  void onLoadMore(int currentPage);
}
