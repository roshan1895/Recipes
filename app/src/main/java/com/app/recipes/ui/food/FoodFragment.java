package com.app.recipes.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.app.recipes.model.food.cat.AllCategoryResponse;
import com.app.recipes.model.food.cat.Category;
import com.app.recipes.model.food.meals.AllMealsResponse;
import com.app.recipes.model.food.meals.Meal;
import com.app.recipes.ui.SearchActivity;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.CommonUtils;
import com.app.recipes.utils.Connection_Check;
import com.app.recipes.utils.UrlRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodFragment extends Fragment implements MealItemAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener {
    List<Category> categoryList;
    CategoryAdapter adapter;
    RecyclerView rvCat,rvMeal;
    List<Meal> mealList;
    MealItemAdapter mealItemAdapter;
    RelativeLayout emptyview,emptyviewNew;
    RelativeLayout loader,loaderLayout;
    LottieAnimationView animationView,animationViewNew;
    RelativeLayout errorLay,errorLayNew;
    Button retryBtn;
    RelativeLayout somethingWentWrongLayout,somethingWentWrongLayoutNew;
    Button erroRetryBttn;
    String catName;
    Category cat;
    LinearLayout searchLayout;
    boolean isFirst=true;

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
        emptyviewNew=getView().findViewById(R.id.emptyview_new);
        loader=getView().findViewById(R.id.loader);
        animationView=getView().findViewById(R.id.animationView);
        errorLay=getView().findViewById(R.id.error_lay);
        retryBtn=getView().findViewById(R.id.retry_btn);
        erroRetryBttn=getView().findViewById(R.id.retry_btn_error);
        somethingWentWrongLayout=getView().findViewById(R.id.something_went_error_layout);
        somethingWentWrongLayoutNew=getView().findViewById(R.id.something_went_error_layout_new);
        errorLayNew=getView().findViewById(R.id.error_lay_new);
       loaderLayout=getView().findViewById(R.id.loader_layout);
       searchLayout=getView().findViewById(R.id.search_layout);
       animationViewNew=getView().findViewById(R.id.animationViewNew);
       if(!isFirst)
       {
           CommonUtils.hideLoader(animationView,loader);
        CommonUtils.showLoader(animationViewNew,loaderLayout);
       }
       searchLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(), SearchActivity.class);
               intent.putExtra("type","food");
               startActivity(intent);
           }
       });
        setupRV();



    }
    void setupRV()
    {   categoryList=new ArrayList<>();
        mealList=new ArrayList<>();
        rvCat=getView().findViewById(R.id.rv_cat);
        rvCat.hasFixedSize();
        rvCat.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
       if(Connection_Check.checkConnection(getActivity()))
       {
           loadCategory();

       }
       else
       {
           CommonUtils.hideLoader(animationView,loader);
           CommonUtils.showConnectionError(errorLay);
       }
        rvMeal=getView().findViewById(R.id.rv_meal);
        rvMeal.hasFixedSize();
        rvMeal.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if(Connection_Check.checkConnection(getActivity()))
//        {
//            loadMeals();
//
//        }
//        else
//        {
//            CommonUtils.hideLoader(animationView,loader);
//            CommonUtils.showConnectionError(errorLay);
//        }

    }
    void  loadCategory()
    {
        Call<AllCategoryResponse>call= ApInterface.getFoodClient().create(UrlRequest.class).allCategory();
        call.enqueue(new Callback<AllCategoryResponse>() {
            @Override
            public void onResponse(Call<AllCategoryResponse> call, Response<AllCategoryResponse> response) {
                if(response.isSuccessful())
                {   Log.e("cat_response",response.message()+"");
                    AllCategoryResponse allCategoryResponse=response.body();
                    categoryList= allCategoryResponse.getCategories();
                    adapter=new CategoryAdapter(getActivity(),categoryList,FoodFragment.this);
                    rvCat.setAdapter(adapter);
                    if(adapter.getSelected()!=null)
                    {
                        catName=adapter.getSelected().getStrCategory();
                    }
                    loadMeals();

                }
                else
                {
                    Log.e("cat_error",response.code()+"");
                     CommonUtils.hideLoader(animationView,loader);
                     CommonUtils.showWrongError(somethingWentWrongLayout);
                }
            }

            @Override
            public void onFailure(Call<AllCategoryResponse> call, Throwable t) {
                Log.e("food_failure",t.getMessage()+"");
                CommonUtils.hideLoader(animationView,loader);
                CommonUtils.showWrongError(somethingWentWrongLayout);
            }
        });

    }
    void loadMeals()
    {   if(isFirst)
        {
            if(errorLay.getVisibility()==View.VISIBLE)
            {
                CommonUtils.hideConnectionError(errorLay);
            }
            if(somethingWentWrongLayout.getVisibility()==View.VISIBLE)
            {
                CommonUtils.hideWrongError(somethingWentWrongLayout);
            }
        }
        else
        {
            if(errorLayNew.getVisibility()==View.VISIBLE)
            {
                CommonUtils.hideConnectionError(errorLayNew);
            }
            if(somethingWentWrongLayoutNew.getVisibility()==View.VISIBLE)
            {
                CommonUtils.hideWrongError(somethingWentWrongLayoutNew);
            }
        }

         Log.e("cat_name",catName+"");
        Call<AllMealsResponse> call=ApInterface.getFoodClient().create(UrlRequest.class).allMeal(catName);
        call.enqueue(new Callback<AllMealsResponse>() {
            @Override
            public void onResponse(Call<AllMealsResponse> call, Response<AllMealsResponse> response) {
             if(isFirst)
             {
                 CommonUtils.hideLoader(animationView,loader);

             }
             else
             {              CommonUtils.hideLoader(animationViewNew,loaderLayout);

             }
                if(response.isSuccessful())
                {  AllMealsResponse allMealsResponse=response.body();
                    mealList=allMealsResponse.getMeals();
                    mealItemAdapter=new MealItemAdapter(getActivity(),mealList,FoodFragment.this);
                    rvMeal.setAdapter(mealItemAdapter);
                }
                else
                {   if(isFirst)
                    {
                        CommonUtils.showWrongError(somethingWentWrongLayout);

                    }
                    else
                    {
                        CommonUtils.showWrongError(somethingWentWrongLayoutNew);

                    }
                }
            }

            @Override
            public void onFailure(Call<AllMealsResponse> call, Throwable t) {
               Log.e("meal_response",t.getMessage()+"");
               if(isFirst)
               {
                  CommonUtils.hideLoader(animationView,loader);
                   CommonUtils.showWrongError(somethingWentWrongLayout);

               }
               else
               {
                   CommonUtils.hideLoader(animationViewNew,loaderLayout);
                   CommonUtils.showWrongError(somethingWentWrongLayoutNew);

               }


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

    @Override
    public void onItemCLick(int pos, String name) {
        Log.e("name",name+"");
        isFirst=false;
           catName=name;
           mealList.clear();
           CommonUtils.showLoader(animationViewNew,loaderLayout);
           loadMeals();
    }
}
