package com.app.commons;

import android.util.Log;

/**
 * Created by on 2015/7/20.
 */
public class JSONArray {
    private org.json.JSONArray ja;
    public JSONArray(org.json.JSONArray ja){
        if(ja == null){
            throw new RuntimeException("json array is null");
        }
        this.ja = ja;
    }
    public int length(){
        return this.ja.length();
    }

    public Object get(int i){
        try{
            return this.ja.get(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public Boolean getBoolean(int i){
        try{
            return this.ja.getBoolean(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public Double getDouble(int i){
        try{
            return this.ja.getDouble(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public Integer getInt(int i){
        try{
            return this.ja.getInt(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public Long getLong(int i){
        try{
            return this.ja.getLong(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public String getString(int i){
        try{
            return this.ja.getString(i);
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return "";
    }

    public JSONObject getJSONObject(int i){
        try{
            return new JSONObject(this.ja.getJSONObject(i));
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

    public JSONArray getJSONArray(int i){
        try{
            return new JSONArray(this.ja.getJSONArray(i));
        }catch(Exception e){
            Log.e("json tools", e.getMessage(), e);
        }
        return null;
    }

}
