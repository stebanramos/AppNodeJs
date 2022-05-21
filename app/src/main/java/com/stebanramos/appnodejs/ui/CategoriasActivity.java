package com.stebanramos.appnodejs.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.stebanramos.appnodejs.R;
import com.stebanramos.appnodejs.databinding.ActivityCategoriasBinding;
import com.stebanramos.appnodejs.models.Categoria;
import com.stebanramos.appnodejs.models.Producto;
import com.stebanramos.appnodejs.utilies.CategoriasAdapter;
import com.stebanramos.appnodejs.utilies.ProductosAdapter;
import com.stebanramos.appnodejs.viewModels.CategoriasViewModel;
import com.stebanramos.appnodejs.viewModels.MenuViewModel;

import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    private final String TAG = "CategoriasActivity";

    private ActivityCategoriasBinding binding;
    CategoriasViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i(TAG, "onCreate()");

        initComponents();
        binding.etSearch.clearFocus();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddCategoria();
            }
        });
    }

    private void initComponents() {
        Log.i(TAG, "initComponents()");

        initRecyclerView();

        mainViewModel = ViewModelProviders.of(this).get(CategoriasViewModel.class);

        Observer<List<Categoria>> observer = new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                if (categorias != null && categorias.size() > 0) {
                    binding.progressCircular.setVisibility(View.GONE);
                }

                setupRecyclerView(categorias);
            }
        };

        mainViewModel.getData(this).observe(this, observer);
    }

    private void initRecyclerView() {
        Log.i(TAG, "initRecyclerView()");

        binding.recyclerCategorias.setHasFixedSize(true);
        binding.recyclerCategorias.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView(List<Categoria> categoriaList) {
        Log.i(TAG, "setupRecyclerView() productoList " + categoriaList);

        try {
            CategoriasAdapter categoriasAdapter = new CategoriasAdapter(categoriaList, CategoriasActivity.this);
            binding.recyclerCategorias.setAdapter(categoriasAdapter);

            categoriasAdapter.setOnItemClickListener(new CategoriasAdapter.ClickListener() {
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

                    categoriasAdapter.filter(charSequence.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ocultarTeclado();
        return true;
    }

    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        view.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showDialogAddCategoria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_categoria, null);
        builder.setView(view);

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button btnAceptar = view.findViewById(R.id.btnAceptar);
        EditText nombre = view.findViewById(R.id.etCategoria);
        EditText id = view.findViewById(R.id.etId);

        AlertDialog alertDialog = builder.create();

        btnAceptar.setOnClickListener(view12 -> {

            mainViewModel.addData(CategoriasActivity.this, id.getText().toString(), nombre.getText().toString());
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
}