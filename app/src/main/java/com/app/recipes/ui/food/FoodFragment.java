package com.app.recipes.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.app.recipes.utils.CommonUtils;
import com.app.recipes.utils.Connection_Check;
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
    Button retryBtn;
    RelativeLayout somethingWentWrongLayout;
    Button erroRetryBttn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }
    void initView()
    {
        emptyview=getView().findViewById(R.id.emptyview);
        loader=getView().findViewById(R.id.loader);
        animationView=getView().findViewById(R.id.animationView);
        errorLay=getView().findViewById(R.id.error_lay);
        retryBtn=getView().findViewById(R.id.retry_btn);
        erroRetryBttn=getView().findViewById(R.id.retry_btn_error);
        somethingWentWrongLayout=getView().findViewById(R.id.something_went_error_layout);
        setupRV();


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
        if(Connection_Check.checkConnection(getActivity()))
        {
            loadMeals();

        }
        else
        {
            CommonUtils.hideLoader(animationView,loader);
            CommonUtils.showConnectionError(errorLay);
        }

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
    {    if(errorLay.getVisibility()==View.VISIBLE)
         {
             CommonUtils.hideConnectionError(errorLay);
         }
         if(somethingWentWrongLayout.getVisibility()==View.VISIBLE)
         {
             CommonUtils.hideWrongError(somethingWentWrongLayout);
         }
        Call<AllMealsResponse> call=ApInterface.getClient().create(UrlRequest.class).allMeal("Seafood");
        call.enqueue(new Callback<AllMealsResponse>() {
            @Override
            public void onResponse(Call<AllMealsResponse> call, Response<AllMealsResponse> response) {
              CommonUtils.hideLoader(animationView,loader);
                if(response.isSuccessful())
                {  AllMealsResponse allMealsResponse=response.body();
                    mealList=allMealsResponse.getMeals();
                    mealItemAdapter=new MealItemAdapter(getActivity(),mealList,FoodFragment.this);
                    rvMeal.setAdapter(mealItemAdapter);
                }
                else
                {
                    CommonUtils.showWrongError(somethingWentWrongLayout);
                }
            }

            @Override
            public void onFailure(Call<AllMealsResponse> call, Throwable t) {
               Log.e("meal_response",t.getMessage()+"");
                CommonUtils.hideLoader(animationView,loader);
                CommonUtils.showWrongError(somethingWentWrongLayout);

            }
        });
    }

    @Override
    public void OnItemClick(int pos, String id, String name) {
        Intent intent=new Intent(getActivity(), MealDetailsActivity.class);
        intent.putExtra("meal_id",id);
        intent.putExtra("meal_name",name);
        startActivity(intent);
    }
}
