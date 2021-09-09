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
import com.example.doctor360.activity.AppointRequestPatientDetails;
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

        doctorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = receiveParams.getRequestStatus();
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), AppointRequestPatientDetails.class);
                intent.putExtra("appoint_patient_id", receiveParams.getPatientId().get_id());
                intent.putExtra("appoint_patient_name", receiveParams.getPatientId().getName());
                intent.putExtra("appoint_id", receiveParams.get_id());
                intent.putExtra("appoint_date", receiveParams.getDate());
                intent.putExtra("appoint_time", receiveParams.getTime());
                intent.putExtra("appoint_remarks", receiveParams.getDescription());
                intent.putExtra("appoint_status", status);
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

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

            nameTxt = itemView.findViewById(R.id.PatientProfileName);
            dateTxt= itemView.findViewById(R.id.PatientReqDate);
            timeTxt = itemView.findViewById(R.id.PatientReqTime);
            imgProfile = itemView.findViewById(R.id.imagePatientProfile);

        }

    }
}
