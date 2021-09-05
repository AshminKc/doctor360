package com.example.doctor360.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.activity.RequestDoctorDetailsActivity;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int limit = 5;
    Context context;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedDoctorList;
    private static final String TAG = "DoctorListAdapter";

    public DoctorListAdapter(List<VerifiedDoctorReceiveParams.DataBean> verifiedList, Context _context){
        this.verifiedDoctorList = verifiedList;
        this.context = _context;
    }

    @NonNull
    @Override
    public DoctorListAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_home_doctor_single_item, parent, false);
        DoctorListAdapter.DoctorViewHolder viewHolder= new DoctorListAdapter.DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VerifiedDoctorReceiveParams.DataBean receiveParams = verifiedDoctorList.get(position);

        final DoctorListAdapter.DoctorViewHolder viewHolder = (DoctorListAdapter.DoctorViewHolder) holder;
        viewHolder.nameTxt.setText("DR. "+receiveParams.getName());
        viewHolder.specTxt.setText(receiveParams.getSpecialization());

        if(receiveParams.getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.imgProfile.setImageBitmap(decodedImage);
        } else {
            viewHolder.imgProfile.setImageResource(R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {
        if(verifiedDoctorList.size() > limit){
            return limit;
        }
        else
        {
            return verifiedDoctorList.size();
        }
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,specTxt;
        SquareImageView imgProfile;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.imageDocProfile);
            nameTxt = itemView.findViewById(R.id.txtDocName);
            specTxt = itemView.findViewById(R.id.txtDocSpecialization);

        }

    }
}
