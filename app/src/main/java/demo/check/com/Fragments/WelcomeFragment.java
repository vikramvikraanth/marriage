package demo.check.com.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import demo.check.com.R;

public class WelcomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


    Unbinder unbinder;
    @BindView(R.id.slider)
    SliderLayout mDemoSlider;
    Context context;

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        unbinder = ButterKnife.bind(this, view);

        context = getActivity();
        HashMap();
        return view;
    }

    public void HashMap() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.step1);
        file_maps.put("2", R.drawable.step2);
        file_maps.put("3", R.drawable.step3);
        file_maps.put("4", R.drawable.step4);
        file_maps.put("5", R.drawable.step4);
        file_maps.put("6", R.drawable.step5);
        file_maps.put("7", R.drawable.step6);
        file_maps.put("8", R.drawable.step7);
        file_maps.put("9", R.drawable.step8);
        file_maps.put("10", R.drawable.step9);
        file_maps.put("11", R.drawable.step10);
        file_maps.put("12", R.drawable.step11);
        file_maps.put("13", R.drawable.step12);
        file_maps.put("14", R.drawable.step13);
        file_maps.put("15", R.drawable.step14);
        file_maps.put("16", R.drawable.step15);
        file_maps.put("17", R.drawable.step16);
        file_maps.put("18", R.drawable.step17);
        file_maps.put("19", R.drawable.step18);
        file_maps.put("20", R.drawable.step19);
        file_maps.put("21", R.drawable.step20);
        file_maps.put("22", R.drawable.step21);
        file_maps.put("23", R.drawable.step22);
        file_maps.put("24", R.drawable.step23);
        file_maps.put("memory", R.drawable.step24);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                   // .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
