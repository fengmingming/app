package com.app.remote;

import java.util.Map;

/**
 * Created by on 2015/7/7.
 */
public interface IRemote<T> {
    public class Config{
        private String url;
        private String method;
        private Map param;
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map getParam() {
            return param;
        }

        public void setParam(Map param) {
            this.param = param;
        }

    }

    public class ResBo<T>{
        private boolean success;
        private String errMsg;
        private T result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public T getResult() {
            return result;
        }

        public void setResult(T result) {
            this.result = result;
        }
    }
    public void processUI(ResBo<T> resBo);
}
