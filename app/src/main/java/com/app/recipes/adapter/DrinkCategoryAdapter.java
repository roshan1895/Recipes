package com.app.recipes.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.recipes.R;
import com.app.recipes.model.drink.cat.Drink;
import com.app.recipes.model.food.cat.Category;
import com.app.recipes.utils.Constants;

import java.util.List;

public class DrinkCategoryAdapter  extends RecyclerView.Adapter<DrinkCategoryAdapter.MyViewHolder> {
     Context context;
     List<Drink> drinkList;
    int index=0;
    String catname;
    public interface OnItemClickListener
    {
        void onItemCLick(int pos,String name);
    }
   OnItemClickListener onItemClickListener;

    public  DrinkCategoryAdapter(Context context,List<Drink> drinkList,OnItemClickListener onItemClickListener)
     {
         this.context=context;
         this.drinkList=drinkList;
         this.onItemClickListener=onItemClickListener;
     }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item_layout,parent,false);
         return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cat_tv.setText(drinkList.get(position).getStrCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
                catname=drinkList.get(position).getStrCategory().replace(' ','_');
               onItemClickListener.onItemCLick(position,catname);
            }
        });
        if(index==position)
        {
            holder.cat_rel.setBackgroundResource(R.drawable.tab_selected);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cat_tv.setTextColor(context.getColor(R.color.white));
            }


        }
        else
        {
            holder.cat_rel.setBackgroundResource(R.drawable.tab_unselected);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.cat_tv.setTextColor(context.getColor(R.color.text_color));
            }        }
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
    public Drink getSelected(){
        if (index ==0) {
            return drinkList.get(index);
        }
        return null;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout cat_rel;
        TextView cat_tv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_rel=itemView.findViewById(R.id.cat_layout);
            cat_tv=itemView.findViewById(R.id.cat_tv);
        }
    }
}
