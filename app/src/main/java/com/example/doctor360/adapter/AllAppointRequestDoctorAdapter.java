package com.example.doctor360.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.activity.AppointRequestPatientDetails;
import com.example.doctor360.model.DoctorRequestAppoitmentReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AllAppointRequestDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context _context;
    List<DoctorRequestAppoitmentReceiveParams.DataBean> dataBeanList;
    private static final String TAG = "AllAppointRequestDoctor";

    public AllAppointRequestDoctorAdapter(List<DoctorRequestAppoitmentReceiveParams.DataBean> requestList, Context context){
        this._context = context;
        this.dataBeanList = requestList;
    }

    @NonNull
    @Override
    public AllAppointRequestDoctorAdapter.AppointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allappointment_doctor_single_item, parent, false);
        AllAppointRequestDoctorAdapter.AppointViewHolder viewHolder= new AllAppointRequestDoctorAdapter.AppointViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final DoctorRequestAppoitmentReceiveParams.DataBean receiveParams = dataBeanList.get(position);

        final AllAppointRequestDoctorAdapter.AppointViewHolder appointViewHolder = (AllAppointRequestDoctorAdapter.AppointViewHolder) holder;
        appointViewHolder.nameTxt.setText(receiveParams.getPatientId().getName());
        appointViewHolder.dateTxt.setText(receiveParams.getDate());
        appointViewHolder.timeTxt.setText(receiveParams.getTime());
        appointViewHolder.remarksTxt.setText(receiveParams.getDescription());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            appointViewHolder.stausTxt.setText("Accepted");
        else
            appointViewHolder.stausTxt.setText("Pending");

        if(receiveParams.getPatientId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getPatientId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            appointViewHolder.imageView.setImageBitmap(decodedImage);
        } else {
            appointViewHolder.imageView.setImageResource(R.drawable.noimage);
        }

        appointViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
                Activity activity = (Activity) _context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class AppointViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, dateTxt, timeTxt, remarksTxt, stausTxt;
        ImageView imageView;

        public AppointViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.requestPatientNameTXT);
            dateTxt = itemView.findViewById(R.id.requestRequestDateTXT);
            timeTxt = itemView.findViewById(R.id.requestPatientTimeTXT);
            remarksTxt = itemView.findViewById(R.id.requestPatientRemarksTXT);
            stausTxt = itemView.findViewById(R.id.requestPatientStatusTXT);
            imageView = itemView.findViewById(R.id.requestPatientProfilePic);
        }

    }
}
