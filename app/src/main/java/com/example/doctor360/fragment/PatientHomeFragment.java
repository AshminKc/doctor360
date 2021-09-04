package com.example.doctor360.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.MainModel;
import com.example.doctor360.R;
import com.example.doctor360.adapter.AppointRequestPatientAdapter;
import com.example.doctor360.adapter.ChatRequestPatientAdapter;
import com.example.doctor360.adapter.ImageSliderAdapter;
import com.example.doctor360.helper.ConnectionDetector;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class PatientHomeFragment extends Fragment {

    View rootView;
    private long delayTime =  5000;
    ImageSliderAdapter imageSliderAdapter;
    private AutoScrollViewPager viewPager;
    private CircleIndicator pageIndicatorView;
    TextView viewAllDoctorTXT, viewAllHospitalTXT;
    RecyclerView doctorRecyclerView, hospitalRecyclerView;
    ChatRequestPatientAdapter chatRequestPatientAdapter;
    AppointRequestPatientAdapter appointRequestPatientAdapter;
    ConnectionDetector connectionDetector;
    ArrayList<MainModel> mainModels;
    private static final String TAG = "PatientHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_patient_home,container,false);

        viewPager = rootView.findViewById(R.id.sliderViewPager);
        pageIndicatorView = rootView.findViewById(R.id.pageIndicator);
        viewAllDoctorTXT = rootView.findViewById(R.id.txtViewAllDoctor);
        viewAllHospitalTXT = rootView.findViewById(R.id.txtViewAllHospitals);
        doctorRecyclerView = rootView.findViewById(R.id.allDoctorRecyclerView);
        hospitalRecyclerView = rootView.findViewById(R.id.allHospitalRecyclerView);

        viewPager.startAutoScroll();
        viewPager.setInterval(delayTime);
        viewPager.setStopScrollWhenTouch(true);

        imageSliderAdapter = new ImageSliderAdapter(getContext());
        viewPager.setAdapter(imageSliderAdapter);
        pageIndicatorView.setViewPager(viewPager);

        Integer[] logo = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};
        String[] name = {"Ram", "Sita","Gita","Shiva","Sudeep"};
        String [] mobile = {"1111","0000","2222","3333","4444"};

        mainModels = new ArrayList<>();
        for(int i=0;i<logo.length;i++){
            MainModel mainModel = new MainModel(logo[i], name[i], mobile[i]);
            mainModels.add(mainModel);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        doctorRecyclerView.setLayoutManager(linearLayoutManager);
        doctorRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        hospitalRecyclerView.setLayoutManager(linearLayoutManager1);
        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());

        chatRequestPatientAdapter = new ChatRequestPatientAdapter(mainModels, getContext());
        doctorRecyclerView.setAdapter(chatRequestPatientAdapter);

        appointRequestPatientAdapter = new AppointRequestPatientAdapter(mainModels, getContext());
        hospitalRecyclerView.setAdapter(appointRequestPatientAdapter);

        return rootView;
    }
}
