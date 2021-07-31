package com.example.doctor360.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.doctor360.R;
import com.example.doctor360.adapter.DoctorListPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DoctorListFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;
    private DoctorListPagerAdapter doctorListPagerAdapter;
    private TabLayout tabLayout;
    private View firstTab, secondTab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_doctor_list,container,false);

        viewPager = rootView.findViewById(R.id.doctorListViewPager);
        tabLayout = rootView.findViewById(R.id.doctorListTabs);

        doctorListPagerAdapter = new DoctorListPagerAdapter(getChildFragmentManager());
        doctorListPagerAdapter.addFragment(new PendingDoctorListFragment(),getString(R.string.pending_doctors));
        doctorListPagerAdapter.addFragment(new VerifiedDoctorListFragment(),getString(R.string.verified_doctors));

        viewPager.setAdapter(doctorListPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

        firstTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        secondTab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);

        firstTab.setBackground(getResources().getDrawable(R.drawable.tab_selected));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int selectedTabPosition = tab.getPosition();

                if (selectedTabPosition == 0) {
                    firstTab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                    secondTab.setBackground(getResources().getDrawable(R.drawable.tab_unselected));
                } else  {
                    firstTab.setBackground(getResources().getDrawable(R.drawable.tab_unselected));
                    secondTab.setBackground(getResources().getDrawable(R.drawable.tab_selected));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }
}
