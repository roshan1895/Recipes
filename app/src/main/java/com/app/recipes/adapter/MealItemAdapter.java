package com.app.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.recipes.R;
import com.app.recipes.model.meals.Meal;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MealItemAdapter extends RecyclerView.Adapter<MealItemAdapter.MyViewHolder> {
     Context context;
     List<Meal> mealList;
    public interface OnItemClickListener
    {
        void OnItemClick(int pos,String id,String name);
    }
    OnItemClickListener onItemClickListener;
    public MealItemAdapter(Context context,List<Meal> mealList,OnItemClickListener onItemClickListener)
     {
         this.context=context;
         this.mealList=mealList;
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
         holder.meal_title.setText(mealList.get(position).getStrMeal());
        Glide.with(context).load(mealList.get(position).getStrMealThumb()).into(holder.meal_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.OnItemClick(position,mealList.get(position).getIdMeal(),mealList.get(position).getStrMeal());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
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
}
