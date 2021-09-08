package com.example.doctor360.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.model.ViewPatientScheduledAppointmentReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ViewPatientAppointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ViewPatientScheduledAppointmentReceiveParams.DataBean> scheduledList;
    private static final String TAG = "ViewPatientAppointAdapt";

    public ViewPatientAppointAdapter(Context context, List<ViewPatientScheduledAppointmentReceiveParams.DataBean> scheduledList) {
        this.context = context;
        this.scheduledList = scheduledList;
    }

    @NonNull
    @Override
    public ViewPatientAppointAdapter.ScheduledViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_scheduled_appointment_single_item, parent, false);
        ViewPatientAppointAdapter.ScheduledViewHolder viewHolder= new ViewPatientAppointAdapter.ScheduledViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewPatientScheduledAppointmentReceiveParams.DataBean receiveParams = scheduledList.get(position);

        final ViewPatientAppointAdapter.ScheduledViewHolder viewHolder = (ViewPatientAppointAdapter.ScheduledViewHolder) holder;
        viewHolder.txtDoctorName.setText("DR. "+receiveParams.getDoctorId().getName());
        viewHolder.txtDate.setText(receiveParams.getDate());
        viewHolder.txtTime.setText(receiveParams.getTime());
        viewHolder.txtRemarks.setText(receiveParams.getDescription());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            viewHolder.txtStatus.setText("Accepted");
        else
            viewHolder.txtStatus.setText("Rejected");

        if(receiveParams.getDoctorId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getDoctorId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.doctorImage.setImageBitmap(decodedImage);
        } else {
            viewHolder.doctorImage.setImageResource(R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {
        return scheduledList.size();
    }

    public class ScheduledViewHolder extends RecyclerView.ViewHolder {

        TextView txtDoctorName, txtDate, txtTime, txtRemarks, txtStatus;
        ImageView doctorImage;

        public ScheduledViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorImage = itemView.findViewById(R.id.schDoctorImage);
            txtDoctorName = itemView.findViewById(R.id.schDoctorName);
            txtDate = itemView.findViewById(R.id.schDoctorDate);
            txtTime = itemView.findViewById(R.id.schDoctorTime);
            txtRemarks = itemView.findViewById(R.id.schDoctorRemarks);
            txtStatus = itemView.findViewById(R.id.schDoctorStatus);
        }
    }
}
