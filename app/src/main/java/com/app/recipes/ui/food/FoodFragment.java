package com.app.recipes.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.recipes.R;
import com.app.recipes.adapter.CategoryAdapter;
import com.app.recipes.adapter.MealItemAdapter;
import com.app.recipes.model.categories.AllCategoryResponse;
import com.app.recipes.model.categories.Category;
import com.app.recipes.model.meals.AllMealsResponse;
import com.app.recipes.model.meals.Meal;
import com.app.recipes.ui.MealDetailsActivity;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.UrlRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodFragment extends Fragment implements MealItemAdapter.OnItemClickListener {
    List<Category> categoryList;
    CategoryAdapter adapter;
    RecyclerView rvCat,rvMeal;
    List<Meal> mealList;
    MealItemAdapter mealItemAdapter;
    RelativeLayout emptyview;
    RelativeLayout loader;
    LottieAnimationView animationView;
    RelativeLayout errorLay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRV();
    }
    void initView()
    {
        emptyview=getView().findViewById(R.id.emptyview);
        loader=getView().findViewById(R.id.loader);
        animationView=getView().findViewById(R.id.animationView);
        errorLay=getView().findViewById(R.id.error_lay);


    }
    void setupRV()
    {   categoryList=new ArrayList<>();
        mealList=new ArrayList<>();
        rvCat=getView().findViewById(R.id.rv_cat);
        rvCat.hasFixedSize();
        rvCat.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        loadCategory();

        rvMeal=getView().findViewById(R.id.rv_meal);
        rvMeal.hasFixedSize();
        rvMeal.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadMeals();
    }
    void  loadCategory()
    {
        Call<AllCategoryResponse>call= ApInterface.getClient().create(UrlRequest.class).allCategory();
        call.enqueue(new Callback<AllCategoryResponse>() {
            @Override
            public void onResponse(Call<AllCategoryResponse> call, Response<AllCategoryResponse> response) {
                if(response.isSuccessful())
                {AllCategoryResponse allCategoryResponse=response.body();
                    categoryList= allCategoryResponse.getCategories();
                    adapter=new CategoryAdapter(getActivity(),categoryList);
                    rvCat.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<AllCategoryResponse> call, Throwable t) {
                Log.e("food_failure",t.getMessage()+"");
            }
        });

    }
    void loadMeals()
    {
        Call<AllMealsResponse> call=ApInterface.getClient().create(UrlRequest.class).allMeal("Seafood");
        call.enqueue(new Callback<AllMealsResponse>() {
            @Override
            public void onResponse(Call<AllMealsResponse> call, Response<AllMealsResponse> response) {
                if(response.isSuccessful())
                {  AllMealsResponse allMealsResponse=response.body();
                    mealList=allMealsResponse.getMeals();
                    mealItemAdapter=new MealItemAdapter(getActivity(),mealList,FoodFragment.this);
                    rvMeal.setAdapter(mealItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<AllMealsResponse> call, Throwable t) {
               Log.e("meal_response",t.getMessage()+"");
            }
        });
    }

    @Override
    public void OnItemClick(int pos, String id, String name) {
        Intent intent=new Intent(getActivity(), MealDetailsActivity.class);
        startActivity(intent);
    }
}
