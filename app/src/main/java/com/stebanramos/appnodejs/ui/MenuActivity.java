package com.stebanramos.appnodejs.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.stebanramos.appnodejs.databinding.ActivityMenuBinding;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.ProductosAdapter;
import com.stebanramos.appnodejs.viewModels.MenuViewModel;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private final String TAG = "MenuActivity";
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i(TAG, "onCreate()");

        initComponents();
        binding.etSearch.clearFocus();

    }

    private void initComponents(){
        Log.i(TAG, "initComponents()");

        initRecyclerView();

        MenuViewModel mainViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);

        Observer<List<Producto>> observer = new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> products) {
                if (products != null && products.size() > 0){
                    binding.progressCircular.setVisibility(View.GONE);
                }

                setupRecyclerView(products);
            }
        };

        mainViewModel.getData(this).observe(this, observer);
    }

    private void initRecyclerView(){
        Log.i(TAG, "initRecyclerView()");

        binding.recyclerProducts.setHasFixedSize(true);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView(List<Producto> productoList){
        Log.i(TAG, "setupRecyclerView() productoList " + productoList);

        try {
            ProductosAdapter productosAdapter = new ProductosAdapter(productoList,MenuActivity.this, "user");
            binding.recyclerProducts.setAdapter(productosAdapter);

            productosAdapter.setOnItemClickListener(new ProductosAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    /*Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
                    intent.putExtra("country_name", productoList.get(position).getNombre());
                    startActivity(intent);*/
                }
            });

            binding.etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    productosAdapter.filter(charSequence.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ocultarTeclado();
        return true;
    }

    private void ocultarTeclado() {
        View vieww = getCurrentFocus();

        if (vieww != null) {
            vieww.clearFocus(); //*Agregar!
            InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(vieww.getWindowToken(), 0);
        }
    }

}