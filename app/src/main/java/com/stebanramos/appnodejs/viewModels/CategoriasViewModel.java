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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stebanramos.appnodejs.models.Categoria;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.Preferences;
import com.stebanramos.appnodejs.utilies.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriasViewModel extends ViewModel {

    private final String TAG = "CategoriasViewModel";

    private RequestQueue mQueue;
    private List<Categoria> categoriaList;
    private MutableLiveData<List<Categoria>> muCategoriaList;

    public LiveData<List<Categoria>> getData(Context context) {
        Log.i(TAG, "getData()");

        if (muCategoriaList == null) {
            muCategoriaList = new MutableLiveData<>();
            loadData(context);
        }
        return muCategoriaList;
    }

    private void loadData(Context context) {
        Log.i(TAG, "loadData()");

        try {
            categoriaList = new ArrayList<>();

            VolleySingleton.getInstance(context).addToRequestQueue(

                    new JsonObjectRequest(Request.Method.GET, "http://10.0.2.2:3000/categorias/FindAll", null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i(TAG, "loadData() onResponse " + response);
                                        JSONArray jsonArray = response.getJSONArray("Categorias");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                                            String id = jsonObject.get("_id").toString();
                                            String nombre = jsonObject.get("nombre").toString();

                                            categoriaList.add(new Categoria(id, nombre));
                                        }

                                        muCategoriaList.setValue(categoriaList);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }) {
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

    public void addData(Context context, String id, String nombre) {
        Log.i(TAG, "addData()");
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", id);
            jsonBody.put("nombre", nombre);
            final String requestBody = jsonBody.toString();

            VolleySingleton.getInstance(context).addToRequestQueue(

                    new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:3000/categorias/Add", null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i(TAG, "addData() onResponse " + response);

                                        loadData(context);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            Log.i(TAG, "- getBody requestBody " + requestBody);

                            return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                        }

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
