package com.app.commons;

import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Map;

public class Utils {

    private static ImageLoader loader = ImageLoader.getInstance();
    private static ThreadLocal<AsyncHttpClient> httpLocal = new ThreadLocal<AsyncHttpClient>();

    public static void asyncLoadInternetImageView(final ImageView iv,String url){
        loader.displayImage(url,iv);
    }

    public static void asyncHttpRequest(String url, Map<String,Object> param, ResponseHandlerInterface resHandler, String method){
        RequestParams rp = new RequestParams();
        rp.setContentEncoding("utf-8");
        if(param != null){
            Object value = null;
            for(String key:param.keySet()){
                value = param.get(key);
                if(key !=null && value != null){
                    rp.add(key, value.toString());
                }
            }
        }
        if("get".equalsIgnoreCase(method)){
            getAsyncHttpClient().get(url, rp, resHandler);
        }else{
            getAsyncHttpClient().post(url, rp, resHandler);
        }
    }

    public static void asyncHttpRequestGet(String url, Map<String,Object> param, ResponseHandlerInterface resHandler){
        asyncHttpRequest(url, param, resHandler, "get");
    }

    public static void asyncHttpRequestPost(String url, Map<String,Object> param, ResponseHandlerInterface resHandler){
        asyncHttpRequest(url, param, resHandler, "post");
    }

    private static AsyncHttpClient getAsyncHttpClient(){
        AsyncHttpClient client = httpLocal.get();
        if(client == null){
            client = new AsyncHttpClient();
            httpLocal.set(client);
        }
        return client;
    }

}
