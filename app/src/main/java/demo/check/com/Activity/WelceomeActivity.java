package demo.check.com.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import demo.check.com.FontChangeCrawler;
import demo.check.com.R;

public class WelceomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welceome);
        FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(this.getAssets(), getString(R.string.app_font));
        fontChangeCrawler.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }
}
