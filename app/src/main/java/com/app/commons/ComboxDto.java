package com.app.commons;

/**
 * Created by developserver on 2015/7/30.
 */
public class ComboxDto {

    private String key;
    private String value;
    public ComboxDto(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }
}
