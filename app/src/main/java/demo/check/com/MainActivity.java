package demo.check.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.check.com.Fragments.MapScreen;
import demo.check.com.Fragments.WelcomeFragment;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.welcome_btn)
    Button welcomeBtn;
    @BindView(R.id.time_line_btn)
    Button timeLineBtn;
    @BindView(R.id.venue_btn)
    Button venueBtn;
   static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.welcome_btn, R.id.time_line_btn, R.id.venue_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.welcome_btn:
                fragment = new WelcomeFragment();
                moveToFragment(fragment);
                break;
            case R.id.time_line_btn:
                break;
            case R.id.venue_btn:
                fragment = new MapScreen();
                moveToFragment(fragment);
                break;
        }
    }
    private void moveToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }
}
