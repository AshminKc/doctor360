package com.example.doctor360.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.model.DoctorRequestAppoitmentReceiveParams;
import com.example.doctor360.utils.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AppointRequestPatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int limit = 5;
    Context context;
    List<DoctorRequestAppoitmentReceiveParams.DataBean> appointmentList;
    private static final String TAG = "AppointRequestPatientAd";

    public AppointRequestPatientAdapter(List<DoctorRequestAppoitmentReceiveParams.DataBean> mainModels, Context _context){
        this.appointmentList = mainModels;
        this.context = _context;
    }

    @NonNull
    @Override
    public AppointRequestPatientAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appoint_request_single_item, parent, false);
        AppointRequestPatientAdapter.DoctorViewHolder viewHolder= new AppointRequestPatientAdapter.DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final DoctorRequestAppoitmentReceiveParams.DataBean receiveParams = appointmentList.get(position);

        final AppointRequestPatientAdapter.DoctorViewHolder doctorViewHolder = (AppointRequestPatientAdapter.DoctorViewHolder) holder;
        doctorViewHolder.nameTxt.setText(receiveParams.getPatientId().getName());
        doctorViewHolder.dateTxt.setText(receiveParams.getDate());
        doctorViewHolder.timeTxt.setText(receiveParams.getTime());

        if(receiveParams.getPatientId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getPatientId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            doctorViewHolder.imgProfile.setImageBitmap(decodedImage);
        } else {
            doctorViewHolder.imgProfile.setImageResource(R.drawable.noimage);
        }

    }

    @Override
    public int getItemCount() {
        if(appointmentList.size() > limit){
            return limit;
        }
        else
        {
            return appointmentList.size();
        }
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,dateTxt, timeTxt;
        SquareImageView imgProfile;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.txtPatientProfileName);
            dateTxt= itemView.findViewById(R.id.txtPatientReqDate);
            timeTxt = itemView.findViewById(R.id.txtPatientReqTime);
            imgProfile = itemView.findViewById(R.id.imagePatientProfile);

        }

    }
}
