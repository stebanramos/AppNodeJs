package com.stebanramos.appnodejs.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.stebanramos.appnodejs.R;
import com.stebanramos.appnodejs.databinding.ActivityMenuBinding;
import com.stebanramos.appnodejs.databinding.ActivityProductosBinding;
import com.stebanramos.appnodejs.models.Categoria;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.ProductosAdapter;
import com.stebanramos.appnodejs.viewModels.MenuViewModel;
import com.stebanramos.appnodejs.viewModels.ProductosViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private final String TAG = "MenuActivity";
    private ActivityProductosBinding binding;
    ProductosViewModel productosViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductosBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i(TAG, "onCreate()");

        initComponents();
        binding.etSearch.clearFocus();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddProducto();
            }
        });
    }

    private void initComponents() {
        Log.i(TAG, "initComponents()");

        initRecyclerView();

        productosViewModel = ViewModelProviders.of(this).get(ProductosViewModel.class);

        Observer<List<Producto>> observer = new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> products) {
                if (products != null && products.size() > 0) {
                    binding.progressCircular.setVisibility(View.GONE);
                }

                setupRecyclerView(products);
            }
        };

        productosViewModel.getData(this).observe(this, observer);
    }

    private void initRecyclerView() {
        Log.i(TAG, "initRecyclerView()");

        binding.recyclerProducts.setHasFixedSize(true);
        binding.recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView(List<Producto> productoList) {
        Log.i(TAG, "setupRecyclerView() productoList " + productoList);

        try {
            ProductosAdapter productosAdapter = new ProductosAdapter(productoList, ProductosActivity.this, "admin");
            binding.recyclerProducts.setAdapter(productosAdapter);

            productosAdapter.setOnItemClickListener(new ProductosAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Log.i(TAG, "setupRecyclerView() view " + view.getContentDescription());
                    String tag = view.getContentDescription().toString();

                    if(tag.equals("Add")){

                    }

                    if(tag.equals("Edit")){

                    }

                    if(tag.equals("Delete")){
                        String producto_id = productoList.get(position).getId();
                        productosViewModel.deleteData(ProductosActivity.this, producto_id);

                    }
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showDialogAddProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_producto, null);
        builder.setView(view);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        EditText nombre = view.findViewById(R.id.etNombre);
        EditText etSku = view.findViewById(R.id.etSku);
        AutoCompleteTextView categorias = view.findViewById(R.id.listCategoria);

        AlertDialog alertDialog = builder.create();

        ArrayList<Categoria> arraylistCategorias = new ArrayList<>();

        Observer<List<Categoria>> observer = new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categoriaList) {
                if (categoriaList != null && categoriaList.size() > 0) {
                    arraylistCategorias.addAll(categoriaList);
                    List<String> items = new ArrayList<>();
                    for (int i = 0; i < categoriaList.size(); i++) {
                        items.add(categoriaList.get(i).getNombre());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProductosActivity.this,
                            android.R.layout.simple_dropdown_item_1line, items);

                    categorias.setAdapter(adapter);

                }

            }
        };

        productosViewModel.getDataCategorias(this).observe(this, observer);
        final String[] categoria_id = {""};
        categorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "showDialogAddProducto() arraylistCategorias " + adapterView.getItemAtPosition(i));

                for (int j = 0; j < arraylistCategorias.size(); j++) {

                    if (adapterView.getItemAtPosition(i).equals(arraylistCategorias.get(j).getNombre())) {
                        categoria_id[0] = arraylistCategorias.get(j).getId();
                    }
                }
            }
        });
        ;
        btnAceptar.setOnClickListener(view12 -> {

            productosViewModel.addData(ProductosActivity.this, nombre.getText().toString(), etSku.getText().toString(), categoria_id[0]);
            alertDialog.dismiss();
            ocultarTeclado();
        });

        btnCancelar.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            ocultarTeclado();

        });


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
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