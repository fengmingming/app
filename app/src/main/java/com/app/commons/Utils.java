package com.app.commons;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.*;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Map;

public class Utils {

    private static ImageLoader loader = ImageLoader.getInstance();
    private static ThreadLocal<AsyncHttpClient> httpLocal = new ThreadLocal<AsyncHttpClient>();

    public static void asyncLoadInternetImageView(final ImageView iv,String url){
        loader.displayImage(url,iv);
    }

    public static void asyncHttpRequest(String url, Map<String,Object> param, final JsonHttpResponseHandler resHandler, String method){
        RequestParams rp = new RequestParams();
        rp.setContentEncoding("utf-8");
        rp.put(Constants.KEY, Installation.id());
        if(param != null){
            Object value = null;
            for(String key:param.keySet()){
                value = param.get(key);
                if(key !=null && value != null){
                    rp.put(key, value.toString());
                }
            }
        }
        if("get".equalsIgnoreCase(method)){
            getAsyncHttpClient().get(url, rp, resHandler);
        }else{
            getAsyncHttpClient().post(url, rp, resHandler);
        }
    }

    public static void asyncHttpRequestGet(String url, Map<String,Object> param, JsonHttpResponseHandler resHandler){
        asyncHttpRequest(url, param, resHandler, "get");
    }

    public static void asyncHttpRequestPost(String url, Map<String,Object> param, JsonHttpResponseHandler resHandler){
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

    public static boolean isValid(Object obj){
        if(obj != null && !"".equals(obj)){
            return true;
        }
        return false;
    }

    /**
     * webview使用url的时候使用，通过asynchttpclient请求不需要wrapUrl
     */
    public static String wrapUrl(String url){
        if(url != null){
            if(url.contains("?")){
                url = url + "&" + Constants.KEY + "=" + Installation.id();
            }else{
                url = url + "?" + Constants.KEY + "=" + Installation.id();
            }
        }
        return url;
    }

    public static String md5(String str){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return toHex(md.digest()).toUpperCase();
        }catch(Exception e){
            Log.e("md5",e.getMessage(),e);
        }
        return str;
    }

    private static String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}
