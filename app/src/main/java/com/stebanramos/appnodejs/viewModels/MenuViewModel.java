package com.stebanramos.appnodejs.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.Preferences;
import com.stebanramos.appnodejs.utilies.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuViewModel extends ViewModel {

    private final String TAG = "MenuViewModel";

    private RequestQueue mQueue;
    private List<Producto> productoList;
    private MutableLiveData<List<Producto>> muProductList;

    public LiveData<List<Producto>> getData(Context context) {
        Log.i(TAG, "getData()");

        if (muProductList == null){
            muProductList = new MutableLiveData<>();
            loadData(context);
        }
        return muProductList;
    }

    private void loadData(Context context) {
        Log.i(TAG, "loadData()");

        try {
            productoList = new ArrayList<>();

            VolleySingleton.getInstance(context).addToRequestQueue(

                    new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:3000/productos/FindAll", null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i(TAG, "loadData() onResponse " + response);
                                        JSONArray jsonArray = response.getJSONArray("Productos");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            String nombre = jsonObject.get("nombre").toString();
                                            String SKU = jsonObject.get("SKU").toString();
                                            String id = jsonObject.get("_id").toString();

                                            productoList.add(new Producto(nombre, "", "", id));
                                        }

                                        muProductList.setValue(productoList);


                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Log.i(TAG, "- getHeaders");
                            Map<String, String> params = new HashMap<>();
                            params.put("authorization", Preferences.get_str(context, "auth_token"));
                            return params;
                        }
                    }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
