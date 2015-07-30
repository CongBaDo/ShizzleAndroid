package com.shizzelandroid;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by congba on 7/30/15.
 */
public class TestApplication extends Application {

    private static final String TAG = "MTApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.bg_noimage)
                // EDIT HERE
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer()).cacheInMemory(true)
                .cacheOnDisk(true).imageScaleType(ImageScaleType.NONE)
                .handler(new Handler()).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(options).threadPoolSize(7)
                .threadPriority(Thread.NORM_PRIORITY - 1).build();
        ImageLoader.getInstance().init(config);
    }
}
