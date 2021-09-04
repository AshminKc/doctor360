package com.example.doctor360.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doctor360.R;

public class PatientAskQuestionFragment extends Fragment {

    View rootView;
    private static final String TAG = "PatientAskQuestionFragm";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_ask_question_patient,container,false);

        return rootView;
    }
}
