package com.example.wyz.videoplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.videoplayer.JCVideoPlayer;
import com.example.videoplayer.JCVideoPlayerStandard;

import java.util.List;

/**
 * Created by Wyz on 2016/12/6.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public  List<VideoBean> itemInfos;
    private Context mContext;
    private LayoutInflater mInflater;

    private  final  int TYPE_ITEM=0;
    private  final int TYPE_FOOTER=1;
    private  View mFooterView;

    public RecyclerAdapter(Context context, List<VideoBean> list) {
        mContext=context;
        itemInfos=list;
        mInflater= LayoutInflater.from(mContext);

    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        if(getItemCount()>0){
            notifyItemInserted(getItemCount()-1);
        }else{
            notifyItemInserted(0);
        }

    }


    public void setNoMoreDataView(){
        if(mFooterView!=null){
            mFooterView.setVisibility(View.GONE);
        }
    }
    public  void setNoNetFooterView(){
        if(mFooterView!=null){
            mFooterView.setVisibility(View.GONE);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           System.out.println("onCreateViewHolder");
        if(viewType==TYPE_FOOTER&&mFooterView!=null){
            return new FooterViewHolder(mInflater.inflate(R.layout.item_footer,parent,false));
        }
        return new ItemViewHolder(mInflater.inflate(R.layout.item_video_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_ITEM){
            if(holder instanceof  ItemViewHolder){
                ((ItemViewHolder)holder).play_time_TextView.setText(String.valueOf(itemInfos.get(position).getPlayCount()));
                ((ItemViewHolder)holder).from_TextView.setText(itemInfos.get(position).getTopicName());
                Glide.with(mContext).load(itemInfos.get(position).getTopicImg())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()
                        .into(((ItemViewHolder)holder).mImageView);
                JCVideoPlayerStandard jcVideoPlayerStandard=((ItemViewHolder)holder).mJCVideoPlayerStandard;
                boolean setUp = jcVideoPlayerStandard.setUp(
                        itemInfos.get(position).getMp4_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        TextUtils.isEmpty(itemInfos.get(position).getDescription())?itemInfos.get(position).getTitle()+"":itemInfos.get(position).getDescription());
                if (setUp) {
                    Glide.with(mContext).load(itemInfos.get(position).getCover())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.ic_launcher)
                            .crossFade().into(jcVideoPlayerStandard.thumbImageView);
                }
            }
        }
        else if(getItemViewType(position)==TYPE_FOOTER){
            if(holder instanceof  FooterViewHolder){
                ((FooterViewHolder)holder).mTextView.setText("正在加载更多....");
                //((FooterViewHolder)holder).mTextView.setVisibility(View.VISIBLE);
            }
        }
    }










    @Override
    public int getItemCount() {
        if(mFooterView==null){
            return itemInfos.size();
        }else{
            return itemInfos.size()+1;
        }
    }



    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return  TYPE_FOOTER;
        }else {
            return  TYPE_ITEM;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private JCVideoPlayerStandard mJCVideoPlayerStandard;
        private ImageView mImageView;
        private  TextView from_TextView;
        private  TextView play_time_TextView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mJCVideoPlayerStandard=(JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
            mImageView=(ImageView)itemView.findViewById(R.id.iv_logo);
            from_TextView=(TextView)itemView.findViewById(R.id.tv_from);
            play_time_TextView=(TextView)itemView.findViewById(R.id.tv_play_time);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private  TextView mTextView;
        public FooterViewHolder(View view) {
            super(view);
            mTextView=(TextView)view.findViewById(R.id.textView);
        }
    }
}
