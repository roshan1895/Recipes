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
import com.app.recipes.model.categories.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    List<Category> categoryList;
    int index=0;
    public CategoryAdapter(Context context,List<Category> categoryList)
    {
        this.context=context;
        this.categoryList=categoryList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.category_item_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.cat_tv.setText(categoryList.get(position).getStrCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();

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
        return categoryList.size();
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
