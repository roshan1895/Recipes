
package com.app.recipes.ui;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.app.recipes.R;

public class MealDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
     Toolbar toolbar;
     CollapsingToolbarLayout toolbarLayout;
     AppBarLayout appBarLayout;
     boolean appBarExpanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
    }
    void initView()
    {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout=findViewById(R.id.htab_collapse_toolbar);
        appBarLayout=findViewById(R.id.htab_appbar);
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if ((Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange())) {
//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.text_color));
            appBarExpanded = false;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(text_color), PorterDuff.Mode.SRC_ATOP);
//                      htabCollapseToolbar.setTitle("Testing");
            toolbar.setTitle(business_name);
//                    Log.e("appbarexpanded",appBarExpanded+"");
            invalidateOptionsMenu();
        }
        else if((Math.abs(verticalOffset)>=appBarLayout.getTotalScrollRange()/2))
        {
            appBarExpanded=false;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(text_color), PorterDuff.Mode.SRC_ATOP);
//                     htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");
            invalidateOptionsMenu();
        }
        else if (verticalOffset == 0) {
            //expanded
            appBarExpanded=true;
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(white), PorterDuff.Mode.SRC_ATOP);
//                     htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");

            invalidateOptionsMenu();
//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }
        else {
            appBarExpanded=true;
//                    title.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(white), PorterDuff.Mode.SRC_ATOP);
//                    htabCollapseToolbar.setTitle("");
            toolbar.setTitle("");
//                    Log.e("appbarexpanded",appBarExpanded+"");
            invalidateOptionsMenu();

        }
    }
}