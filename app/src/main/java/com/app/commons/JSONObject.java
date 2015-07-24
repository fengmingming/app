package com.app.commons;

import android.util.Log;

/**
 * Created by developserver on 2015/7/20.
 */
public class JSONObject {

    private org.json.JSONObject jo;

    public JSONObject(org.json.JSONObject jo){
        if(jo == null){
            throw new RuntimeException("json object is null");
        }
        this.jo = jo;
    }

    public JSONObject getJSONObject(String name){
        try{
            return new JSONObject(this.jo.getJSONObject(name));
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public JSONArray getJSONArray(String name){
        try{
            return new JSONArray(this.jo.getJSONArray(name));
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public String getString(String name){
        try{
            String str = this.jo.getString(name);
            if("null".equalsIgnoreCase(str)){
                return null;
            }
            return str;
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }

    public Boolean getBoolean(String name){
        try{
            return this.jo.getBoolean(name);
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }

    public Double getDouble(String name){
        try{
            return this.jo.getDouble(name);
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }

    public Integer getInt(String name){
        try{
            return this.jo.getInt(name);
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }

    public Long getLong(String name){
        try{
            return this.jo.getLong(name);
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }

    public Object get(String name){
        try{
            return this.jo.get(name);
        }catch(Exception e){
            Log.e("json tools",e.getMessage(),e);
        }
        return null;
    }
}
