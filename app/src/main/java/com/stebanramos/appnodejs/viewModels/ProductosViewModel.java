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
import com.android.volley.toolbox.JsonObjectRequest;
import com.stebanramos.appnodejs.models.Categoria;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.Preferences;
import com.stebanramos.appnodejs.utilies.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductosViewModel extends ViewModel {
    private final String TAG = "ProductosViewModel";

    private RequestQueue mQueue;
    private List<Producto> productoList;
    private MutableLiveData<List<Producto>> muProductList;
    private List<Categoria> categoriaList;
    private MutableLiveData<List<Categoria>> muCategoriaList;

    public LiveData<List<Categoria>> getDataCategorias(Context context) {
        Log.i(TAG, "getData()");

        if (muCategoriaList == null) {
            muCategoriaList = new MutableLiveData<>();
            getCategorias(context);
        }
        return muCategoriaList;
    }

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

    private void getCategorias(Context context) {
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

    public void addData(Context context, String nombre, String sku, String categoria_id) {
        Log.i(TAG, "addData()");
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nombre", nombre);
            jsonBody.put("SKU", sku);
            jsonBody.put("categoria", categoria_id);
            final String requestBody = jsonBody.toString();

            VolleySingleton.getInstance(context).addToRequestQueue(

                    new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:3000/productos/Add", null,

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

    public void deleteData(Context context, String producto_id) {
        Log.i(TAG, "deleteData()");
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", producto_id);
            final String requestBody = jsonBody.toString();
            Log.i(TAG, "deleteData() requestBody " + requestBody);

            VolleySingleton.getInstance(context).addToRequestQueue(

                    new JsonObjectRequest(Request.Method.DELETE, "http://10.0.2.2:3000/productos/Delete", null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i(TAG, "deleteData() onResponse " + response);

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
