package demo.check.com;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.check.com.Activity.MapScreen;
import demo.check.com.Activity.Time;
import demo.check.com.Activity.WelceomeActivity;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.navigationView)
    BottomNavigationView navigationView;
    @BindView(R.id.lattio_animaton)
    com.airbnb.lottie.LottieAnimationView lattioAnimaton;
    @BindView(R.id.greatings)
    TextView greatings;
    @BindView(R.id.hero_name)
    TextView heroName;
    @BindView(R.id.with_)
    TextView with;
    @BindView(R.id.heroine)
    TextView heroine;
    @BindView(R.id.footer_text)
    TextView footerText;
    @BindView(R.id.bottom_text)
    TextView bottomText;
    Typeface main,others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(this.getAssets(), getString(R.string.app_font));
        fontChangeCrawler.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));


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

    public void Startactivity(Class object) {
        startActivity(new Intent(getApplicationContext(), object));
    }


}
