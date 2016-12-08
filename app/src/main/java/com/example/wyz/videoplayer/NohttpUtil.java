package com.example.wyz.videoplayer;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

/**
 * Created by Wyz on 2016/12/6.
 */
public class NohttpUtil {
    public static JSONObject getRequest(String url) {
        try {
            Request request = NoHttp.createJsonObjectRequest(url, RequestMethod.GET);
            Response response = NoHttp.startRequestSync(request);

            if (response.isSucceed()) {
                //((JSONObject) ((RestResponse) response).result).get("V9LG4CHOR")
                return new JSONObject(response.get().toString());
                //((JSONObject) ((RestResponse) response).result).get("V9LG4CHOR");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
