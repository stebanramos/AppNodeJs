package com.stebanramos.appnodejs.utilies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stebanramos.appnodejs.databinding.ProductCardBinding;
import com.stebanramos.appnodejs.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder>{

    private final String TAG = "CountriesAdapter";

    private List<Producto> dataSet;
    private Context context;
    private static ClickListener mClickListener;
    private ArrayList<Producto> arraylist;
    private String rol;

    public ProductosAdapter(List<Producto> dataSet, Context context, String rol) {
        Log.i(TAG, "ProductosAdapter() ");
        this.dataSet = dataSet;
        this.context = context;
        this.arraylist = new ArrayList<Producto>();
        this.arraylist.addAll(dataSet);
        this.rol = rol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductCardBinding binding = ProductCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String nombre = dataSet.get(position).getNombre();
        Log.i(TAG, "onBindViewHolder() name " + nombre);
        holder.tvProducto.setText(nombre);

        if(rol.equals("user")){
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);

        }

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvProducto;
        private Button btnAdd, btnEdit, btnDelete;

        public ViewHolder(@NonNull ProductCardBinding binding) {
            super(binding.getRoot());

            tvProducto = binding.tvProducto;
            btnAdd =binding.btnAdd;
            btnEdit = binding.btnEdit;
            btnDelete = binding.btnDelete;

            btnAdd.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener listener){
        ProductosAdapter.mClickListener = listener;
    }

    public interface ClickListener {
        void onItemClick(int position, View view);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSet.clear();
        if (charText.length() == 0) {
            dataSet.addAll(arraylist);
        } else {
            for (Producto producto : arraylist) {
                if (producto.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataSet.add(producto);
                }
            }
        }
        notifyDataSetChanged();
    }
}
