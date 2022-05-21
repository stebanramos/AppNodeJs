package com.stebanramos.appnodejs.utilies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferences {

    private static final String TAG = "Preferences";
    private static final String KEY_PREFERENCES = "PREFERENCE_FILE_KEY";

    public static void set_str(Context context, String name, String value){
        Log.i(TAG, "set_str()");

        try{
            SharedPreferences reader = context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = reader.edit();
            editor.putString(name, value);
            editor.apply();

            }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String get_str(Context context, String key){
        Log.i(TAG, "get_str()");

        String result = "";
        try {
            SharedPreferences reader = context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
            result = reader.getString(key,"");
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
