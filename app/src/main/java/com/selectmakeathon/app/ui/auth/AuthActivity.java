package com.selectmakeathon.app.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.auth.login.LoginFragment;
import com.selectmakeathon.app.ui.auth.otp.OtpFragment;
import com.selectmakeathon.app.ui.auth.signup.SignupFragment;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.util.Constants;

public class AuthActivity extends AppCompatActivity {

    static View loadingContainer;
    static LottieAnimationView loadingAnimation;

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loadingContainer = findViewById(R.id.loading_animation_container);
        loadingAnimation = findViewById(R.id.lottie_loading_animation);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = prefs.edit();

        updateFragment(LoginFragment.newInstance());
    }

    public void updateFragment(Fragment fragment) {

        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container_auth, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }
    }

    public void startMainActivity() {
        prefEditor.putBoolean(Constants.PREF_IS_LOGGED_IN, false).apply();

        TaskStackBuilder.create(AuthActivity.this)
                .addNextIntentWithParentStack(new Intent(AuthActivity.this, MainActivity.class))
                .startActivities();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    public static void startAnimation(){
        loadingContainer.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
    }

    public static void stopAnimation(){
        loadingContainer.setVisibility(View.INVISIBLE);
        loadingAnimation.pauseAnimation();
    }

}
