package com.app.commons;

import android.widget.ImageView;

import com.app.MainActivity;
import com.app.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by sls-30 on 2015/7/10.
 */
public class Utils {

    private static ImageLoader loader = ImageLoader.getInstance();

    public static void ayncLoadInternetImageView(final ImageView iv,String url){
        loader.displayImage(url,iv);
    }

}
