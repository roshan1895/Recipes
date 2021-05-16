package com.app.recipes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.recipes.R;
import com.app.recipes.adapter.DrinkItemAdapter;
import com.app.recipes.adapter.MealItemAdapter;
import com.app.recipes.model.drink.SearchDrinksResponse;
import com.app.recipes.model.drink.drink.Drink;
import com.app.recipes.model.food.SearchFoodResponse;
import com.app.recipes.model.food.meals.Meal;
import com.app.recipes.ui.drink.DrinkDetailsActivity;
import com.app.recipes.ui.food.MealDetailsActivity;
import com.app.recipes.utils.ApInterface;
import com.app.recipes.utils.Connection_Check;
import com.app.recipes.utils.UrlRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MealItemAdapter.OnItemClickListener, DrinkItemAdapter.OnItemClickListener {
    String type;
    ImageView back_image,clear_search_query,voice_search_query;
    EditText search_edit_text;
    LinearLayout toggle_rel;
    FrameLayout toggle_frame;
    ProgressBar progressBar;
    RecyclerView search_list;
    LinearLayout no_result_layout;
    final int VOICE_SEARCH_CODE = 3012;
    String query;
    List<Meal> mealList;
    List<Drink> drinkList;
    MealItemAdapter mealItemAdapter;
    DrinkItemAdapter drinkItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        type=getIntent().getStringExtra("type");
        initView();

    }
    void initView()
    {
     back_image=findViewById(R.id.back_image_view);
     clear_search_query=findViewById(R.id.clear_search_query);
     search_edit_text=findViewById(R.id.search_edit_text);
     toggle_rel=findViewById(R.id.toggle_rel);
     toggle_frame=findViewById(R.id.toggle_frame);
     progressBar=findViewById(R.id.progress_bar);
     search_list=findViewById(R.id.search_list);
     no_result_layout=findViewById(R.id.no_result_layout);
     voice_search_query=findViewById(R.id.voice_search_query);
     drinkList=new ArrayList<>();
     mealList=new ArrayList<>();
     setupRv();

     clear_search_query.setOnClickListener(this);
     back_image.setOnClickListener(this);
     voice_search_query.setOnClickListener(this);
     search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query=s.toString();
                toggleImageView(query);
                if(type.equalsIgnoreCase("food"))
                {
                    searchFoodQuery(query);
                }
                else if(type.equalsIgnoreCase("drink"))
                {
                    searchDrinkQuery(query);
                }
            }
        });
    }
    void setupRv()
    {
        search_list.hasFixedSize();
        search_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.clear_search_query)
        {
            search_edit_text.setText("");

        }
        else if(id==R.id.voice_search_query)
        {
            startVoiceRecognition();

        }
        else if(id==R.id.back_image_view)
        {

            finish();
        }

    }

    void searchFoodQuery(String str)
    {
        Log.e("str_",str+"");
        Log.e("str_length",str.length()+"");
        if(!TextUtils.isEmpty(str))
        {
            if(str.length()>=2)
            {
                if(search_list.getVisibility()==View.GONE)
                {
                    search_list.setVisibility(View.VISIBLE);
                }
                if(Connection_Check.checkConnection(getApplicationContext()))
                {
                    showProgress();
                    searchFoods();
                }
                else
                {
                    Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                     mealList.clear();
            }

        }
        else
        {
            mealList.clear();

        }
    }
    void searchDrinkQuery(String str)
    {
        if(!TextUtils.isEmpty(str))
        {
            if(str.length()>=2)
            {
                if(search_list.getVisibility()==View.GONE)
                {
                    search_list.setVisibility(View.VISIBLE);
                }
                if(Connection_Check.checkConnection(getApplicationContext()))
                {
                    showProgress();
                    searchDrinks();
//                    search(currentPage,str);
                }
                else
                {
                    Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                drinkList.clear();
            }

        }
        else
        {
            drinkList.clear();

        }
    }
    void showProgress()
    {
        clear_search_query.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    void hideProgress()
    {
        progressBar.setVisibility(View.INVISIBLE);
        clear_search_query.setVisibility(View.VISIBLE);
    }
    void toggleImageView(String query)
    {
        if(!query.isEmpty())
        {
            toggle_frame.setVisibility(View.VISIBLE);
        }
        else
        {
            toggle_frame.setVisibility(View.GONE);
        }
    }
    public void startVoiceRecognition() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra("android.speech.extra.PROMPT", "Speak Now");
        this.startActivityForResult(intent, VOICE_SEARCH_CODE);
    }
    void searchFoods()
    {
        Call<SearchFoodResponse> call= ApInterface.getFoodClient().create(UrlRequest.class).searchMeals(query);
        call.enqueue(new Callback<SearchFoodResponse>() {
            @Override
            public void onResponse(Call<SearchFoodResponse> call, Response<SearchFoodResponse> response) {
               hideProgress();
                if(no_result_layout.getVisibility()==View.VISIBLE)
                {
                    no_result_layout.setVisibility(View.GONE);
                }
                if(response.isSuccessful())
                {
                    SearchFoodResponse searchFoodResponse=response.body();
                    mealList=searchFoodResponse.getMealList();
                    if(mealList!=null)
                    {
                        mealItemAdapter =new MealItemAdapter(getApplicationContext(),mealList,SearchActivity.this);
                        search_list.setAdapter(mealItemAdapter);
                    }
                    else
                    {
                        if(mealItemAdapter!=null)
                        {
                            mealItemAdapter.clear();
                        }
                        no_result_layout.setVisibility(View.VISIBLE);

                    }
                }
                else {
                    if(mealList!=null)
                    {
                        mealList.clear();
                    }
                    Toast.makeText(SearchActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SearchFoodResponse> call, Throwable t) {
               hideProgress();
                if(mealList!=null)
                {
                    mealList.clear();
                }
                Toast.makeText(SearchActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void searchDrinks()
    {
        Call<SearchDrinksResponse> call=ApInterface.getDrinkClient().create(UrlRequest.class).searchDrinks(query);
        call.enqueue(new Callback<SearchDrinksResponse>() {
            @Override
            public void onResponse(Call<SearchDrinksResponse> call, Response<SearchDrinksResponse> response) {
                hideProgress();
                if(no_result_layout.getVisibility()==View.VISIBLE)
                {
                    no_result_layout.setVisibility(View.GONE);
                }
                if(response.isSuccessful())
                {
                    SearchDrinksResponse searchDrinksResponse=response.body();
                    drinkList=searchDrinksResponse.getMealList();
                    if(drinkList!=null)
                    {
                        drinkItemAdapter =new DrinkItemAdapter(getApplicationContext(),drinkList,SearchActivity.this);
                        search_list.setAdapter(drinkItemAdapter);
                    }
                    else
                    {
                       if(drinkItemAdapter!=null)
                       {
                           drinkItemAdapter.clear();
                       }
                        no_result_layout.setVisibility(View.VISIBLE);

                    }
//                    if(drinkList.isEmpty())
//                    {
//                        no_result_layout.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        drinkItemAdapter =new DrinkItemAdapter(getApplicationContext(),drinkList,SearchActivity.this);
//                        search_list.setAdapter(drinkItemAdapter);
//                    }

                }
                else
                {
                    if(drinkList!=null)
                {
                    drinkList.clear();
                }

                    Toast.makeText(SearchActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchDrinksResponse> call, Throwable t) {
                hideProgress();
                if(drinkList!=null)
                {
                    drinkList.clear();
                }
                Toast.makeText(SearchActivity.this, "Something went wrong,Please try again...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void OnItemClick(int pos, String id, String name) {
        Intent intent=new Intent(SearchActivity.this, MealDetailsActivity.class);
        intent.putExtra("meal_id",id);
        intent.putExtra("meal_name",name);
        startActivity(intent);
    }

    @Override
    public void OnDrinkClick(int pos, String id, String name) {
        Intent intent=new Intent(SearchActivity.this, DrinkDetailsActivity.class);
        intent.putExtra("drink_id",id);
        intent.putExtra("drink_name",name);
        startActivity(intent);
    }
}