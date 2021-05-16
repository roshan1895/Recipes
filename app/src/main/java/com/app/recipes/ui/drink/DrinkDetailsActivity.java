package com.app.recipes.ui.drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.recipes.R;
import com.app.recipes.adapter.IngredientsAdapter;
import com.app.recipes.model.Ingredient;
import com.app.recipes.model.drink.drink.details.Drink;
import com.app.recipes.model.drink.drink.details.DrinkDetailsResponse;
import com.app.recipes.model.food.meals.details.Meal;
import com.app.recipes.model.food.meals.details.MealDetailsResponse;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.CommonUtils;
import com.app.recipes.utils.Constants;
import com.app.recipes.utils.UrlRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.recipes.utils.CommonUtils.checkValue;

public class DrinkDetailsActivity extends AppCompatActivity  implements AppBarLayout.OnOffsetChangedListener{
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appBarLayout;
    boolean appBarExpanded = true;
    String mealId;
    String  mealName;
    TextView mealTitle,mealCategory;
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
    List<Ingredient> ingredientList;
    IngredientsAdapter ingredientsAdapter;
    ExtendedFloatingActionButton videoBtn;
    LinearLayout instructions_layout;
    TextView meal_type;
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
        videoBtn=findViewById(R.id.watch_video_btn);
        loader=findViewById(R.id.loader);
        instructions_layout=findViewById(R.id.instructions_layout);
        animationView=findViewById(R.id.animationView);
        errorLay=findViewById(R.id.error_lay);
        retryBtn=findViewById(R.id.retry_btn);
        erroRetryBttn=findViewById(R.id.retry_btn_error);
        somethingWentWrongLayout=findViewById(R.id.something_went_error_layout);
        rvingredients=findViewById(R.id.rv_ingredients);
        instructionTv=findViewById(R.id.instructions_tv);
        meal_type=findViewById(R.id.meal_type);
        mealId=getIntent().getStringExtra("drink_id");
        mealName=getIntent().getStringExtra("drink_name");
        Log.e("drink_id",mealId+"");
        setupRv();
        loadDetails();

    }
    void setupRv()
    {
        rvingredients.hasFixedSize();
        rvingredients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ingredientsAdapter =new IngredientsAdapter(getApplicationContext());
        rvingredients.setAdapter(ingredientsAdapter);
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
    {   if(errorLay.getVisibility()== View.VISIBLE)
    {
        errorLay.setVisibility(View.GONE);
    }
        if(somethingWentWrongLayout.getVisibility()==View.VISIBLE)
        {
            somethingWentWrongLayout.setVisibility(View.GONE);
        }
        Call<DrinkDetailsResponse> call= ApInterface.getDrinkClient().create(UrlRequest.class).drinkDetails(mealId);
        call.enqueue(new Callback<DrinkDetailsResponse>() {
            @Override
            public void onResponse(Call<DrinkDetailsResponse> call, Response<DrinkDetailsResponse> response) {
                CommonUtils.hideLoader(animationView,loader);
                if(response.isSuccessful())
                {  DrinkDetailsResponse detailsResponse=response.body();
                    setdata(detailsResponse);
                }
                else
                {
                    CommonUtils.showWrongError(somethingWentWrongLayout);
                }
            }

            @Override
            public void onFailure(Call<DrinkDetailsResponse> call, Throwable t) {
                CommonUtils.hideLoader(animationView,loader);
                CommonUtils.showWrongError(somethingWentWrongLayout);

            }
        });
    }
    void setdata(DrinkDetailsResponse mealDetailsResponse)
    {
        Drink meal=mealDetailsResponse.getDrinks().get(0);
        mealTitle.setText(meal.getStrDrink());
        Glide.with(getApplicationContext()).load(meal.getStrDrinkThumb()).into(htab_header);
        meal_type.setVisibility(View.VISIBLE);
        meal_type.setText(meal.getStrAlcoholic());
        mealCategory.setText(meal.getStrCategory());
        if(!mealDetailsResponse.getDrinks().get(0).getStrInstructions().isEmpty())
        {
            SpannableString string= new SpannableString(mealDetailsResponse.getDrinks().get(0).getStrInstructions());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    string.setSpan(new BulletSpan(20,getColor(R.color.green_dark),10),0,string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            instructionTv.setText(string);
        }
        else
        {
            instructions_layout.setVisibility(View.GONE);
        }


//        setInstructions(mealDetailsResponse.getDrinks().get(0).getStrInstructions());
        parseIngredients(meal);

    }
    void parseIngredients(Drink meal)
    {
        if(checkValue(meal.getStrIngredient1()))
        {
            addIngredient(meal.getStrIngredient1(),meal.getStrMeasure1());
        }
        if(checkValue(meal.getStrIngredient2()))
        {
            addIngredient(meal.getStrIngredient2(),meal.getStrMeasure2());
        }
        if(checkValue(meal.getStrIngredient3()))
        {
            addIngredient(meal.getStrIngredient3(),meal.getStrMeasure3());
        }
        if(checkValue(meal.getStrIngredient4()))
        {
            addIngredient(meal.getStrIngredient4(),meal.getStrMeasure4());
        }
        if(checkValue(meal.getStrIngredient5()))
        {
            addIngredient(meal.getStrIngredient5(),meal.getStrMeasure5());
        }
        if(checkValue(meal.getStrIngredient6()))
        {
            addIngredient(meal.getStrIngredient6(),meal.getStrMeasure6());
        }
        if(checkValue(meal.getStrIngredient7()))
        {
            addIngredient(meal.getStrIngredient7(),meal.getStrMeasure7());
        }
        if(checkValue(meal.getStrIngredient8()))
        {
            addIngredient(meal.getStrIngredient8(),meal.getStrMeasure8());
        }
        if(checkValue(meal.getStrIngredient9()))
        {
            addIngredient(meal.getStrIngredient9(),meal.getStrMeasure9());
        }
        if(checkValue(meal.getStrIngredient10()))
        {
            addIngredient(meal.getStrIngredient10(),meal.getStrMeasure10());
        }
        if(checkValue(meal.getStrIngredient11()))
        {
            addIngredient(meal.getStrIngredient11(),meal.getStrMeasure11());
        }
        if(checkValue(meal.getStrIngredient12()))
        {
            addIngredient(meal.getStrIngredient12(),meal.getStrMeasure12());
        }
        if(checkValue(meal.getStrIngredient13()))
        {
            addIngredient(meal.getStrIngredient13(),meal.getStrMeasure13());
        }
        if(checkValue(meal.getStrIngredient14()))
        {
            addIngredient(meal.getStrIngredient14(),meal.getStrMeasure14());
        }
        if(checkValue(meal.getStrIngredient15()))
        {
            addIngredient(meal.getStrIngredient15(),meal.getStrMeasure15());
        }



    }

    void addIngredient(String name,String qty)
    {   ingredientsAdapter.add(new Ingredient(name, Constants.getDrinkIngredientsSmallImage(name),qty));
    }
    void setInstructions(String str)
    {
        List<SpannableString> list=new ArrayList<>();
        String lines[] = str.split("\\r?\\n");
        setInstructionsTv(lines);
    }
    void setInstructionsTv(String[] list)
    {   Log.e("list.length",list.length+"");
        final SpannableStringBuilder builder=new SpannableStringBuilder();
        boolean first=true;
        for(String  str:list)
        {   Log.e("list_str.length",str.length()+"");
            SpannableString string= new SpannableString(str);

            if(str.length()>0)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        string.setSpan(new BulletSpan(20,getResources().getColor(R.color.green_dark),10),0,string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            if(first)
            {
                first = false;
                builder.append(string);
            }
            else
            {
                builder.append("\n").append(string);

            }
        }
        instructionTv.setText(builder);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}