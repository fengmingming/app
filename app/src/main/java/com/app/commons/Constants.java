package com.app.commons;

/**
 *
 */
public class Constants {
    public static final String KEY = "app-token";
    public static final String HOST = "http://app.365020.com/mobile";
    public static final String URL_INDEX_FLOOR = HOST + "/goods/getOEMAjax.htm";
    public static final String URL_IMAGE = "http://img.app.365020.com";
    public static final String URL_INDEX_LOOP = HOST + "/index.htm";
    /**
     * 分类页面
     * */
    public static final String URL_CATEGORY = HOST + "/category.htm";

    /**
     *
     * */
    public static final String URL_CATEGORY_GOODS_LIST = HOST + "/goods/goodsList.htm";

    /**
     * 详情页地址
     * */
    public static final String URL_GOODS_DETAIL = HOST + "/goods/goodsDetail.htm?id=";
    //加入购物车
    public static final String URL_ADD_CART = HOST + "/carts/addCart.htm";
    //购物车数量
    public static final String URL_CART_NUMBER = HOST + "/carts/getShopCartCount.htm";

    public static final String DETAIL_CONTANT_WRAP = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\"></head><body style=\"text-align: center; background-color: null; vertical-align: middle;\">{0}</body></html>";

    public static final String URL_CATEGORY_JSON = HOST + "/json/category.json";
    /**
     * 搜索地址
     * */
    public static final String URL_SEARCH = HOST + "/goods/goodsListBySearch.htm";
    /**
     *我的购物车
     */
    public static final String URL_CARTINFO = HOST + "/carts/myCarts.htm";
}
