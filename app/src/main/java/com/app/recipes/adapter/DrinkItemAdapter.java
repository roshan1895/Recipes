package com.app.recipes.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.recipes.R;
import com.app.recipes.model.Ingredient;
import com.app.recipes.model.drink.drink.Drink;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class DrinkItemAdapter extends RecyclerView.Adapter<DrinkItemAdapter.MyViewHolder> {
    Context context;
    List<Drink> drinkList;
    public interface OnItemClickListener
    {
        void OnDrinkClick(int pos,String id,String name);
    }
    OnItemClickListener onItemClickListener;
    public  DrinkItemAdapter(Context context,List<Drink> drinkList,OnItemClickListener onItemClickListener)
    {
        this.context=context;
        this.drinkList=drinkList;
        this.onItemClickListener=onItemClickListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.meals_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.meal_title.setText(drinkList.get(position).getStrDrink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Glide.with(context).load(drinkList.get(position).getStrDrinkThumb()).placeholder(new ColorDrawable(context.getColor(R.color.green_light))).error(new ColorDrawable(context.getColor(R.color.green_light))).fallback(new ColorDrawable(context.getColor(R.color.green_light))).into(holder.meal_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.OnDrinkClick(position,drinkList.get(position).getIdDrink(),drinkList.get(position).getStrDrink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView meal_image;
        TextView meal_title;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            meal_image=itemView.findViewById(R.id.meal_image);
            meal_title=itemView.findViewById(R.id.meal_title);
        }
    }
    public void remove(Drink r) {
        int position = drinkList.indexOf(r);
        if (position > -1) {
            drinkList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Drink getItem(int position) {
        return drinkList.get(position);
    }
    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

}
