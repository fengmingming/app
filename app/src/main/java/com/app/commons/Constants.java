package com.app.commons;

/**
 *
 */
public class Constants {
    public static final String KEY = "app-token";
    public static final String HOST = "http://m.365020.com/app-client";
    public static final String URL_INDEX_FLOOR = HOST + "/goods/getOEMAjax.htm";
    public static final String URL_IMAGE = "http://img.365020.com";
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
    public static final String URL_CART_CHANGESTATUS = HOST + "/carts/changeStatus.htm";
    public static final String URL_CART_CHANGENUMBER = HOST + "/carts/changeNumber.htm";
    public static final String URL_CART_DELETEPRODUCTS = HOST + "/carts/deleteProducts.htm";
    public static final String URL_CART_AMOUNT = HOST + "/carts/amount.htm";
    public static final String URL_LOGIN = HOST + "/user/loginValidate.htm";
    public static final String URL_ISNOTLOGIN = HOST + "/user/isUserLogin.htm";

    //图片验证码url
    public static final String URL_REQIMGCODE = HOST + "/img/validate.htm";
    public static final String URL_SENDMOBILECODE = HOST + "/user/sendMobileCode.htm";
    public static final String URL_REGISTER = HOST + "/user/register.htm";
    public static final String URL_PCENTER = HOST + "/pcenter/index.htm";
    //地址
    public static final String URL_ADDRESS = HOST + "/pcenter/address/index.htm";
    public static final String URL_ADDRESS_SETDEFAULT = HOST + "/address/setDefaultAddress.htm";
    public static final String URL_ADDRESS_DELETE = HOST + "/address/deleteAddress.htm";
    public static final String URL_AREA = HOST + "/address/getComboboxByPId.htm";
    public static final String URL_ADDORUPDAREA = HOST + "/address/saveOrUpdateAddress.htm";
    public static final String URL_ADDRESS_ID = HOST + "/address/getUserAddress.htm";
    //提交订单
    public static final String URL_COMMITORDER = HOST + "/order/commitOrder.htm";
    //订单列表
    public static final String URL_ORDERLIST = HOST + "/pcenter/orders/list.htm";
    public static final String URL_ODETAIL =  HOST + "/pcenter/orders/detail.htm";

    public static final String URL_ODETELE = HOST + "/pcenter/orders/delete.htm";
    public static final String URL_MYCOUPON = HOST + "/pcenter/coupon/myCoupon.htm";
    public static final String URL_COUPONCODE = HOST + "/pcenter/coupon/code.htm";
    public static final String URL_PAYPASS = HOST + "/user/setPayPass.htm";
    public static final String URL_BINGDMOBILE = HOST + "/user/bindmobile.htm";
    public static final String URL_ISEXISTMOBILE = HOST + "/user/isExistMobile.htm";
    public static final String URL_SENDMCTOUM = HOST + "/user/sendMCToUM.htm";
    /**
     * 退出登录
     */
    public static final String URL_LOGOUT = HOST + "/user/logout.htm";

    public static final String URL_RESETPASS = HOST + "/user/resetPwd.htm";
}
