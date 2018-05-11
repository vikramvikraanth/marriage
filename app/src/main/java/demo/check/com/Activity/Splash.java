package demo.check.com.Activity;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.check.com.MainActivity;
import demo.check.com.R;
import io.fabric.sdk.android.Fabric;

public class Splash extends AppCompatActivity {

    @BindView(R.id.lattio_animaton)
    LottieAnimationView lattioAnimaton;
    Context context = Splash.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        lattioAnimaton.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                checkMultiplePermissions();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void checkMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();


            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsNeeded.add("Access the Current Location");
            }

            if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                permissionsNeeded.add("Access Fine Location");
            }
           /* if(!addPermission(permissionsList, android.Manifest.permission.CALL_PHONE))
            {
                permissionsNeeded.add("Call Phone");
            }*/

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        1);
            } else {
                startActivity(new Intent(context, MainActivity.class));

            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(context, MainActivity.class));
                    return;
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(getApplicationContext(), "Please permit all the permissions", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
