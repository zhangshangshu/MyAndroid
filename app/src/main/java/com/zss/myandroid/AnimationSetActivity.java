package com.zss.myandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class AnimationSetActivity extends AppCompatActivity {
    private Button btnAnimSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_set);

        btnAnimSet = findViewById(R.id.btn_animation_set);
        Animation animationSet = AnimationUtils.loadAnimation(this,R.anim.view_animation_set);
        btnAnimSet.startAnimation(animationSet);
    }
}
