package com.app.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.recipes.R;
import com.app.recipes.model.Ingredient;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    Context context;
    List<Ingredient> ingredientList;
    public IngredientsAdapter(Context context,List<Ingredient> ingredientList)
    {
        this.context=context;
        this.ingredientList=ingredientList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.ingredients_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.ingr_name.setText(ingredientList.get(position).getName());
        Glide.with(context).load(ingredientList.get(position).getImage()).into(holder.ingr_img);
        holder.ingr_qty.setText(ingredientList.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ingr_img;
        TextView ingr_name,ingr_qty;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingr_img=itemView.findViewById(R.id.ingr_image);
            ingr_name=itemView.findViewById(R.id.ingr_title);
            ingr_qty=itemView.findViewById(R.id.qty_tv);
        }
    }
}
