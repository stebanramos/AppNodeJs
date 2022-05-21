package com.stebanramos.appnodejs.utilies;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private final String TAG = "VolleySingleton";
    private static VolleySingleton mInstance;
    private ImageLoader imageLoader;
    private static Context context;
    private RequestQueue mRequestQueue;

    //by making this constructor private only this class can access it
    private VolleySingleton(Context context) {
        Log.d(TAG, " VolleySingleton()");
        mInstance.context = context;
        //we make the context for the whole life time of the application not just a single activity
        mRequestQueue = getRequestQueue();

        imageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

    }

    public static synchronized VolleySingleton getInstance(Context context){

        if (mInstance == null){
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        Log.i(TAG, "getRequestQueue()");

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }

}
