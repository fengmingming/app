package com.app.interfaces;

import com.app.view.GoodsView;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developserver on 2015/7/16.
 */
public interface IGoodsCom {

    public static class Config{
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, Object> getParam() {
            return param;
        }

        public void setParam(Map<String, Object> param) {
            this.param = param;
        }

        private Map<String, Object> param = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    public interface ParseJsonToGoods{
        public List<GoodsView.Goods> parse(JSONObject jsonObject) throws Exception;
    }

    public void setConfig(Config config);

    public Config getConfig();

    public void builder();

    public boolean isLoading();

    public void reset();

    public void parseJsonToGoods(ParseJsonToGoods parseJsonToGoods);
}
