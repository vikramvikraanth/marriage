package demo.check.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.check.com.Activity.MapScreen;
import demo.check.com.Activity.Time;
import demo.check.com.Activity.WelceomeActivity;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi_welcome:
                        Startactivity(WelceomeActivity.class);
                        break;
                    case R.id.navi_timeline:
                        Startactivity(Time.class);
                        break;

                    case R.id.navi_venue:
                        Startactivity(MapScreen.class);
                        break;

                }
                return true;
            }
        });
    }
    public void Startactivity(Class object){
        startActivity(new Intent(getApplicationContext(),object));
    }



}
