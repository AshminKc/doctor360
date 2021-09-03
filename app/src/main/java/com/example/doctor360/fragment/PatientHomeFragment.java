package com.example.doctor360.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doctor360.R;
import com.example.doctor360.adapter.ImageSliderAdapter;

import me.relex.circleindicator.CircleIndicator;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class PatientHomeFragment extends Fragment {

    View rootView;
    private long delayTime =  5000;
    ImageSliderAdapter imageSliderAdapter;
    private AutoScrollViewPager viewPager;
    private CircleIndicator pageIndicatorView;
    private static final String TAG = "PatientHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_patient_home,container,false);

        viewPager = rootView.findViewById(R.id.sliderViewPager);
        pageIndicatorView = rootView.findViewById(R.id.pageIndicator);

        viewPager.startAutoScroll();
        viewPager.setInterval(delayTime);
        viewPager.setStopScrollWhenTouch(true);

        imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);
        pageIndicatorView.setViewPager(viewPager);

        return rootView;
    }
}
