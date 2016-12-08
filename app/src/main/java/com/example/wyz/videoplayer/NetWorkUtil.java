package com.example.wyz.videoplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/12/6.
 */
public class NetWorkUtil {
    public static List<VideoBean> StringToVideoBean(JSONObject data){
        List<VideoBean> videoBeanList=new ArrayList<>();
        int size=data.names().length();
        for(int i=0;i<size;i++){
            try
            {
                JSONArray videoArray=data.getJSONArray(data.names().get(i).toString());
                for(int j=0;j<videoArray.length();j++){
                    JSONObject videoObject=(JSONObject) videoArray.get(j);
                    VideoBean videoBean=new VideoBean();
                    videoBean.setTopicImg(videoObject.getString("topicImg"));
                    videoBean.setVideosource(videoObject.getString("videosource"));
                    videoBean.setMp4Hd_url(videoObject.getString("mp4Hd_url"));
                    videoBean.setTopicDesc(videoObject.getString("topicDesc"));
                    videoBean.setCover(videoObject.getString("cover"));
                    videoBean.setTitle(videoObject.getString("title"));
                    videoBean.setPlayCount(videoObject.getInt("playCount"));
                    videoBean.setReplyBoard(videoObject.getString("replyBoard"));
                    videoBean.setSectiontitle(videoObject.getString("description"));
                    videoBean.setMp4_url(videoObject.getString("mp4_url"));
                    videoBean.setLength(videoObject.getInt("length"));
                    videoBean.setPlayersize(videoObject.getInt("playersize"));
                    videoBean.setM3u8Hd_url(videoObject.getString("m3u8Hd_url"));
                    videoBean.setVid(videoObject.getString("vid"));
                    videoBean.setM3u8_url(videoObject.getString("m3u8_url"));
                    videoBean.setPtime(videoObject.getString("ptime"));
                    videoBean.setTopicName(videoObject.getString("topicName"));
                    videoBeanList.add(videoBean);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
       return   videoBeanList;
    }
}
