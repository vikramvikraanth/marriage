package demo.check.com.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.check.com.FontChangeCrawler;
import demo.check.com.R;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

public class Time extends AppCompatActivity {

    @BindView(R.id.flipper_layout)
    FlipperLayout flipperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        ButterKnife.bind(this);
        setLayout();
        FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(this.getAssets(), getString(R.string.app_font));
        fontChangeCrawler.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
    }

    private void setLayout() {
        String url[] = new String[]{
                "http://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
                "https://i.pinimg.com/originals/d3/84/d1/d384d1c565dc6b501a61286bf0879481.jpg",
                "https://i.pinimg.com/originals/18/40/72/184072abb72399c23ab635faaa0a94ad.jpg"
        };
        for (int i = 0; i < 3; i++) {
            FlipperView view = new FlipperView(getBaseContext());
            view.setImageUrl(url[i])
                    .setDescription("Cool" + (i + 1));
            flipperLayout.addFlipperView(view);
            view.setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                @Override
                public void onFlipperClick(FlipperView flipperView) {

                }
            });
        }
    }
}
