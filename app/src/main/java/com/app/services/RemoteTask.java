package com.app.services;

import android.os.AsyncTask;

import com.app.remote.IRemote;

import org.apache.http.impl.DefaultHttpServerConnection;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/7.
 */
public class RemoteTask<T> extends AsyncTask<IRemote.Config,Integer,IRemote.ResBo<T>> {

    private IRemote<T> remote;
    public RemoteTask(IRemote<T> remote){
        this.remote = remote;
    }

    @Override
    protected IRemote.ResBo<T> doInBackground(IRemote.Config... params) {
        if(params.length == 0){
            throw new RuntimeException("IRemote.Config is not exist");
        }
        IRemote.Config config = params[0];

        return null;
    }

    @Override
    protected void onPostExecute(IRemote.ResBo<T> resBo) {
        super.onPostExecute(resBo);
        this.remote.processUI(resBo);
    }
}
