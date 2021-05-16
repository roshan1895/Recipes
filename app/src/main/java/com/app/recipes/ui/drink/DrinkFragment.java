package com.app.recipes.ui.drink;

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
import com.app.recipes.adapter.DrinkCategoryAdapter;
import com.app.recipes.adapter.DrinkItemAdapter;
import com.app.recipes.adapter.MealItemAdapter;
import com.app.recipes.model.drink.cat.AllDrinkCatResponse;
import com.app.recipes.model.drink.cat.Drink;
import com.app.recipes.model.drink.drink.AllDrinkResponse;
import com.app.recipes.model.food.cat.AllCategoryResponse;
import com.app.recipes.model.food.cat.Category;
import com.app.recipes.model.food.meals.AllMealsResponse;
import com.app.recipes.ui.SearchActivity;
import com.app.recipes.ui.food.FoodFragment;
import com.app.recipes.ui.food.MealDetailsActivity;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.CommonUtils;
import com.app.recipes.utils.Connection_Check;
import com.app.recipes.utils.UrlRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkFragment extends Fragment implements DrinkItemAdapter.OnItemClickListener, DrinkCategoryAdapter.OnItemClickListener {
    List<Drink> categoryList;
    DrinkCategoryAdapter adapter;
    RecyclerView rvCat,rvDrink;
    List<com.app.recipes.model.drink.drink.Drink> drinkList;
    DrinkItemAdapter drinkItemAdapter;
    RelativeLayout emptyview,emptyviewNew;
    RelativeLayout loader,loaderLayout;
    LottieAnimationView animationView,animationViewNew;
    RelativeLayout errorLay,errorLayNew;
    Button retryBtn;
    RelativeLayout somethingWentWrongLayout,somethingWentWrongLayoutNew;
    Button erroRetryBttn;
    String catName;
    Category cat;
    boolean isFirst=true;
    LinearLayout searchLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drink,container,false);
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
        emptyviewNew=getView().findViewById(R.id.emptyview_new);
        somethingWentWrongLayoutNew=getView().findViewById(R.id.something_went_error_layout_new);
        errorLayNew=getView().findViewById(R.id.error_lay_new);
        animationViewNew=getView().findViewById(R.id.animationViewNew);
        loaderLayout=getView().findViewById(R.id.loader_layout);
        searchLayout=getView().findViewById(R.id.search_layout);
        if(!isFirst)
        {
            CommonUtils.hideLoader(animationView,loader);
            CommonUtils.showLoader(animationViewNew,loaderLayout);
        }
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("type","drink");
                startActivity(intent);
            }
        });

        setupRV();


    }
    void setupRV()
    {   categoryList=new ArrayList<>();
        drinkList=new ArrayList<>();
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

        rvDrink=getView().findViewById(R.id.rv_meal);
        rvDrink.hasFixedSize();
        rvDrink.setLayoutManager(new LinearLayoutManager(getActivity()));
//        if(Connection_Check.checkConnection(getActivity()))
//        {
//            loadDrinks();
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
        Call<AllDrinkCatResponse> call= ApInterface.getDrinkClient().create(UrlRequest.class).allDrinkCategory("list");
        call.enqueue(new Callback<AllDrinkCatResponse>() {
            @Override
            public void onResponse(Call<AllDrinkCatResponse> call, Response<AllDrinkCatResponse> response) {
                if(response.isSuccessful())
                {
                    Log.e("cat_response",response.message()+"");
                    AllDrinkCatResponse allCategoryResponse=response.body();
                    categoryList= allCategoryResponse.getDrinks();
                    adapter=new DrinkCategoryAdapter(getActivity(),categoryList,DrinkFragment.this);
                    rvCat.setAdapter(adapter);
                    if(adapter.getSelected()!=null)
                    {
                        catName=adapter.getSelected().getStrCategory();
                    }
                    loadDrinks();
                }
                else
                {
                    Log.e("cat_error",response.code()+"");
                    CommonUtils.hideLoader(animationView,loader);
                    CommonUtils.showWrongError(somethingWentWrongLayout);

                }
            }

            @Override
            public void onFailure(Call<AllDrinkCatResponse> call, Throwable t) {
                Log.e("food_failure",t.getMessage()+"");
                CommonUtils.hideLoader(animationView,loader);
                CommonUtils.showWrongError(somethingWentWrongLayout);
            }
        });

    }
    void loadDrinks()
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
        Call<AllDrinkResponse> call=ApInterface.getDrinkClient().create(UrlRequest.class).allDrink(catName);
        call.enqueue(new Callback<AllDrinkResponse>() {
            @Override
            public void onResponse(Call<AllDrinkResponse> call, Response<AllDrinkResponse> response) {
                if(isFirst)
                {
                    CommonUtils.hideLoader(animationView,loader);

                }
                else
                {              CommonUtils.hideLoader(animationViewNew,loaderLayout);

                }
                if(response.isSuccessful())
                {  AllDrinkResponse allMealsResponse=response.body();
                    drinkList=allMealsResponse.getDrinks();
                    drinkItemAdapter=new DrinkItemAdapter(getActivity(),drinkList, DrinkFragment.this);
                    rvDrink.setAdapter(drinkItemAdapter);
                }
                else
                {
                    if(isFirst)
                    {
                        CommonUtils.showWrongError(somethingWentWrongLayout);

                    }
                    else
                    {
                        CommonUtils.showWrongError(somethingWentWrongLayoutNew);

                    }                }
            }

            @Override
            public void onFailure(Call<AllDrinkResponse> call, Throwable t) {
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
    public void onItemCLick(int pos, String name) {
        isFirst=false;
        catName=name;
        drinkList.clear();
        CommonUtils.showLoader(animationViewNew,loaderLayout);
        loadDrinks();
    }

    @Override
    public void OnDrinkClick(int pos, String id, String name) {
        Intent intent=new Intent(getActivity(), DrinkDetailsActivity.class);
        intent.putExtra("drink_id",id);
        intent.putExtra("drink_name",name);
        startActivity(intent);
    }
}
