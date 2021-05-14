package com.app.recipes.utils;

import android.graphics.drawable.Animatable;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

public class CommonUtils {
    public  static  void  showLoader(LottieAnimationView animationView, View view)
    {   view.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }
    public  static  void  hideLoader(LottieAnimationView animationView, View view)
    {
        animationView.pauseAnimation();
        view.setVisibility(View.GONE);
    }
    public static void ShowConnectionError(View view)
    {
        view.setVisibility(View.VISIBLE);
    }
    public  static  void hideConnectionError(View view)
    {
        view.setVisibility(View.GONE);

    }
    public  static  void showWrongError(View view)
    {
        view.setVisibility(View.VISIBLE);

    }
    public  static  void hideWrongError(View view)
    {
        view.setVisibility(View.GONE);

    }
    public  static  void showEmptyView(View view)
    {
        view.setVisibility(View.VISIBLE);
    }
    public  static  void hideEmptyView(View view )
    {
        view.setVisibility(View.GONE);
    }
}
