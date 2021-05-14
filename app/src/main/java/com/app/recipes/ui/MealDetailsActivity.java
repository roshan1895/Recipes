
package com.app.recipes.ui;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.app.recipes.model.meals.details.MealDetailsResponse;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.CommonUtils;
import com.app.recipes.utils.UrlRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.recipes.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
     Toolbar toolbar;
     CollapsingToolbarLayout toolbarLayout;
     AppBarLayout appBarLayout;
     boolean appBarExpanded = true;
     String mealId;
     String  mealName;
     TextView  mealTitle,mealCategory;
     ImageView htab_header;
    RelativeLayout emptyview;
    RelativeLayout loader;
    LottieAnimationView animationView;
    RelativeLayout errorLay;
    Button retryBtn;
    RelativeLayout somethingWentWrongLayout;
    Button erroRetryBttn;
    TextView instructionTv;
    RecyclerView rvingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        initView();
        }
    void initView()
    {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLayout=findViewById(R.id.htab_collapse_toolbar);
        appBarLayout=findViewById(R.id.htab_appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        mealTitle=findViewById(R.id.meal_title);
        mealCategory=findViewById(R.id.meal_category);
        htab_header=findViewById(R.id.htab_header);
        emptyview=findViewById(R.id.emptyview);
        loader=findViewById(R.id.loader);
        animationView=findViewById(R.id.animationView);
        errorLay=findViewById(R.id.error_lay);
        retryBtn=findViewById(R.id.retry_btn);
        erroRetryBttn=findViewById(R.id.retry_btn_error);
        somethingWentWrongLayout=findViewById(R.id.something_went_error_layout);
        rvingredients=findViewById(R.id.rv_ingredients);
        mealId=getIntent().getStringExtra("meal_id");
        mealName=getIntent().getStringExtra("meal_name");

    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange())) {
//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.text_color));
            appBarExpanded = false;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.text_color), PorterDuff.Mode.SRC_ATOP);
//                      htabCollapseToolbar.setTitle("Testing");
            toolbar.setTitleTextColor(getResources().getColor(R.color.text_color));
            toolbar.setTitle(mealName);

//                    Log.e("appbarexpanded",appBarExpanded+"");
        }
        else if((Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()/2))
        {
            appBarExpanded=false;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.text_color), PorterDuff.Mode.SRC_ATOP);
//                     htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");
        }
        else if (verticalOffset == 0) {
            //expanded
            appBarExpanded=true;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//                     htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");

//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }
        else {
            appBarExpanded=true;
//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//                    htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");

        }
    }
    void loadDetails()
    {   if(errorLay.getVisibility()==View.VISIBLE)
        {
            errorLay.setVisibility(View.GONE);
        }
        if(somethingWentWrongLayout.getVisibility()==View.VISIBLE)
        {
            somethingWentWrongLayout.setVisibility(View.GONE);
        }
        Call<MealDetailsResponse> call= ApInterface.getClient().create(UrlRequest.class).mealDetails(mealId);
        call.enqueue(new Callback<MealDetailsResponse>() {
            @Override
            public void onResponse(Call<MealDetailsResponse> call, Response<MealDetailsResponse> response) {
                CommonUtils.hideLoader(animationView,loader);
                if(response.isSuccessful())
                {  MealDetailsResponse detailsResponse=response.body();
                    setdata(detailsResponse);
                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<MealDetailsResponse> call, Throwable t) {

            }
        });
    }
    void setdata(MealDetailsResponse mealDetailsResponse)
    {

    }
}