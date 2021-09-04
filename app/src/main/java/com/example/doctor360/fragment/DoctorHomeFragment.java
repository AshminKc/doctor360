package com.example.doctor360.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.MainModel;
import com.example.doctor360.R;
import com.example.doctor360.activity.DoctorDashboardActivity;
import com.example.doctor360.adapter.AppointRequestPatientAdapter;
import com.example.doctor360.adapter.ChatRequestPatientAdapter;
import com.example.doctor360.adapter.ImageSliderAdapter;
import com.example.doctor360.helper.ConnectionDetector;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

public class DoctorHomeFragment extends Fragment {

    View rootView;
    private long delayTime =  5000;
    ImageSliderAdapter imageSliderAdapter;
    private AutoScrollViewPager viewPager;
    private CircleIndicator pageIndicatorView;
    TextView viewAllChatTXT, viewAllAppointTXT;
    RecyclerView chatRecyclerView, appointRecyclerView;
    ChatRequestPatientAdapter chatRequestPatientAdapter;
    AppointRequestPatientAdapter appointRequestPatientAdapter;
    ConnectionDetector connectionDetector;
    ArrayList<MainModel> mainModels;
    private static final String TAG = "DoctorHomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_doctor_home,container,false);

        viewPager = rootView.findViewById(R.id.sliderViewPager);
        pageIndicatorView = rootView.findViewById(R.id.pageIndicator);
        viewAllChatTXT = rootView.findViewById(R.id.txtViewAllChat);
        viewAllAppointTXT = rootView.findViewById(R.id.txtViewAllAppoint);
        chatRecyclerView = rootView.findViewById(R.id.chatRequestRecyclerView);
        appointRecyclerView = rootView.findViewById(R.id.appointRequestRecyclerView);

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
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        appointRecyclerView.setLayoutManager(linearLayoutManager1);
        appointRecyclerView.setItemAnimator(new DefaultItemAnimator());

        chatRequestPatientAdapter = new ChatRequestPatientAdapter(mainModels, getContext());
        chatRecyclerView.setAdapter(chatRequestPatientAdapter);

        appointRequestPatientAdapter = new AppointRequestPatientAdapter(mainModels, getContext());
        appointRecyclerView.setAdapter(appointRequestPatientAdapter);

        viewAllChatTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction ft = fm.beginTransaction();
                RequestAppointmentDoctorFragment technologyNewsFragment = new RequestAppointmentDoctorFragment();
                ft.replace(R.id.fragmentContainer2, technologyNewsFragment,"educationFragment").addToBackStack("educationFragment").commit();
                ((DoctorDashboardActivity) getActivity()).setToolbarAndNavView(getContext(),3, getResources().getString(R.string.menu_appointment_request));
            }
        });

        viewAllAppointTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DoctorDashboardActivity.class);
                intent.putExtra("view_all_appoint_request", 2);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
