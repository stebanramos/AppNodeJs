package com.stebanramos.appnodejs.utilies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stebanramos.appnodejs.databinding.ProductCardBinding;
import com.stebanramos.appnodejs.models.Categoria;
import com.stebanramos.appnodejs.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder>{

    private final String TAG = "CategoriasAdapter";

    private List<Categoria> dataSet;
    private Context context;
    private static ClickListener mClickListener;
    private ArrayList<Categoria> arraylist;

    public CategoriasAdapter(List<Categoria> dataSet, Context context) {
        Log.i(TAG, "CategoriasAdapter() ");
        this.dataSet = dataSet;
        this.context = context;
        this.arraylist = new ArrayList<Categoria>();
        this.arraylist.addAll(dataSet);
    }

    @NonNull
    @Override
    public CategoriasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductCardBinding binding = ProductCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CategoriasAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String nombre = dataSet.get(position).getNombre();
        Log.i(TAG, "onBindViewHolder() name " + nombre);
        holder.tvProducto.setText(nombre);

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvProducto;

        public ViewHolder(@NonNull ProductCardBinding binding) {
            super(binding.getRoot());

            tvProducto = binding.tvProducto;
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(CategoriasAdapter.ClickListener listener){
        mClickListener = listener;
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
            for (Categoria categoria : arraylist) {
                if (categoria.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataSet.add(categoria);
                }
            }
        }
        notifyDataSetChanged();
    }
}
